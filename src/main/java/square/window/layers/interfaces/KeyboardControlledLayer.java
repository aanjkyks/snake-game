package square.window.layers.interfaces;


import square.events.types.KeyPressedEvent;
import square.events.types.KeyReleasedEvent;

public interface KeyboardControlledLayer extends Layer {
    boolean onKeyReleased(KeyReleasedEvent event);

    boolean onKeyPressed(KeyPressedEvent event);
}
