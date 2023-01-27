package leo.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import leo.leoException.IncorrectMarkException;
import leo.leoException.LeoException;
import leo.leoException.NoTaskFoundException;
import leo.leoException.NoStorageFileException;
import leo.ui.Ui;

/**
 * Represents a storage place containing a list Tasks. A <code>Storage</code> object corresponds to
 * a database consisting of the list of Tasks, the File in which the data is stored in and the file path.
 */
public class Storage {

    private final TaskList data;
    private final String dataFilePath;
    private final File taskFile;

    public Storage(String filePath) throws NoStorageFileException, IncorrectMarkException {
        String root = Paths.get("").toAbsolutePath().toString();
        this.dataFilePath = Paths.get(root, filePath).toString();

        this.taskFile = new File(this.dataFilePath);
        this.data = new TaskList(loadData());
    }

    /**
     * Returns a list of tasks stored in the data file.
     *
     * @return List of tasks.
     * @throws IncorrectMarkException If a completed task is set to be completed.
     * @throws NoStorageFileException If file is not found.
     */
    private List<Task> loadData() throws IncorrectMarkException, NoStorageFileException {
        List<Task> taskList = new ArrayList<>();
        if (!taskFile.exists()) {
            return taskList;
        }

        try {
            Scanner scanner = new Scanner(taskFile);
            while (scanner.hasNextLine()) {
                String task = scanner.nextLine().strip();
                char taskType = task.charAt(1);
                char completion = task.charAt(4);
                String description = task.substring(7).strip();

                try {
                    if (taskType == 'T') {
                        Task t = new ToDo(description);
                        if (completion == 'X')
                            t.mark();
                        taskList.add(t);
                    } else {
                        String[] temp = description.split("\\|");
                        String taskDescription = temp[0].strip();
                        String time = temp[1].strip();
                        LocalDateTime dt = convertString(time);
                        if (taskType == 'D') {
                            Task t = new Deadline(taskDescription, dt);
                            if (completion == 'X')
                                t.mark();
                            taskList.add(t);
                        } else if (taskType == 'E') {
                            String to = temp[2].strip();
                            LocalDateTime dtTo = convertString(to);
                            Task t = new Event(taskDescription, dt, dtTo);
                            if (completion == 'X')
                                t.mark();
                            taskList.add(t);
                        }
                    }
                } catch (LeoException e) {
                    throw new IncorrectMarkException("This task was already marked previously.");
                }
            }
            return taskList;
        } catch (FileNotFoundException e) {
            throw new NoStorageFileException("No file found!! >:-(");
        }
    }

    /**
     * Adds task into the list of tasks.
     * Prints task upon successful addition.
     *
     * @param task Task to be added into the list.
     */
    public void addTask(Task task) {
        data.addTask(task);
        Ui.displayMessage(Ui.leoResponse("added " + task.getTask() + " to your tasks :-) !"));
    }

    /**
     * Prints the list of tasks stored in the data.
     *
     * @throws NoTaskFoundException If there are no tasks in the list.
     */
    public void showList() throws NoTaskFoundException {
        try {
            int length = data.size();
            for (int i = 0; i < length; i++) {
                Ui.displayMessage((i + 1) + ". " + getTask(i).toString());
            }
        } catch (LeoException e) {
            Ui.displayMessage(Ui.leoResponse(e.getMessage()));
        }
    }

    /**
     * Marks a task in the list.
     * Prints error message if task is marked or not in the list.
     *
     * @param num Index of task to be marked.
     */
    public void mark(int num) {
        try {
            getTask(num - 1).mark();
            Ui.displayMessage(Ui.leoResponse("Good work! You have completed task " + num + ":"));
            Ui.displayMessage(Ui.notFirstLine(getTask(num - 1).toString()));
        } catch (LeoException e) {
            Ui.displayMessage(Ui.leoResponse(e.getMessage()));
        }
    }

    /**
     * Unmarks a task in the list.
     * Prints error message if task is not marked or not in the list.
     *
     * @param num Index of task to be unmarked.
     */
    public void unmark(int num) {
        try {
            getTask(num - 1).unmark();
            Ui.displayMessage(Ui.leoResponse("No worries! I have unmarked task " + num + ":"));
            Ui.displayMessage(Ui.notFirstLine(getTask(num - 1).toString()));
        } catch (LeoException e) {
            Ui.displayMessage(Ui.leoResponse(e.getMessage()));
        }
    }

    /**
     * Deletes a task in the list.
     * Prints error message if task is not in the list.
     *
     * @param num Index of task to be deleted.
     */
    public void delete(int num) {
        Task removed;
        try {
            removed = getTask(num -1);
            data.removeTask(num - 1);
            Ui.displayMessage(Ui.leoResponse("I have removed task " + num + ":"));
            assert removed != null;
            Ui.displayMessage(Ui.notFirstLine(removed.toString()));
        } catch (LeoException e) {
            Ui.displayMessage(Ui.leoResponse(e.getMessage()));
        }
    }

    /**
     * Returns Task in the list at the index stated.
     *
     * @param num Index of task required.
     * @return Task required.
     * @throws NoTaskFoundException If task is not in the list.
     */
    private Task getTask(int num) throws NoTaskFoundException {
        try {
            return data.getTask(num);
        } catch (Exception e) {
            throw new NoTaskFoundException("Hm, this task does not exist...");
        }
    }

    /**
     * Returns the path of the data file.
     *
     * @return String representation of path.
     */
    public String getDataFilePath() {
        return this.dataFilePath;
    }

    /**
     * Returns the number of tasks in the data list.
     *
     * @return Integer presenting number of tasks in the list.
     */
    public int getDataLength() {
        return data.size();
    }

    /**
     * Writes the tasks in the list into the data file in hard disk.
     *
     * @throws NoStorageFileException If file does not exist.
     */
    public void writeToFile() throws NoStorageFileException {
        try {
            int len = getDataLength();
            FileWriter fileWriter = new FileWriter(getDataFilePath());

            for (int i = 0; i < len; i++) {
                fileWriter.write(data.getTask(i).saveFormat());
            }

            fileWriter.close();
        } catch (IOException e) {
            throw new NoStorageFileException("No file found!! >:-(");
        }
    }

    /**
     * Converts String object to LocalDateTime.
     *
     * @param str String representation of date and time.
     * @return LocalDateTime object.
     */
    private LocalDateTime convertString(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm");
        return LocalDateTime.parse(str, formatter);
    }

}
