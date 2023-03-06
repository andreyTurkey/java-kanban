package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds;
    private List<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String nameTask, String description) {
        super(nameTask, description);
        this.subtaskIds = new ArrayList<>();
        this.subtasks = new ArrayList<>();
        this.type = Type.EPIC;
    }

    public Epic() {
        super();
        subtaskIds = new ArrayList<>();
        this.subtasks = new ArrayList<>();
        this.type = Type.EPIC;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
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
        String str = endTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
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



