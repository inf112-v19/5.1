package inf112.roborally.game.gui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import inf112.roborally.game.ProgramRegisters;
import inf112.roborally.game.objects.Player;

public class RegisterVisuals {
    private Player player;
    private ProgramRegisters registers;
    private Sprite board;
    private Sprite lifetoken;
    private Sprite damagetoken;
    private Sprite locktoken;
    private Sprite card;

    float scale = .66f;

    public RegisterVisuals(Player player) {
        this.player = player;
        registers = player.getRegisters();
        board = new Sprite(new Texture("assets/cards/programregistertemplate.png"));
        lifetoken = new Sprite(new Texture("assets/cards/lifetoken.png"));
        damagetoken = new Sprite(new Texture("assets/cards/damagetoken.png"));
        locktoken = new Sprite(new Texture("assets/cards/locktoken.png"));
        card = new Sprite(new Texture("assets/cards/testcard.png"));

        board.setSize(board.getWidth()*scale, board.getHeight()*scale);
        lifetoken.setSize(lifetoken.getWidth()*scale, lifetoken.getHeight()*scale);
        locktoken.setSize(locktoken.getWidth()*scale, locktoken.getHeight()*scale);
        damagetoken.setSize(damagetoken.getWidth()*scale, damagetoken.getHeight()*scale);
        card.setSize(card.getWidth()*scale, card.getHeight()*scale);
    }

    public void draw(SpriteBatch batch, OrthographicCamera camera, FitViewport port) {
        camera.position.set(board.getWidth() - 1920 / 2 + 100*scale, 1080 / 2 - 100*scale, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        board.draw(batch);
        drawLifeTokens(batch);
        drawDamageTokens(batch);
        drawLocks(batch);
        drawCardsInRegisters(batch);
    }

    private void drawLifeTokens(SpriteBatch batch) {
        for (int i = 0; i < player.getLives(); i++) {
            lifetoken.setPosition(732*scale + 80.5f*scale * i, board.getHeight() - 101*scale);
            lifetoken.draw(batch);
        }
    }

    private void drawDamageTokens(SpriteBatch batch) {
        for (int i = 0; i < player.getDamage(); i++) {
            damagetoken.setPosition(893*scale - 79*scale * i, board.getHeight() - 175*scale);
            damagetoken.draw(batch);

            if (i > 8) return;
        }
    }

    private void drawLocks(SpriteBatch batch) {
        for (int i = 4; i >= 0; i--) {
            if (registers.isLocked(4 - i)) {
                locktoken.setPosition(880*scale - 204*scale * i, board.getHeight() - 290*scale);
                locktoken.draw(batch);
            }
        }
    }

    private void drawCardsInRegisters(SpriteBatch batch) {
        for (int i = 0; i < 5; i++) {
            if (player.getRegisters().getCardInRegister(i) != null) {
                card.setPosition(26*scale + 205*scale * i, 30*scale);
//                card.setRegion(cardVisual.getRegion(registers.getCardInRegister(i)));
                card.draw(batch);
            }
        }
    }


}