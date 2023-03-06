package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    private int epicId;

    protected static final int EPIC_ID_INDEX = 5;
    protected static final int DURATION_INDEX = 6;
    protected static final int STARTTIME_INDEX = 7;

    public Subtask(String nameSubtask, String descriptionSubtask) {
        super(nameSubtask, descriptionSubtask);
        this.type = Type.SUBTASK;
    }

    public Subtask(String nameTask, String description, String startTime, int durationMinutes) {
        super(nameTask, description, startTime, durationMinutes);
        this.type = Type.SUBTASK;
    }

    public Subtask() {
        super();
        this.type = Type.SUBTASK;
    }

    public Subtask(Task task, int epicId) {
        super(task.name, task.description);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public int getEpicIdOrZero() {
        if (getEpicId() > 0) {
            return epicId;
        } else {
            return 0;
        }
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
        return name + " " + getId() + " " + getStatus() + " " + epicId + " " + startTime;
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

        String str = parts[EPIC_ID_INDEX].replaceAll("\n", "");
        int idIndex = Integer.parseInt(str);
        subtask.setEpicId(idIndex);

        try {
            subtask.setDuration(Duration.parse(parts[DURATION_INDEX]));

            String partSartTime = parts[STARTTIME_INDEX].replaceAll("\n", "");
            startTime = LocalDateTime.parse(partSartTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        } catch (ArrayIndexOutOfBoundsException exc) {
            return subtask;
        }
        return subtask;
    }
}


