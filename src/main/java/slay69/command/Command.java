package slay69.command;

import java.io.IOException;

import slay69.Slay69Exception;
import slay69.storage.Storage;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Represents a user command that can act on the application components.
 */
public abstract class Command {
    /**
     * Executes this command.
     *
     * @throws Slay69Exception if the command cannot be applied to the task list
     * @throws IOException if a changed task list cannot be saved
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws Slay69Exception, IOException;

    /**
     * Returns whether this command should end the application.
     */
    public boolean isExit() {
        return false;
    }
}
