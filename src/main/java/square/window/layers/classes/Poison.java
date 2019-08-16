package square.window.layers.classes;

import square.events.Dispatcher;
import square.events.Event;
import square.events.types.ClockTickEvent;
import square.window.Window;
import square.window.layers.interfaces.ClockTickLayer;

import java.awt.*;
import java.util.Random;

import static square.Config.SPRITE_SIZE;

public class Poison extends Food implements ClockTickLayer {
    private final Random RANDOM = new Random();
    private Color color = Color.RED;
    private int ticks = 0;
    private int lastX, lastY;

    public Poison(Window window) {
        super(window);
    }


    @Override
    public void onRender(Graphics g, float interpolation) {
        int diffX = (int) ((super.getRenderX() - lastX) * interpolation);
        int diffY = (int) ((super.getRenderY() - lastY) * interpolation);
        g.setColor(color);
        g.fillRect(lastX + diffX, lastY + diffY, SPRITE_SIZE, SPRITE_SIZE);
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
        dispatcher.dispatch(Event.Type.CLOCK_TICK, event1 -> onTick((ClockTickEvent) event1));
    }

    @Override
    public boolean onTick(ClockTickEvent event) {
        lastX = super.getRenderX();
        lastY = super.getRenderY();
        ticks++;
        if (ticks > RANDOM.nextInt(10) + 60) {
            init();
            ticks = 0;
        }
        return false;
    }
}
