package slay69.task;

/**
 * A task that should be completed by the given deadline text.
 */
public class Deadline extends Task {
    /** Deadline text displayed to the user and written to storage. */
    protected String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description text describing the task
     * @param by deadline text supplied by the user
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the displayed task text including its deadline.
     *
     * @return the displayed deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /**
     * Returns the saved task fields including the deadline text.
     *
     * @return the serialized deadline
     */
    @Override
    public String toStorageString() {
        return "D|" + super.toStorageString() + "|" + by;
    }
}
