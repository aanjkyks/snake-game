package square.events.types;

import square.events.Event;

public class NewGameEvent extends Event {
    public NewGameEvent() {
        super(Type.NEW_GAME);
    }
}
