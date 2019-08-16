package square.window.layers.classes;

import square.events.Event;
import square.objects.Score;
import square.window.Window;
import square.window.layers.interfaces.Layer;

import java.awt.*;
import java.util.List;

public class ScoreBoard implements Layer {
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0.15f);
    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    private final Window window;

    public ScoreBoard(Window window) {
        this.window = window;
    }

    @Override
    public void onRender(Graphics g, float interpolation) {
        g.setFont(FONT);
        g.setColor(TRANSPARENT);
        int hiScoresWidth = getStringWidth(g, "Hi scores:");
        g.fillRect(window.getWidth() - hiScoresWidth, 0, hiScoresWidth, 18);
        g.setColor(Color.ORANGE);
        g.drawString("Hi scores:", window.getWidth() - hiScoresWidth, 15);
        List<Score> scores = window.getScores().getScores();
        for (int i = 0; i < scores.size(); i++) {
            Score score1 = scores.get(i);
            g.setColor(TRANSPARENT);
            int scoreWidth = getStringWidth(g, score1.toString());
            g.fillRect(window.getWidth() - scoreWidth, 20 + i * 20, scoreWidth, 18);
            g.setColor(Color.ORANGE);
            g.drawString(score1.toString(), window.getWidth() - scoreWidth, 40 + i * 20 - 5);
        }


    }

    @Override
    public int getRenderX() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getRenderY() {
        return getRenderX();
    }

    private int getStringWidth(Graphics g, String string) {
        return g.getFontMetrics().stringWidth(string);
    }


    @Override
    public void onEvent(Event event) {

    }
}
