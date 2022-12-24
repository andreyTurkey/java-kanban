package model;

public class Task {
    protected int id;
    protected String name;
    protected String discription;
    protected Status status;

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

    @Override
    public String toString() {
        return name + " " + getId() + " " + getStatus();
    }
}




