package slay69.command;

import java.io.IOException;

import slay69.Slay69Exception;
import slay69.storage.Storage;
import slay69.task.Task;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Marks a task as complete and persists the updated task list.
 */
public class MarkCommand extends Command {
    private final int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws Slay69Exception, IOException {
        Task task = tasks.getTask(taskNumber);
        task.markAsDone();
        ui.showTaskStatusChanged(task, true);
        storage.save(tasks.getTasks());
    }
}
