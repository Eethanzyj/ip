package slay69.command;

import java.util.List;

import slay69.storage.Storage;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Finds tasks containing a keyword in their descriptions.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a search command for the supplied keyword or phrase.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Integer> matchingNumbers = tasks.find(keyword);
        ui.showMatchingTasks(tasks.getTasks(), matchingNumbers);
    }
}
