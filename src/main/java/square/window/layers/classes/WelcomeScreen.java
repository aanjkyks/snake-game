package square.window.layers.classes;

import square.events.Dispatcher;
import square.events.Event;
import square.window.Window;
import square.window.layers.interfaces.Layer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WelcomeScreen implements Layer {
    private List<String> list = new ArrayList<>();
    private Window window;

    public WelcomeScreen(Window window) {
        this.window = window;
        list.add("Welcome to Snake!");
        list.add("The best game on the planet!");
        list.add("You, the blue snake, get points");
        list.add("For eating green squares");
        list.add("Beware red squares!");
        list.add("Controls :");
        list.add("Arrow keys - movement");
        list.add("R - allow player to move");
        list.add("Randomly by itself");
        list.add("Press any key to continue");

    }

    @Override
    public void onRender(Graphics g, float interpolation) {
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, window.getWidth(), window.getHeight());
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
        for (int i = list.size() - 1; i >= 0; i--) {
            g.drawString(list.get(i), window.getWidth() / 2 - g.getFontMetrics().stringWidth(list.get(i)) / 2,
                    window.getHeight() / 3 * 2 - 24 * (list.size() - i));
        }
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
        dispatcher.dispatch(Event.Type.KEY_PRESSED, event1 -> close());
    }

    private boolean close() {
        window.getLayers().remove(this);
        return true;
    }
}
