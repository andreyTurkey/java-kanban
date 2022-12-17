package tasks;

public class Subtask extends Task{
    protected int epicId;

    public Subtask(String nameSubtask, String discriptionSubtask) {
        super(nameSubtask, discriptionSubtask);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return name +" "  + getId() + " "+ getStatus() + " "+ epicId;
    }
}
