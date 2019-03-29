package inf112.roborally.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import inf112.roborally.game.RoboRallyGame;
import inf112.roborally.game.enums.GameState;
import inf112.roborally.game.enums.PlayerState;
import inf112.roborally.game.enums.Rotate;
import inf112.roborally.game.gui.Hud;
import inf112.roborally.game.objects.Player;

public class GameLogic extends BoardLogic {

    private final Hud hud;
    private Board board;
    private RoboRallyGame game;
    private Player player1;


    public GameLogic(Board board, Hud hud, RoboRallyGame game) {
        super(board.getPlayers());
        this.game = game;
        this.board = board;
        this.hud = hud;
        player1 = players.get(0);

        //TODO: Player choosing which direction to face needs to happen when the game initially starts.
        update();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            game.setScreen(game.settingsScreen);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            System.out.println("Switched to EndGame screen");
            game.endGameScreen.addWinner(player1);
            game.setScreen(game.endGameScreen);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            players.get(0).getRegisters().returnCards(players.get(0));
            hud.clearAllCards();
            hud.updateCards();
        }

        boolean updatePlayer = true;

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player1.move(1);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            player1.rotate(Rotate.LEFT);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player1.rotate(Rotate.RIGHT);
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player1.reverse();
        }
        else {
            updatePlayer = false;
        }
        if (updatePlayer) {
            board.boardMoves();
            updatePlayers();
        }
    }

    public void update() {
        handleInput();
        updatePlayers();
        switch (state) {
            case BETWEEN_ROUNDS:
                doBeforeRound();
                break;
            case PICKING_CARDS:
                checkIfReady();
                break;
            case ROUND:
                doPhase();
                break;
            case BOARD_MOVES:
                boardMoves();
                break;
            case GAME_OVER:
                endGame();
        }
    }

    @Override
    protected void doBeforeRound() {
        super.doBeforeRound();
        hud.clearAllCards();
        hud.updateCards();
    }

    @Override
    public void checkIfReady() {
        if (player1.isReady()) {
            state = GameState.ROUND;
            if (player1.playerState == PlayerState.READY)
                player1.playerState = PlayerState.OPERATIONAL;
            state = GameState.ROUND;
        }
    }

    @Override
    protected void boardMoves() {
        board.boardMoves();
        super.boardMoves();
    }

    @Override
    protected void endGame() {
        game.endGameScreen.addWinner(checkIfAPlayerHasWon());
        game.setScreen(game.endGameScreen);
    }

    @Override
    protected void cleanBoard() {
        board.cleanUp();
    }


    //Test if Rotate left tile works

    public void runTests() {
        rotateLeftTileTest();
        rotateRightTileTest();
        robotLaserDamageTest();
        boardLaserDamageTest();
        wallStopRobotLaserTest();
        repairSiteTest();
        blueBeltTest();
        pinkBeltTest();
        flagTest();
    }

    //Test if Rotate left tile works

    public void rotateLeftTileTest() {
        System.out.println("\nTEST - rotateLeftTileTest");
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        System.out.println("Expected direction: SOUTH : Actual direction: " + player1.getDirection());

    }

    public void execute(ProgramCard card) {
        player1.receiveCard(card);
        player1.getRegisters().placeCard(0);
        player1.getRegisters().executeCard(0);
    }

    //Test if Rotate right tile works
    public void rotateRightTileTest() {
        System.out.println("\nTEST - rotateRightTileTest");
        execute(new ProgramCard(Rotate.UTURN, 0, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.NONE, 2, 10, ""));
        board.boardMoves();
        System.out.println("Expected direction: NORTH : Actual direction: " + player1.getDirection());
    }

    //Test if Robot laser gives 1 damage to enemy robots
    public void robotLaserDamageTest() {
        System.out.println("\nTEST - robotLaserDamageTest");
        execute(new ProgramCard(Rotate.UTURN, 0, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.RIGHT, 0, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        System.out.println("Expected damage: 1 : Actual damage: " + player1.getDamage());
    }

    //Test if the lasers on the board gives 1 damage
    public void boardLaserDamageTest() {
        System.out.println("\nTEST - boardLaserDamageTest");
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        System.out.println("Expected damage: 2 : Actual damage: " + player1.getDamage());
    }

    //Test if wall stops robot lasers
    public void wallStopRobotLaserTest() {
        System.out.println("\nTEST - wallStopRobotLaserTest");
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        System.out.println("Expected damage: 0 : Actual damage : " + players.get(3).getDamage());

    }

    //Test if repairSite changes backup location
    public void repairSiteTest() {
        System.out.println("\nTEST - repairSiteTest");
        execute(new ProgramCard(Rotate.RIGHT, 0, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.NONE, 2, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.LEFT, 0, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        System.out.println("Expected position: X = 2, Y = 4 \nActual position: X = " + player1.getBackup().getX() + ", Y = " + player1.getBackup().getY());
    }

    //Test if blue belt moves the robot 1 tile
    public void blueBeltTest() {
        System.out.println("\nTEST - blueBeltTest");
        execute(new ProgramCard(Rotate.RIGHT, 0, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        System.out.println("Expected position: X = 3, Y = 3 \nActual position: X = " + player1.getX() + ", Y = " + player1.getY());
    }

    //Test if pink belt moves the robot 2 tiles
    public void pinkBeltTest() {
        System.out.println("\nTEST - pinkBeltTest");
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        System.out.println("Expected position: X = 4, Y = 1 \nActual position: X = " + player1.getX() + ", Y = " + player1.getY());
    }

    /*
    Under work
     */
    public void flagTest() {
        System.out.println("\nTEST - flagTest");
        execute(new ProgramCard(Rotate.RIGHT, 1, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.RIGHT, 1, 10, ""));
        board.boardMoves();
        execute(new ProgramCard(Rotate.NONE, 1, 10, ""));
        board.boardMoves();
        System.out.println("Expected position: X = 3, Y = 1 \nActual position: X = " + player1.getBackup().getX() + ", Y = " + player1.getBackup().getY());
    }
}

