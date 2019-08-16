package square.events.types;

import square.events.Event;

public abstract class KeyEvent extends Event {
    private int keyCode;

    KeyEvent(Type type, int keyCode) {
        super(type);
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

}
