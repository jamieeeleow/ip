package command;

import storage.Storage;

public class Unmark extends ListAction {

    public Unmark(Storage s, String command) {
        int num = extractTaskNum(command);
        s.unmark(num);
    }

}
