package slay69.command;

import java.io.IOException;

import slay69.Slay69Exception;
import slay69.storage.Storage;
import slay69.task.Task;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Removes a task and persists the updated task list.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws Slay69Exception, IOException {
        Task removedTask = tasks.deleteTask(taskNumber);
        ui.showTaskDeleted(removedTask, tasks.size());
        storage.save(tasks.getTasks());
    }
}
