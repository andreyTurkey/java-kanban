package service;

import model.Task;
import model.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final int MAX_HISTORY_SIZE = 10;
    private CustomLinkedList<Task> linkedHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        if (linkedHistory.size == MAX_HISTORY_SIZE) {

            linkedHistory.unlinkFirst();

            linkedHistory.historyFromCastomLinkedList.put(task.getId(), linkedHistory.getFirst());

            linkedHistory.linkLast(task);

            linkedHistory.historyFromCastomLinkedList.put(task.getId(), linkedHistory.getLast());
        }
        if (!linkedHistory.historyFromCastomLinkedList.containsKey(task.getId())) {

            linkedHistory.linkLast(task);

            linkedHistory.historyFromCastomLinkedList.put(task.getId(), linkedHistory.getLast());
        }

        Node<Task> node = linkedHistory.historyFromCastomLinkedList.get(task.getId());

        linkedHistory.removeNode(node);

        linkedHistory.linkLast(task);

        linkedHistory.historyFromCastomLinkedList.put(task.getId(), linkedHistory.getLast());
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
    public HashMap<Integer, Node> fromMap() {
        HashMap<Integer, Node> forPrint = new HashMap<>();
        for (Integer id : linkedHistory.historyFromCastomLinkedList.keySet()) {

            Node node = linkedHistory.historyFromCastomLinkedList.get(id);

            forPrint.put(id, node);

        }
        return forPrint;
    }

    @Override
    public void remove(int id) {
        Node<Task> node = linkedHistory.historyFromCastomLinkedList.get(id);

        linkedHistory.historyFromCastomLinkedList.remove(id);

        linkedHistory.removeNode(node);

        linkedHistory.historyFromCastomLinkedList.put(id, linkedHistory.getFirst());

        linkedHistory.historyFromCastomLinkedList.put(id, linkedHistory.getLast());
    }

    private class CustomLinkedList<Task> {

        private HashMap<Integer, Node> historyFromCastomLinkedList = new HashMap<>();

        private int size = 0;

        private Node<Task> head;

        private Node<Task> tail;

        void linkLast(Task task) {
            final Node<Task> t = tail;
            final Node<Task> newNode = new Node<>(t, task, null);
            tail = newNode;
            if (t == null)
                head = newNode;
            else
                t.next = newNode;
            size++;
        }

        public List<Task> historyFromNode() {
            List<Task> forHistory = new ArrayList<>();
            for (Node<Task> x = head; x != null; x = x.next) {
                forHistory.add(x.data);
            }
            return forHistory;
        }

        void unlinkFirst() {
            Node<Task> f = head;
            Task element = f.data;
            Node<Task> next = f.next;
            //f.data = null;
            f.next = null;
            head = next;
            if (next == null)
                tail = null;
            else
                next.prev = null;
            size--;
        }

        public void removeNode(Node<Task> node) {
            for (Node<Task> x = head; x != null; x = x.next) {
                if (node.equals(x)) {
                    unlink(x);
                }
            }
        }

        public Node<Task> getLast() {
            final Node<Task> l = tail;
            if (l == null)
                throw new NoSuchElementException();
            return l;
        }

        public void unlink(Node<Task> x) {

            Task element = x.data;
            Node<Task> next = x.next;
            Node<Task> prev = x.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            x.data = null;
            size--;
        }

        public Node getFirst() {
            final Node<Task> f = head;
            return f;
        }
    }
}

