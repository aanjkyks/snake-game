package square.window.layers.interfaces;

import square.events.EventListener;

import java.awt.*;

public interface Layer extends EventListener {
    void onRender(Graphics g, float interpolation);

    int getRenderX();

    int getRenderY();
}
