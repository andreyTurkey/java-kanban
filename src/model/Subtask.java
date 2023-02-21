package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    protected static final int EPIC_ID_INDEX = 5;
    protected static final int DURATION_INDEX = 6;
    protected static final int STARTTIME_INDEX = 7;

    private Type type = Type.SUBTASK;

    public Subtask(String nameSubtask, String descriptionSubtask) {
        super(nameSubtask, descriptionSubtask);
    }

    public Subtask(String nameTask, String discription, String startTime, int durationMinutes) {
        super(nameTask, discription, startTime, durationMinutes);
    }

    public Subtask() {
        super();
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

    public void setEpicId(Epic epic) {
        int epicId = epic.getId();
        this.epicId = epicId;
    }

    public LocalDateTime getEndTime() {
        LocalDateTime endTime = startTime.plus(duration);
        return endTime;
    }

    @Override
    public String toString() {
        return name + " " + getId() + " " + getStatus() + " " + epicId +" "+ startTime;
    }

    public String toString(Task task) {
        if (duration != null || startTime != null) {
            return String.format("%d,%S,%s,%S,%s,%d,%s,%s\n", id, type, name, status, description, epicId, duration, startTime.format(formatter));
        }
        return String.format("%d,%S,%s,%S,%s,%d\n", id, type, name, status, description, epicId);
    }

    public Subtask fromString(String value) {
        Subtask subtask = new Subtask();
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
        try {
            subtask.setDuration(Duration.parse(parts[DURATION_INDEX]));
            subtask.setStartTime(LocalDateTime.parse(parts[STARTTIME_INDEX], formatter));
        } catch (ArrayIndexOutOfBoundsException exc) {
            return subtask;
        }
        return subtask;
    }
}


