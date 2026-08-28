/**
 * Represents a task that occurs between a start and end date or time.
 */
public class Event extends Task {
    /** The date or time when the event starts. */
    protected String from;

    /** The date or time when the event ends. */
    protected String to;

    /**
     * Creates a new event task.
     *
     * @param description the text describing the event
     * @param from the event's start date or time
     * @param to the event's end date or time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's starting date or time.
     *
     * @return the event start value
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event's ending date or time.
     *
     * @return the event end value
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the event in the format used by the task list.
     *
     * @return the event type marker, status, description, and time range
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " | to: " + to + ")";
    }
}
