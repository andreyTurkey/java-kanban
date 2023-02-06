package model;

public class Task {
    protected static final int ID_INDEX = 0;
    protected static final int NAME_INDEX = 2;
    protected static final int STATUS_INDEX = 3;
    protected static final int DESCRIPTION_INDEX = 4;

    protected int id;
    protected String name;
    protected String description;
    protected Status status;
    protected Type type = Type.TASK;

    public Task(String name, String discription) {
        this.name = name;
        this.description = discription;
        status = Status.NEW;
    }

    public Task() {
        this.name = "";
        this.description = "";
        status = Status.NEW;
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

    public void setDescription(String discription) {
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

        return name + " " + getId() + " " + getStatus();
    }

    public String toString(Task task) {
        return String.format("%d,%S,%s,%S,%s,\n", id, type, name, status, description);
    }

    @Override
    public boolean equals(Object task) {
        if (this == task) return true;
        if (task == null || getClass() != task.getClass()) return false;
        Task otherTask = (Task) task;
        return id == otherTask.getId();
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

        return task;
    }
}




