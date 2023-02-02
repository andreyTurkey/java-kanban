package model;

public class Task {
    protected int id;
    protected String name;
    protected String discription;
    protected Status status;
    protected Type type = Type.TASK;

    public Task(String nameTask, String discription) {
        name = nameTask;
        this.discription = discription;
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
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
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
        return String.format("%d,%S,%s,%S,%s,\n", id, type, name, status, discription);
    }

    @Override
    public boolean equals(Object task) {
        if (this == task) return true;
        if (task == null || getClass() != task.getClass()) return false;
        Task otherTask = (Task) task;
        return id == otherTask.getId();
    }

    public Task fromString(String value) {
        Task task = new Task("", "");
        String[] parts = value.split(",");
        task.setId(Integer.parseInt(parts[0]));
        task.setType(Type.TASK);
        task.setName(parts[2]);
        if (parts[3].equals("NEW")) {
            task.setStatus(Status.NEW);
        } else if (parts[3].equals("IN_PROGRESS")) {
            task.setStatus(Status.IN_PROGRESS);
        } else {
            task.setStatus(Status.DONE);
        }
        task.setDiscription(parts[4]);

        return task;
    }
}




