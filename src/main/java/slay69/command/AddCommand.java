package slay69.command;

import java.io.IOException;

import slay69.storage.Storage;
import slay69.task.Task;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Adds a task and persists the updated task list.
 */
public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws IOException {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        storage.save(tasks.getTasks());
    }
}
