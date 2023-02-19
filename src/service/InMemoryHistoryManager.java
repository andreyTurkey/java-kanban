package service;

import model.CustomLinkedList;
import model.Task;
import model.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList linkedHistory = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (!linkedHistory.getNodes().containsKey(task.getId())) {

            linkedHistory.linkLast(task);

            linkedHistory.getNodes().put(task.getId(), linkedHistory.getLast());
        }

        Node<Task> node = linkedHistory.getNodes().get(task.getId());

        linkedHistory.removeNode(node);

        linkedHistory.linkLast(task);

        linkedHistory.getNodes().put(task.getId(), linkedHistory.getLast());
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyFromNodeList = new ArrayList<>();
        List<Task> history = linkedHistory.historyFromNode();
        for (Task task : history) {

            historyFromNodeList.add(task);

        }
        return historyFromNodeList;
    }

    @Override
    public HashMap<Integer, Node> fromMap() { // Вспомогательный метод
        HashMap<Integer, Node> forPrint = new HashMap<>();
        for (Integer id : linkedHistory.getNodes().keySet()) {

            Node node = linkedHistory.getNodes().get(id);

            forPrint.put(id, node);

        }
        return forPrint;
    }

    @Override
    public void remove(int id) {
        Node<Task> node = linkedHistory.getNodes().get(id);

        linkedHistory.getNodes().remove(id);

        linkedHistory.removeNode(node);

        linkedHistory.getNodes().put(id, linkedHistory.getFirst());

        linkedHistory.getNodes().put(id, linkedHistory.getLast());
    }
}

