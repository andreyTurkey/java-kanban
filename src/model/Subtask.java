package model;

public class Subtask extends Task {
    private int epicId;
    protected static final int EPIC_ID_INDEX = 5;
    private Type type = Type.SUBTASK;

    public Subtask(String nameSubtask, String descriptionSubtask) {
        super(nameSubtask, descriptionSubtask);
        this.epicId = epicId;
    }

    public Subtask() {
        super();
        this.epicId = epicId;
    }

    public Subtask(Task task, int epicId) {
        super(task.name, task.description);
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
        return String.format("%d,%S,%s,%S,%s,%d\n", id, type, name, status, description, epicId);
    }

    public Subtask fromString(String value) {
        Subtask subtask = new Subtask("", "");
        String[] parts = value.split(",");
        subtask.setId(Integer.parseInt(parts[ID_INDEX]));
        subtask.setType(Type.SUBTASK);
        subtask.setName(parts[NAME_INDEX]);
        if (Status.valueOf(parts[STATUS_INDEX]) == Status.NEW) {
            subtask.setStatus(Status.NEW);
        } else if (Status.valueOf(parts[STATUS_INDEX]) == Status.IN_PROGRESS) {
            subtask.setStatus(Status.IN_PROGRESS);
        } else {
            subtask.setStatus(Status.DONE);
        }
        subtask.setDescription(parts[DESCRIPTION_INDEX]);
        subtask.setEpicId(Integer.parseInt(parts[EPIC_ID_INDEX]));

        return subtask;
    }
}


