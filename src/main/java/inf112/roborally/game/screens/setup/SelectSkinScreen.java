package inf112.roborally.game.screens.setup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import inf112.roborally.game.RoboRallyGame;
import inf112.roborally.game.player.Player;
import inf112.roborally.game.tools.AssMan;


/**
 * Screen for selecting a player skin.
 *
 * {@link #choiceIndex} is the index for the chosen player skin.
 */
public class SelectSkinScreen extends SelectScreen {

    public SelectSkinScreen(final RoboRallyGame game) {
        super(game, AssMan.getPlayerSkins().length);
    }

    @Override
    protected void setChoicesBasedOnScreen() {
        choices = new TextureRegionDrawable[numberOfChoices];
        for (int i = 0; i < numberOfChoices; i++) {
            choices[i] = new TextureRegionDrawable(AssMan.getPlayerSkins()[i]);
        }
        information = "skin";
    }

    @Override
    public void completeChoice() {
        for (Player player : game.board.getPlayers()) {
            player.setSkinTexture(AssMan.getPlayerSkins()[choiceIndex]);
        }

        game.setScreen(game.selectMapScreen);
        dispose();
    }

    public int getChoiceIndex(){
        return choiceIndex;
    }
}
