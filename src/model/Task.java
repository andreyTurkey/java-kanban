package model;

public class Task {
    protected int id;
    public String name;
    public String discription;

    protected Statuses statuses;


    public Task(String nameTask, String discription) {
        name = nameTask;
        this.discription = discription;
        statuses = Statuses.NEW;


    }

    public void setStatuses(Statuses statuses) {
        this.statuses = statuses;
    }

    public Statuses getStatuses() {
        return statuses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return name + " " + getId() + " " + getStatuses();
    }
}




