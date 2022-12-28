package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int MAX_HISTORY_SIZE = 10;

    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == MAX_HISTORY_SIZE) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
