package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Task {
    protected static final int ENDTIME_INDEX = 7;

    private List<Integer> subtaskIds;
    private List<Subtask> subtasks;
    private Type type = Type.EPIC;
    private LocalDateTime endTime;

    public Epic(String nameTask, String discription) {
        super(nameTask, discription);
        this.subtaskIds = new ArrayList<>();
        this.subtasks = new ArrayList<>();
    }

    public Epic() {
        super();
        subtaskIds = new ArrayList<>();
        this.subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtasks(Subtask subtask) {
        subtasks.add(subtask);
        calculateEpicTime();
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        calculateEpicTime();
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getEndTimeInString() {
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
            startTime = LocalDateTime.parse(parts[STARTTIME_INDEX], formatter);
            endTime = LocalDateTime.parse(parts[ENDTIME_INDEX], formatter);
        } catch (ArrayIndexOutOfBoundsException exc) {
            return epic;
        }
        return epic;
    }

    public void calculateEpicTime() {
        Duration durationSubtasks = Duration.ofMinutes(0);
        List<Subtask> timingTasks = new ArrayList<>();
        List<Subtask> subtasks = getSubtasks();
        for (Subtask subtask : subtasks) {
            Duration durationSubtask = subtask.getDuration();
            if (durationSubtask != null) {
                durationSubtasks = durationSubtasks.plus(durationSubtask);
                timingTasks.add(subtask);
                subtask.getStartTime().toInstant(ZoneOffset.UTC);
            }
        }
        Collections.sort(timingTasks, (t1, t2) -> (int) (t1.getStartTime().toInstant(ZoneOffset.UTC).toEpochMilli()
                - t2.getStartTime().toInstant(ZoneOffset.UTC).toEpochMilli()));
        if (timingTasks.size() != 0) {
            Subtask sortedStartSubtask = timingTasks.get(0);

            LocalDateTime startTimeSubtask = sortedStartSubtask.getStartTime();
            startTime = startTimeSubtask;

            Subtask sortedEndSubtask = timingTasks.get(timingTasks.size() - 1);
            LocalDateTime endTimeSubtask = sortedEndSubtask.getStartTime().plus(sortedEndSubtask.getDuration());

            endTime = endTimeSubtask;

            Duration totalDurationEpic = Duration.between(startTimeSubtask, endTimeSubtask);

            duration = totalDurationEpic;
        }
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
        if (status == Status.NEW) {
            startTime = null;
            duration = null;
            endTime = null;
        }
    }
}



