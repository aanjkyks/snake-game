package square.window.layers.interfaces;

import java.awt.*;

public interface Player extends Layer {
    Rectangle getBounds();

    int getScore();

    void setScore(int score);

    void init();

    boolean safePosition(Rectangle rectangle);
}
