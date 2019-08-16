package square.events.types;

public class KeyReleasedEvent extends KeyEvent {
    public KeyReleasedEvent(int keyCode) {
        super(Type.KEY_RELEASED, keyCode);
    }
}
