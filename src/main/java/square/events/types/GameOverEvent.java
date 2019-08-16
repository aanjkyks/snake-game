package square.events.types;

import square.events.Event;

public class GameOverEvent extends Event {

    public GameOverEvent() {
        super(Type.GAME_OVER);
    }
}
