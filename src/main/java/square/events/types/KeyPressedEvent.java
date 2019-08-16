package square.events.types;

public class KeyPressedEvent extends KeyEvent {
    public KeyPressedEvent(int keyCode) {
        super(Type.KEY_PRESSED, keyCode);
    }

}
