package tasks;

public class Task {
    public String name;
    public String discription;
    protected int id=0;
    protected String[] statuses = {"NEW", "IN_PROGRESS", "DONE"};
    protected String status;

    public Task(String nameTask, String discription) {
        name = nameTask;
        this.discription = discription;
        status = statuses[0];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getStatuses() {
        return statuses;
    }

    public void setStatuses(String[] statuses) {
        this.statuses = statuses;
    }

    @Override
    public String toString() {
        return name +" "  + getId() + " "+ getStatus();
    }
}

