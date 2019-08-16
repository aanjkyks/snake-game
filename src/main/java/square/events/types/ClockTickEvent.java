package square.events.types;

import square.events.Event;

public class ClockTickEvent extends Event {
    public ClockTickEvent() {
        super(Type.CLOCK_TICK);
    }
}
