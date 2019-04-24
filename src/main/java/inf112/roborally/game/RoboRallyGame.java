package inf112.roborally.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.roborally.game.board.Board;
import inf112.roborally.game.gui.CameraListener;
import inf112.roborally.game.objects.Flag;
import inf112.roborally.game.player.Player;
import inf112.roborally.game.screens.*;
import inf112.roborally.game.screens.setup.PlaceFlagsScreen;
import inf112.roborally.game.screens.setup.SelectMapScreen;
import inf112.roborally.game.screens.setup.SelectSkinScreen;
import inf112.roborally.game.server.ChatClient;
import inf112.roborally.game.server.ChatServer;
import inf112.roborally.game.tools.AssMan;

import java.util.ArrayList;
import java.util.List;

import static inf112.roborally.game.enums.Direction.NORTH;

public class RoboRallyGame extends Game {
    //MAPS:
    public static final String VAULT = "assets/maps/vault.tmx";
    public static final String SPIRAL_MARATHON = "assets/maps/spiralmarathon.tmx";
    public static final String TEST_MAP = "assets/maps/testMap.tmx";
    public static final String LASER_TEST_MAP = "assets/maps/lasertest.tmx";
    public static final String SPACE_BUG = "assets/maps/space_bug.tmx";
    public static final String SPACE_BUG2 = "assets/maps/space_bug2.tmx";
    public static final String AROUND_THE_WORLD = "assets/maps/around_the_world.tmx";

    public static final int MAX_PLAYERS = 8;

    public boolean AIvsAI = false;

    public OrthographicCamera dynamicCamera;
    public Viewport dynamicViewPort;
    public CameraListener cameraListener;

    public OrthographicCamera fixedCamera; //the position of this camera should never change!
    public FitViewport fixedViewPort;

    public SpriteBatch batch;

    //    public SetupScreen setupScreen;
    public SelectSkinScreen selectSkinScreen;
    public SelectMapScreen selectMapScreen;
    public PlaceFlagsScreen placeFlagsScreen;
    public GameScreen gameScreen;
    public SettingsScreen settingsScreen;
    public EndGameScreen endGameScreen;

    public TestScreen testScreen;
    public LaserTestScreen laserTestScreen;

    public static boolean soundMuted;

    /**
     * The screen that was active before setting a new screen with {@link #setScreen(Screen)}
     */
    private Screen screenBefore;
    public ChatServer server;
    public ChatClient client;

    public ArrayList<String> playerNames;
    public Board board;
    public String playerName;

    @Override
    public void create() {
        playerNames = new ArrayList<>();
        AssMan.load();
        AssMan.manager.finishLoading();
        AIvsAI = false;

        board = new Board(this);

        dynamicCamera = new OrthographicCamera();
        dynamicCamera.setToOrtho(false);
        dynamicCamera.update();
        dynamicViewPort = new FitViewport(1920, 1080, dynamicCamera);
        cameraListener = new CameraListener(dynamicCamera);

        fixedCamera = new OrthographicCamera();
        fixedCamera.update();
        fixedViewPort = new FitViewport(1920, 1080, fixedCamera);

        batch = new SpriteBatch();

        settingsScreen = new SettingsScreen(this);

        endGameScreen = new EndGameScreen(this);
        selectSkinScreen = new SelectSkinScreen(this);
        selectMapScreen = new SelectMapScreen(this);

        testScreen = new TestScreen(this);
        laserTestScreen = new LaserTestScreen(this);

        setScreen(new BetterMenu(this));
    }

    /**
     * Sets the current screen. {@link Screen#hide()} is called on any old screen, and {@link Screen#show()} is called on the new
     * screen, if any.
     * <p>
     * Saves the screen that was used before the function call.
     *
     * @param screen may be {@code null}
     */
    @Override
    public void setScreen(Screen screen) {
        this.screenBefore = getScreen();
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public void newGame() {
        dispose();
        create();
    }


    /**
     * Create a new GameScreen with preset map, flag positions and player skin.
     */
    public void createDefaultGameScreen() {
        createDefaultBoard();
        gameScreen = new GameScreen(this);
    }

    /**
     * Create a new GameScreen with chosen player skin, map and flag positions.
     * @see SelectSkinScreen
     * @see SelectMapScreen
     * @see PlaceFlagsScreen
     */
    public void createCustomGameScreen() {
        List<Player> players = createNumberOfPlayers(MAX_PLAYERS); //Todo: Get user input
        if(players != null){
            board.addPlayersToBoard(players);
        }else{
            board.addPlayersToBoard(createNumberOfPlayers(MAX_PLAYERS));
        }
        gameScreen = new GameScreen(this);
    }


    /**
     * Create a new board with preset board map and flag locations.
     */
    private void createDefaultBoard() {
        board.createBoard(VAULT);
        board.getFlags().add(new Flag(7, 7, 1));
        board.getFlags().add(new Flag(11, 11, 2));
        board.getFlags().add(new Flag(10, 10, 3));
        board.addPlayersToBoard(createDefaultPlayers());
        board.findLaserGuns();
    }


    private List<Player> createDefaultPlayers(){
        return createNumberOfPlayers(MAX_PLAYERS);
    }

    /**
     * Creates a list of players. The chosen skin is set here.
     * The players can be added to the board with {@link Board#addPlayersToBoard(List)}.
     * May return null.
     *
     * @param numberOfPlayers the number of players that should be created.
     * @return the list of players, null if {@param numberOfPlayers} is not possible to make.
     */
    public List<Player> createNumberOfPlayers(int numberOfPlayers){
        if(numberOfPlayers < 1 || numberOfPlayers > MAX_PLAYERS){
            return null;
        }

        List<Player> players = new ArrayList<>();
        int index = selectSkinScreen.getChoiceIndex();

        int numberOfSkins = AssMan.getPlayerSkins().length;
        for(int i = 0; i < numberOfPlayers; i++){
            if(index >= numberOfSkins){
                index = 0;
            }
            Player player = new Player("Player" + (i+1), AssMan.getPlayerSkins()[index], NORTH, board);
            players.add(player);
            index++;
        }
        return players;
    }

    public void createTestBoard(){
        board.createBoard(TEST_MAP);
        board.getFlags().add(new Flag(1, 7, 1));
        board.addPlayersToBoard(createDefaultPlayers());
        board.findLaserGuns();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        System.out.println("Disposing RoboRallyGame");
        if (screenBefore != null) {
            screenBefore.dispose();
        }
        batch.dispose();
        testScreen.dispose();
        AssMan.dispose();
        if (gameScreen != null) {
            gameScreen.dispose();
        }
    }

    public String chosenMap(int mapIndex) {
        String[] mapChoices = new String[4];
        mapChoices[0] = VAULT;
        mapChoices[1] = SPACE_BUG;
        mapChoices[2] = SPACE_BUG2;
        mapChoices[3] = AROUND_THE_WORLD;

        return mapChoices[mapIndex];
    }


    public void joinGame(String ip) {
        System.out.println(playerName + " wants to connect to " + ip);
        try {
            client = new ChatClient(ip, 8000, this, playerName);
            new Thread(client).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Screen getScreenBefore() {
        return this.screenBefore;
    }

    public Board getBoard() {
        return board;
    }

    public void startServer() {
        server = new ChatServer(8000, this);
        new Thread(server).start();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
