package square.window.layers.classes;

import square.events.Dispatcher;
import square.events.Event;
import square.events.types.FoodEatenEvent;
import square.window.Window;
import square.window.layers.interfaces.Layer;

import java.awt.*;
import java.util.List;
import java.util.Random;

import static square.Config.SPRITE_SIZE;

public class Food implements Layer {
    private static final int SCORE_VALUE = 10;
    private final Window window;
    private Color color = Color.GREEN;
    private Rectangle square;
    private Random random = new Random();

    public Food(Window window) {
        this.window = window;
        init();
    }

    void init() {
        init(0);
    }

    void init(int depth) {
        if (depth > 10)
            return;
        square = new Rectangle(random.nextInt(window.getWidth() / SPRITE_SIZE) * SPRITE_SIZE,
                random.nextInt(window.getHeight() / SPRITE_SIZE) * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
        if (this instanceof Poison)
            if (!window.getPlayer().safePosition(square))
                init(depth++);
        List<Layer> layers = window.getLayers();
        for (Layer layer : layers)
            if (square.x == layer.getRenderX() && square.y == layer.getRenderY()) {
                init(depth++);
            }
    }

    public Rectangle getSquare() {
        return square;
    }

    public void setSquare(Rectangle square) {
        this.square = square;
    }

    public int getScoreValue() {
        return SCORE_VALUE;
    }

    @Override
    public void onRender(Graphics g, float interpolation) {
        g.setColor(color);
        g.fillRect(square.x, square.y, SPRITE_SIZE, SPRITE_SIZE);
    }

    @Override
    public int getRenderX() {
        return square.x;
    }

    @Override
    public int getRenderY() {
        return square.y;
    }

    @Override
    public void onEvent(Event event) {
        Dispatcher dispatcher = new Dispatcher(event);
        dispatcher.dispatch(Event.Type.FOOD_EATEN, (event1) -> getEaten((FoodEatenEvent) event1));
    }

    private boolean getEaten(FoodEatenEvent event) {

        if (this == event.getFood()) {
            window.getLayers().remove(this);
            return true;
        }
        return false;
    }

}
