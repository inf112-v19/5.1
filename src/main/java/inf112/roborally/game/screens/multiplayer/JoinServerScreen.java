package inf112.roborally.game.screens.multiplayer;

import com.badlogic.gdx.Screen;
import inf112.roborally.game.RoboRallyGame;
import inf112.roborally.game.screens.InputFieldScreen;

public class JoinServerScreen extends InputFieldScreen {
    private String ip;
    private String name;

    public JoinServerScreen(final RoboRallyGame game) {
        super(game);
        text.setText("'Enter server ip'");
        confirm.setText("Connect");
    }

    @Override
    protected void goToPreviousScreen() {
        Screen nameScreen = new NameScreen(game);
        game.setScreen(nameScreen);
        dispose();
    }

    @Override
    protected boolean confirmInput() {
        if (!clicked || text.getText().length() < 3) return false;
        ip = text.getText();
        game.joinGame(ip);
        System.out.println("Trying to connect to: " + ip);
        Screen s = new LobbyScreen(game);
        game.setScreen(s);
        return true;
    }
}
