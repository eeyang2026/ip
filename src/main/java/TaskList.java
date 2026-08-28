import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns the ordered collection of tasks and its basic list operations.
 */
public class TaskList {
    /** The tasks in the order in which they should be displayed. */
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list containing a copy of the supplied tasks.
     *
     * @param initialTasks tasks loaded from storage
     */
    public TaskList(List<Task> initialTasks) {
        tasks = new ArrayList<>(initialTasks);
    }

    /** Creates an empty task list. */
    public TaskList() {
        this(Collections.emptyList());
    }

    /**
     * Adds a task to the end of this list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index the zero-based index
     * @return the task at that index
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index the zero-based index
     * @return the removed task
     */
    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view for storage and display.
     *
     * @return the current tasks
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
