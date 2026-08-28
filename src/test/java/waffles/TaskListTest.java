package waffles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the task collection operations provided by {@link TaskList}.
 */
class TaskListTest {
    /** Verifies adding, retrieving, and removing tasks preserves list order. */
    @Test
    void taskOperations_addAndRemove_preserveOrder() {
        TaskList taskList = new TaskList(List.of(new Todo("first")));
        Task second = new Todo("second");

        taskList.addTask(second);

        assertEquals(2, taskList.size());
        assertEquals("first", taskList.getTask(0).getDescription());
        assertEquals(second, taskList.removeTask(1));
        assertEquals(1, taskList.size());
    }

    /** Verifies callers cannot mutate the task list through its storage view. */
    @Test
    void asList_mutationAttempt_throwsUnsupportedOperationException() {
        TaskList taskList = new TaskList(List.of(new Todo("keep")));

        assertThrows(UnsupportedOperationException.class,
                () -> taskList.asList().add(new Todo("should fail")));
        assertEquals(1, taskList.size());
    }

    /** Verifies the no-argument constructor creates an empty list. */
    @Test
    void emptyConstructor_createsEmptyList() {
        assertEquals(0, new TaskList().size());
    }

    /** Verifies find matches descriptions case-insensitively and keeps list order. */
    @Test
    void findTasks_caseInsensitiveKeyword_returnsMatchingTasksInOrder() {
        Task first = new Todo("read book");
        Task second = new Todo("book a room");
        Task third = new Todo("clean desk");
        TaskList taskList = new TaskList(List.of(first, second, third));

        assertEquals(List.of(first, second), taskList.findTasks("BOOK"));
        assertEquals(List.of(), taskList.findTasks("spaceship"));
    }
}
