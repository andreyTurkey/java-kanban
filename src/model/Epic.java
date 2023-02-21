package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    protected static final int ENDTIME_INDEX = 7;

    private List<Integer> subtaskIds;
    private Type type = Type.EPIC;
    private LocalDateTime endTime;

    public Epic(String nameTask, String discription) {
        super(nameTask, discription);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic() {
        super();
        subtaskIds = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeInString(){
        String str = endTime.format(formatter);
        return str;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskInList(int subtasksId) {
        subtaskIds.add(subtasksId);
    }

    public String toString(Task task) {
        if (duration != null || startTime != null || endTime != null) {
            return String.format("%d,%S,%s,%S,%s,%s,%s,%s\n", id, type, name, status, description, duration, startTime.format(formatter), endTime.format(formatter));
        }
        return String.format("%d,%S,%s,%S,%s\n", id, type, name, status, description);
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
        try {
            epic.setDuration(Duration.parse(parts[DURATION_INDEX]));
            epic.setStartTime(LocalDateTime.parse(parts[STARTTIME_INDEX], formatter));
            epic.setEndTime(LocalDateTime.parse(parts[ENDTIME_INDEX], formatter));
        } catch (ArrayIndexOutOfBoundsException exc) {
            return epic;
        }
        return epic;
    }
}



