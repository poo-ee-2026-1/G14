package backend.simulator;

public class Event implements Comparable<Event> {

    private double timestamp;
    private EventType type;

    public Event(double timestamp, EventType type) {
        this.timestamp = timestamp;
        this.type = type;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public EventType getType() {
        return type;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.timestamp, other.timestamp);
    }
}
    

