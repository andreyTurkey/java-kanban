package model;

public class Subtask extends Task {
    private int epicId;
    private Type type = Type.SUBTASK;

    public Subtask(String nameSubtask, String discriptionSubtask) {
        super(nameSubtask, discriptionSubtask);
        this.epicId = epicId;
    }

    public Subtask(Task task, int epicId) {
        super(task.name, task.discription);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return name + " " + getId() + " " + getStatus() + " " + epicId;
    }

    public String toString(Task task) {
        return String.format("%d,%S,%s,%S,%s,%d\n", id, type, name, status, discription, epicId);
    }

    public Subtask fromString(String value) {
        Subtask subtask = new Subtask("", "");
        String[] parts = value.split(",");
        subtask.setId(Integer.parseInt(parts[0]));
        subtask.setType(Type.SUBTASK);
        subtask.setName(parts[2]);
        if (parts[3].equals("NEW")) {
            subtask.setStatus(Status.NEW);
        } else if (parts[3].equals("IN_PROGRESS")) {
            subtask.setStatus(Status.IN_PROGRESS);
        } else {
            subtask.setStatus(Status.DONE);
        }
        subtask.setDiscription(parts[4]);
        subtask.setEpicId(Integer.parseInt(parts[5]));

        return subtask;
    }
}


