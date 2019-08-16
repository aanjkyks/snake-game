package square.events;

public abstract class Event {

    private Type type;
    private boolean handled;

    public Event(Type type) {
        this.type = type;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    Type getType() {
        return type;
    }

    public enum Type {
        CLOCK_TICK,
        KEY_PRESSED,
        KEY_RELEASED,
        FOOD_EATEN,
        GAME_OVER,
        NEW_GAME
    }
}
