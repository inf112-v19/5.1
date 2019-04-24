package inf112.roborally.game.screens.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import inf112.roborally.game.RoboRallyGame;
import inf112.roborally.game.screens.BasicScreen;
import inf112.roborally.game.screens.BetterMenu;
import inf112.roborally.game.tools.ButtonFactory;

public class MultiplayerScreen extends BasicScreen {

    public MultiplayerScreen(final RoboRallyGame game) {
        super(game);
        TextButton join = ButtonFactory.createTextButton("Join Session", 2);
        join.setTransform(true);
        join.setWidth(700);
        join.setPosition(1920 / 2f - join.getWidth() / 2f, 550);
        join.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new NameScreen(game) {
                    @Override
                    protected boolean confirmInput() {
                        game.playerName = text.getText();
                        game.setScreen(new JoinServerScreen(game));
                        return true;
                    }
                });
                dispose();
            }
        });
        TextButton create = ButtonFactory.createTextButton("Create Session", 2);
        create.setTransform(true);
        create.setWidth(700);
        create.setPosition(1920 / 2f - create.getWidth() / 2f, 350);
        create.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                Screen screen = new HostServerScreen(game);
//                game.setScreen(screen);
                game.setScreen(new NameScreen(game) {
                    @Override
                    protected boolean confirmInput() {
                        game.playerName = text.getText();
                        game.setScreen(new HostServerScreen(game));
                        return true;
                    }
                });
                dispose();
            }
        });

        stage.addActor(join);
        stage.addActor(create);
    }

    @Override
    protected void goToPreviousScreen() {
        BetterMenu menu = new BetterMenu(game);
        game.setScreen(menu);
        dispose();
    }


    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) ;
    }
}
