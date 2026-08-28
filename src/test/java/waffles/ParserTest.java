package waffles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests command recognition and task creation performed by {@link Parser}.
 */
class ParserTest {
    /** Parser under test. */
    private final Parser parser = new Parser();

    /** Verifies that a valid deadline command creates a typed date task. */
    @Test
    void parseTask_validDeadline_createsFormattedDeadline() {
        Deadline deadline = (Deadline) parser.parseTask("deadline report /by 2019-10-15");

        assertEquals(LocalDate.of(2019, 10, 15), deadline.getBy());
        assertEquals("[D][ ] report (by: Oct 15 2019)", deadline.toString());
    }

    /** Verifies that a valid event command creates an event with both dates. */
    @Test
    void parseTask_validEvent_preservesBothDates() {
        Event event = (Event) parser.parseTask(
                "event meeting /from 2019-10-15 /to 2019-10-16");

        assertEquals(LocalDate.of(2019, 10, 15), event.getFrom());
        assertEquals(LocalDate.of(2019, 10, 16), event.getTo());
    }

    /** Verifies that an impossible calendar date is rejected without creating a task. */
    @Test
    void parseTask_invalidDate_throwsHelpfulError() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parser.parseTask("deadline report /by 2019-02-30"));

        assertEquals("a deadline date must use yyyy-MM-dd, like 2019-10-15.",
                exception.getMessage());
    }

    /** Verifies that task numbers are parsed and malformed numbers are rejected. */
    @Test
    void parseTaskNumber_validAndInvalidInputs_behaveAsExpected() {
        assertEquals(3, parser.parseTaskNumber("delete 3"));
        assertThrows(IllegalArgumentException.class, () -> parser.parseTaskNumber("mark nope"));
    }
}
