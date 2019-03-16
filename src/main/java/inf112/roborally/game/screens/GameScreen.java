package inf112.roborally.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import inf112.roborally.game.Main;
import inf112.roborally.game.RoboRallyGame;
import inf112.roborally.game.board.*;
import inf112.roborally.game.enums.Direction;
import inf112.roborally.game.gui.Hud;
import inf112.roborally.game.objects.Player;


public class GameScreen implements Screen {

    public static String mapPath = RoboRallyGame.TEST_MAP;
    private final RoboRallyGame game;
    private final Hud hud;
    private final GameLogic gameLogic;

    private final Board board;
    private final Player player;
    private Music music;


    Background bg;

    public GameScreen(RoboRallyGame game, String mapPath) {
        this.mapPath = mapPath;
        this.game = game;

        board = new VaultBoard();

        board.addPlayer(new Player("Player1", "assets/robot/butlerclaptrap.png", Direction.NORTH, board));
        board.addPlayer(new Player("Player2", "assets/robot/claptrap.png", Direction.SOUTH, board));
        board.placePlayers();

        hud = new Hud(board.getPlayers().get(0));
        gameLogic = new GameLogic(board, hud.getCardsInHandDisplay());
        player = board.getPlayers().get(0);

        // Music
        music = Gdx.audio.newMusic(Gdx.files.internal("assets/music/testMusic1.ogg"));
        music.setLooping(true);
        music.setVolume(0.3f);

        bg = new Background();

        game.camera.zoom = 0.4f;
        game.camera.position.set(board.getWidth() / 2 * Main.PIXELS_PER_TILE,
                board.getHeight() / 2 * Main.PIXELS_PER_TILE, 0);
        game.camera.update();
    }

    @Override
    public void show() {
        music.play();
    }

    @Override
    public void render(float delta) {

        update();
        float r = 10 / 255f;
        float g = 10 / 255f;
        float b = 10 / 255f;

        //The function glClearColor takes in values between 0 and 1. It creates the background color.
        Gdx.gl.glClearColor(r, g, b, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bg.draw();

        board.render(game.camera);

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        board.drawGameObjects(game.batch);
        game.batch.end();

        hud.draw();

        // Mute music
        if (board.boardWantsToMuteMusic()) {
            music.stop();
            board.musicIsMuted();
            board.killTheSound();
        }

    }

    private void update() {
        gameLogic.update();
        game.cameraListener.update();
        bg.update(game.camera);
    }


    @Override
    public void dispose() {
        System.out.println("disposing game screen");
        bg.dispose();

        game.batch.dispose();
        board.dispose();
        for (Player player : board.getPlayers()) {
            player.getSprite().getTexture().dispose();
            player.getBackup().getSprite().getTexture().dispose();
        }
        hud.dispose();
        music.dispose();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resize(int width, int height) {
        game.viewPort.update(width, height);
        hud.resize(width, height);
    }

    public Hud getHud() {
        return hud;
    }
}


