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
        return String.format("%d,%S,%s,%S,%s,\n", id, type, name, status, description);
    }

    public Epic fromString(String value) {
        Epic epic = new Epic();
        String[] parts = value.split(",");
        epic.setId(Integer.parseInt(parts[ID_INDEX]));
        epic.setType(Type.EPIC);
        epic.setName(parts[NAME_INDEX]);
        if (Status.valueOf(parts[STATUS_INDEX]) == Status.NEW) {
            epic.setStatus(Status.NEW);
        } else if (Status.valueOf(parts[STATUS_INDEX]) == Status.IN_PROGRESS) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.DONE);
        }
        epic.setDescription(parts[DESCRIPTION_INDEX]);

        return epic;
    }
}

