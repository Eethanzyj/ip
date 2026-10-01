package slay69.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import slay69.Slay69Exception;

/**
 * Owns the user's tasks and provides operations for accessing and changing them.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     * A defensive copy prevents callers from changing the list directly.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task identified by its one-based number.
     *
     * @throws Slay69Exception if the task number is outside the list
     */
    public Task getTask(int taskNumber) throws Slay69Exception {
        return tasks.get(toIndex(taskNumber));
    }

    /**
     * Removes and returns the task identified by its one-based number.
     *
     * @throws Slay69Exception if the task number is outside the list
     */
    public Task deleteTask(int taskNumber) throws Slay69Exception {
        return tasks.remove(toIndex(taskNumber));
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view for displaying or saving the tasks.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns the original one-based numbers of tasks whose descriptions contain
     * the keyword, ignoring letter case.
     */
    public List<Integer> find(String keyword) {
        String searchText = keyword.toLowerCase(Locale.ROOT);
        List<Integer> matchingNumbers = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getDescription().toLowerCase(Locale.ROOT)
                    .contains(searchText)) {
                matchingNumbers.add(i + 1);
            }
        }

        return matchingNumbers;
    }

    /**
     * Converts a one-based task number to an internal list index.
     */
    private int toIndex(int taskNumber) throws Slay69Exception {
        int index = taskNumber - 1;
        if (index < 0 || index >= tasks.size()) {
            throw new Slay69Exception(
                    "Task " + taskNumber + " does not exist. "
                            + "You currently have " + tasks.size() + " tasks.");
        }
        return index;
    }
}
