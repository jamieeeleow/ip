package command;

import leoException.EmptyDeadlineException;
import leoException.EmptyDescriptionException;
import leoException.IncompleteDurationException;
import leoException.InvalidInputException;
import leoException.LeoException;
import storage.Deadline;
import storage.Event;
import storage.Storage;
import storage.Task;
import storage.ToDo;
import ui.Ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Add extends Command {

    public Add(Storage s, String task) throws LeoException {
        super(s, task);
        try {
            if (task.contains("todo")) {
                s.addTask(new ToDo(task.substring(5)));
            } else if (task.contains("deadline")) {
                try {
                    String t = task.substring(9);
                    String[] taskAndDeadline = t.split("/");
                    String deadlineTask = taskAndDeadline[0].trim();
                    String time = taskAndDeadline[1];
                    LocalDateTime dateTime = convertString(time);
                    Task deadline = new Deadline(deadlineTask, dateTime);
                    s.addTask(deadline);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new EmptyDeadlineException("Uh oh! There is no deadline indicated :-(");
                }
            } else if (task.contains("event")) {
                try {
                    String t = task.substring(6);
                    String[] eventAndDuration = t.split("/");
                    String eventTask = eventAndDuration[0].trim();
                    String from = eventAndDuration[1].trim();
                    String to = eventAndDuration[2].trim();
                    LocalDateTime dateFrom = convertString(from);
                    LocalDateTime dateTo = convertString(to);
                    Task event = new Event(eventTask, dateFrom, dateTo);
                    s.addTask(event);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IncompleteDurationException("Uh oh! The duration of the event is incomplete :-(");
                }
            } else if (task.contains("hello") || task.contains("hi") || task.contains("hey")) {
                Ui.displayMessage(Ui.leoResponse("Well hello to you too! :-D"));
            } else {
                throw new InvalidInputException("Ohno! I do not know what you mean...");
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new EmptyDescriptionException("Uh oh! Description of task is empty :-(");
        }
    }

    private LocalDateTime convertString(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm");
        return LocalDateTime.parse(str, formatter);
    }
}
