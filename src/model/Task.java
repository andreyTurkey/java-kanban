package model;

import javax.swing.text.DateFormatter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected static final int ID_INDEX = 0;
    protected static final int NAME_INDEX = 2;
    protected static final int STATUS_INDEX = 3;
    protected static final int DESCRIPTION_INDEX = 4;
    protected static final int DURATION_INDEX = 5;
    protected static final int STARTTIME_INDEX = 6;
    protected int id;
    protected String name;
    protected String description;
    protected Status status;
    protected Type type = Type.TASK;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String discription) {
        this.name = name;
        this.description = discription;
        this.status = Status.NEW;
    }

    public Task() {
        this.name = "";
        this.description = "";
        this.status = Status.NEW;
    }

    public Task(String name, String discription, String startTime, int durationMinutes) {
        this.name = name;
        this.description = discription;
        this.status = Status.NEW;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(durationMinutes);
    }

    public LocalDateTime getEndTime() {
        LocalDateTime endTime = startTime.plus(duration);
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        return duration;
    }

    protected void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getStartTimeInString() {
        String str = startTime.format(formatter);
        return str;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setStart(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (startTime != null) {
            return name + " " + getId() + " " + getStatus() + startTime.format(formatter) + " " + duration;
        } else {
            return name + " " + getId() + " " + getStatus();
        }
    }

    public String toString(Task task) {
        if (duration != null) {
            return String.format("%d,%S,%s,%S,%s,%s,%s\n", id, type, name, status, description, duration, startTime.format(formatter));
        }
        return String.format("%d,%S,%s,%S,%s\n", id, type, name, status, description);
    }

    @Override
    public boolean equals(Object task) {
        if (this == task) return true;
        if (task == null || getClass() != task.getClass()) return false;
        Task otherTask = (Task) task;
        return id == otherTask.getId();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    public Task fromString(String value) {
        Task task = new Task();
        String[] parts = value.split(",");
        task.setId(Integer.parseInt(parts[ID_INDEX]));
        task.setType(Type.TASK);
        task.setName(parts[NAME_INDEX]);
        if (Status.valueOf(parts[STATUS_INDEX]) == Status.NEW) {
            task.setStatus(Status.NEW);
        } else if (Status.valueOf(parts[STATUS_INDEX]) == Status.IN_PROGRESS) {
            task.setStatus(Status.IN_PROGRESS);
        } else {
            task.setStatus(Status.DONE);
        }
        task.setDescription(parts[DESCRIPTION_INDEX]);
        if (duration != null) {
            task.setDuration(Duration.parse(parts[DURATION_INDEX]));
        }
        if (startTime != null) {
            startTime = LocalDateTime.parse(parts[STARTTIME_INDEX], formatter);
        }
        return task;
    }
}




