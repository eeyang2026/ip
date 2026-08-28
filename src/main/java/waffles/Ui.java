package waffles;

import java.util.Scanner;

/**
 * Handles all input and output for the Waffles chatbot.
 */
public class Ui {
    /** The divider used to separate chatbot messages. */
    private static final String SEPARATOR = "____________________________________________________________";

    /** Reads commands entered through standard input. */
    private final Scanner scanner;

    /** Creates a user interface connected to standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays the chatbot's welcome message. */
    public void showWelcome() {
        String banner = " __        __    _      _____ _____ _      _____  ____\n"
                + " \\ \\      / /   / \\    |  ___|  ___| |    | ____|/ ___|\n"
                + "  \\ \\/\\ / /   / _ \\   | |_  | |_  | |    |  _|  \\___ \\ \n"
                + "   \\ V  V /   / ___ \\  |  _| |  _| | |___ | |___  ___) |\n"
                + "    \\_/\\_/   /_/   \\_\\ |_|   |_|   |_____||_____||____/";

        showDivider();
        System.out.println(banner);
        System.out.println("Hello! I'm Waffles.");
        System.out.println("What can I do for you?");
        showDivider();
    }

    /**
     * Returns whether another command is available.
     *
     * @return whether standard input has another line
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims one command.
     *
     * @return the next command
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Displays the standard divider. */
    public void showDivider() {
        System.out.println(SEPARATOR);
    }

    /** Displays the goodbye message. */
    public void showGoodbye() {
        System.out.println("Until next time, Waffleeeeeeeees out");
    }

    /**
     * Displays all tasks in their current order.
     *
     * @param tasks the task list to display
     */
    public void showTaskList(TaskList tasks) {
        showDivider();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.getTask(i));
        }
        showDivider();
    }

    /**
     * Displays an error message.
     *
     * @param message the problem and correction hint
     */
    public void showError(String message) {
        showDivider();
        System.out.println("Oops, " + message);
        showDivider();
    }

    /**
     * Displays confirmation after adding a task.
     *
     * @param task the newly added task
     * @param taskCount the updated task count
     */
    public void showTaskAdded(Task task, int taskCount) {
        showDivider();
        String taskType = task instanceof Event
                ? "event"
                : task instanceof Deadline ? "deadline" : "task";
        System.out.println("Got it. I've added this " + taskType + ":");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showDivider();
    }

    /**
     * Displays confirmation after changing a task's completion status.
     *
     * @param task the updated task
     * @param isDone whether the task was marked done
     */
    public void showMarkResult(Task task, boolean isDone) {
        showDivider();
        if (isDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
        showDivider();
    }

    /**
     * Displays confirmation after deleting a task.
     *
     * @param task the deleted task
     * @param taskCount the remaining task count
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showDivider();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showDivider();
    }
}
