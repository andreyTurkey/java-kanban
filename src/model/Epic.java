package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    private Type type = Type.EPIC;

    public Epic(String nameTask, String discription) {
        super(nameTask, discription);
        subtaskIds = new ArrayList<>();
    }

    public Epic() {
        super();
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskInList(int subtasksId) {

        subtaskIds.add(subtasksId);
    }

    public String toString(Task task) {
        return String.format("%d,%S,%s,%S,%s,\n", id, type, name, status, discription);
    }

    public Epic fromString(String value) {
        Epic epic = new Epic("", "");
        String[] parts = value.split(",");
        epic.setId(Integer.parseInt(parts[0]));
        epic.setType(Type.EPIC);
        epic.setName(parts[2]);
        if (parts[3].equals("NEW")) {
            epic.setStatus(Status.NEW);
        } else if (parts[3].equals("IN_PROGRESS")) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.DONE);
        }
        epic.setDiscription(parts[4]);

        return epic;
    }
}

