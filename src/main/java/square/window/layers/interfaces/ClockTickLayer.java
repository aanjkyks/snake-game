package square.window.layers.interfaces;

import square.events.types.ClockTickEvent;

public interface ClockTickLayer extends Layer {
    boolean onTick(ClockTickEvent e);
}
