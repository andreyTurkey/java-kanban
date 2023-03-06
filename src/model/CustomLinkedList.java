package model;

import java.util.*;

public class CustomLinkedList {
    private Map<Integer, Node> nodes = new HashMap<>();

    private int size = 0;

    private Node<Task> head;

    private Node<Task> tail;

    public void linkLast(Task task) {
        final Node<Task> t = tail;
        final Node<Task> newNode = new Node<>(t, task, null);
        tail = newNode;
        if (t == null)
            head = newNode;
        else
            t.setNext(newNode);
        size++;
    }

    public List<Task> historyFromNode() {
        List<Task> forHistory = new ArrayList<>();
        for (Node<Task> x = head; x != null; x = x.getNext()) {
            forHistory.add(x.getData());
        }
        return forHistory;
    }

    public void unlinkFirst() {
        Node<Task> f = head;
        Task element = f.getData();
        Node<Task> next = f.getNext();
        f.setNext(null);
        head = next;
        if (next == null)
            tail = null;
        else
            next.setPrev(null);
        size--;
    }

    public void removeNode(Node<Task> node) {
        if (node != null) {
            for (Node<Task> x = head; x != null; x = x.getNext()) {
                if (node.equals(x)) {
                    unlink(x);
                }
            }
        }
    }

    public Node<Task> getLast() {
        final Node<Task> l = tail;
        return l;
    }

    public void unlink(Node<Task> x) {
        Task element = x.getData();
        Node<Task> next = x.getNext();
        Node<Task> prev = x.getPrev();

        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
            x.setPrev(null);
        }

        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
            x.setNext(null);
        }

        x.setData(null);
        size--;
    }

    public Node getFirst() {
        final Node<Task> f = head;
        return f;
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Integer, Node> nodes) {
        this.nodes = nodes;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Node<Task> getHead() {
        return head;
    }

    public void setHead(Node<Task> head) {
        this.head = head;
    }

    public Node<Task> getTail() {
        return tail;
    }

    public void setTail(Node<Task> tail) {
        this.tail = tail;
    }
}

