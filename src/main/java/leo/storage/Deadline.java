package leo.storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline Task. A <code>Deadline</code> object corresponds to
 * a Task containing the String description and LocalDateTime deadline.
 */
public class Deadline extends Task {

    private final LocalDateTime deadline;

    public Deadline(String task, LocalDateTime by) {
        super(task);
        this.deadline = by;
        setType(TaskType.DEADLINE);
    }

    /**
     * Returns type, status, description and deadline of Task.
     *
     * @return String representation of Deadline.
     */
    @Override
    public String toString() {
        return typeAndStatus() + getTask() + " (by: " + getDeadline() + ")";
    }

    /**
     * Returns the String representation of Task that is to be saved in the data file.
     *
     * @return String representation of Deadline.
     */
    @Override
    public String saveFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm");
        String strDeadline = formatter.format(this.deadline);
        return typeAndStatus() + getTask() + " | " + strDeadline + "\n";
    }

    /**
     * Returns formatted deadline obtained from LocalDateTime.
     *
     * @return String representation of the deadline.
     */
    private String getDeadline() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, hh:mm a");
        return formatter.format(this.deadline);
    }
}
