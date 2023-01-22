package model;

import java.util.Objects;

public class Node<Task> {
    private Task data;
    private Node<Task> next;
    private Node<Task> prev;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public Task getData() {
        return data;
    }

    public void setData(Task data) {
        this.data = data;
    }

    public Node<Task> getNext() {
        return next;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    public Node<Task> getPrev() {
        return prev;
    }

    public void setPrev(Node<Task> prev) {
        this.prev = prev;
    }

    @Override
    public boolean equals(Object node) {
        if (this == node) return true;
        if (node == null || getClass() != node.getClass()) return false;
        Node otherNode = (Node) node;
        return Objects.equals(data, otherNode.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}

