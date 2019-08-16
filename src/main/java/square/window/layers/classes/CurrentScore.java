package square.window.layers.classes;

import square.events.Dispatcher;
import square.events.Event;
import square.events.types.FoodEatenEvent;
import square.objects.Score;
import square.window.Window;
import square.window.layers.interfaces.Layer;
import square.window.layers.interfaces.Player;

import java.awt.*;
import java.util.List;

public class CurrentScore implements Layer {
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0.15f);
    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 15);
    private final Window window;
    private int currentScore = 0;
    private Player player;
    private Score scoreToBeat = new Score(0, "");

    public CurrentScore(Player player, Window window) {
        this.player = player;
        this.window = window;
        getScoreToBeat();
    }

    public int getCurrentScore() {
        return currentScore;
    }

    private int getStringWidth(Graphics g, String string) {
        return g.getFontMetrics().stringWidth(string);
    }

    @Override
    public void onRender(Graphics g, float interpolation) {
        g.setColor(TRANSPARENT);
        g.setFont(FONT);
        g.fillRect(0, 0, 20 + getStringWidth(g, "Score :" + currentScore), 15);
        String scoreToBeatStr = "Score to beat:";
        g.fillRect(window.getWidth() - getStringWidth(g, scoreToBeatStr), 0, getStringWidth(g, scoreToBeatStr), 15);
        String scoreToBeat = this.scoreToBeat.toString();
        g.fillRect(window.getWidth() - getStringWidth(g, scoreToBeat), 15, getStringWidth(g, scoreToBeat), 15);
        g.setColor(new Color(0, 0, 0, 0.5f));
        g.drawString(scoreToBeatStr, window.getWidth() - getStringWidth(g, scoreToBeatStr), 10);
        g.drawString(scoreToBeat, window.getWidth() - getStringWidth(g, scoreToBeat), 25);
        g.drawString("Score :" + currentScore, 10, 10);
    }

    @Override
    public int getRenderX() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getRenderY() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onEvent(Event event) {
        Dispatcher dispatcher = new Dispatcher(event);
        dispatcher.dispatch(Event.Type.FOOD_EATEN, event1 -> foodEaten((FoodEatenEvent) event1));
    }

    private boolean foodEaten(FoodEatenEvent event) {
        currentScore += event.getScoreValue();
        getScoreToBeat();
        player.setScore(currentScore);
        return false;
    }

    private void getScoreToBeat() {
        List<Score> scores = window.getScores().getScores();
        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            if (currentScore > score.getScore()) {
                break;
            }
            scoreToBeat = score;
        }
    }
}
