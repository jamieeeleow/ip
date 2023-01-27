package leo.storage;

import leo.leoException.IncorrectMarkException;
import leo.leoException.IncorrectUnmarkException;
import leo.ui.Ui;

/**
 * Represents a Task. A <code>Task</code> object contains the String description.
 */
public class Task {

    private final String task;
    private boolean done;
    private TaskType type;

    public Task(String task) {
        this.task = task;
        this.done = false;
        this.type = TaskType.TODO;
    }

    /**
     * Returns the description of Task.
     *
     * @return Description of Task.
     */
    public String getTask() {
        return this.task;
    }

    /**
     * Returns completion status of Task.
     *
     * @return Status of Task.
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * Marks Task that is done.
     *
     * @throws IncorrectMarkException If task is marked.
     */
    public void mark() throws IncorrectMarkException {
        if (this.done) {
            throw new IncorrectMarkException("This task was already marked previously.");
        }
        this.done = true;
    }

    /**
     * Unmarks Task that is not done.
     *
     * @throws IncorrectUnmarkException If task is not marked.
     */
    public void unmark() throws IncorrectUnmarkException {
        if (!this.done) {
            throw new IncorrectUnmarkException("This task has not been marked.");
        }
        this.done = false;
    }

    /**
     * Returns the String representation of Task that is to be saved in the data file.
     *
     * @return String representation of Task.
     */
    public String saveFormat() {
        return task;
    }

    /**
     * Returns the TaskType of Task.
     *
     * @return TaskType of Task.
     */
    public TaskType getType() {
        return this.type;
    }

    /**
     * Set the TaskType of Task.
     *
     * @param tt TaskType to be set to the Task.
     */
    public void setType(TaskType tt) {
        this.type = tt;
    }

    /**
     * Returns the TaskType and status of Task in String representation.
     *
     * @return String representation of TaskType and status.
     */
    public String typeAndStatus() {
        return Ui.type(this) + Ui.completion(this);
    }

}
