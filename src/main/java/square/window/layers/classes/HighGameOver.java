package square.window.layers.classes;

import square.events.Dispatcher;
import square.events.Event;
import square.events.types.KeyPressedEvent;
import square.events.types.KeyReleasedEvent;
import square.events.types.NewGameEvent;
import square.objects.Score;
import square.window.Window;
import square.window.layers.interfaces.KeyboardControlledLayer;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HighGameOver implements KeyboardControlledLayer {
    private Window window;
    private StringBuilder stringBuilder = new StringBuilder();
    private String name;

    public HighGameOver(Window window) {
        this.window = window;
    }

    @Override
    public void onRender(Graphics g, float interpolation) {
        g.setColor(Color.RED);
        g.fillRect(0, 0, window.getWidth(), window.getHeight());
        g.setColor(Color.ORANGE);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 32));

        int drawX = window.getWidth() / 2;
        int drawY = window.getHeight() / 2;

        g.drawString("Game Over!", drawX - getStringWidth(g, "Game Over!") / 2, drawY - 50);

        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));

        g.setColor(Color.WHITE);
        String scoreStr = "New High Score: " + window.getPlayer().getScore();
        g.drawString(scoreStr, drawX - getStringWidth(g, scoreStr) / 2, drawY);

        g.setColor(Color.ORANGE);
        String nameStr = "Name: " + (name == null ? "" : name);
        g.drawString(nameStr, drawX - getStringWidth(g, nameStr) / 2, drawY + 50);

        String infoString = "Press enter to play again";
        g.drawString(infoString, drawX - getStringWidth(g, infoString) / 2, drawY + 100);
    }

    private int getStringWidth(Graphics g, String s) {
        return g.getFontMetrics().stringWidth(s);
    }

    @Override
    public int getRenderX() {
        return 0;
    }

    @Override
    public int getRenderY() {
        return 0;
    }

    @Override
    public void onEvent(Event event) {
        Dispatcher dispatcher = new Dispatcher(event);
        dispatcher.dispatch(Event.Type.KEY_PRESSED, event1 -> onKeyPressed((KeyPressedEvent) event1));
    }

    @Override
    public boolean onKeyReleased(KeyReleasedEvent event) {
        return false;
    }

    @Override
    public boolean onKeyPressed(KeyPressedEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            window.getScores().addScore(new Score(window.getPlayer().getScore(), stringBuilder.toString()));
            window.onEvent(new NewGameEvent());
            window.getLayers().remove(this);
        }
        String ch = KeyEvent.getKeyText(event.getKeyCode());
        if (ch.length() < 2)
            stringBuilder.append(ch);
        name = stringBuilder.toString();
        return true;
    }
}
