/**
 * Represents a task in the Waffles task list.
 */
public class Task {
    /** The text describing the task. */
    protected String description;

    /** Whether the task has been completed. */
    protected boolean isDone;

    /**
     * Creates a new incomplete task.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the task description.
     *
     * @param description the new task description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the status marker used when displaying this task.
     *
     * @return {@code "X"} for a completed task, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the bracketed status marker used by the current list display.
     *
     * @return {@code "[X]"} for a completed task, or {@code "[ ]"} otherwise
     */
    public String isDoneSymbol() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Returns the task in the format used by the task list.
     *
     * @return the status marker followed by the task description
     */
    @Override
    public String toString() {
        return isDoneSymbol() + " " + description;
    }
}
