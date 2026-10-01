package slay69.command;

import slay69.storage.Storage;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Displays the farewell message and signals that the application should exit.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
