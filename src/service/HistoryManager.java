package service;

import model.Node;
import model.Task;

import java.util.HashMap;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);

    public HashMap<Integer, Node> fromMap();
}

