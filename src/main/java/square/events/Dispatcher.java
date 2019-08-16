package square.events;

public class Dispatcher {

    private Event event;

    public Dispatcher(Event event) {
        this.event = event;
    }

    public void dispatch(Event.Type type, EventHandler handler) {
        if (event.isHandled())
            return;

        if (event.getType() == type)
            event.setHandled(handler.handle(event));
    }
}
