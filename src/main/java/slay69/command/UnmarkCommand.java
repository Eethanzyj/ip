package slay69.command;

import java.io.IOException;

import slay69.Slay69Exception;
import slay69.storage.Storage;
import slay69.task.Task;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Marks a task as incomplete and persists the updated task list.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws Slay69Exception, IOException {
        Task task = tasks.getTask(taskNumber);
        task.markAsUndone();
        ui.showTaskStatusChanged(task, false);
        storage.save(tasks.getTasks());
    }
}
