package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> idSubtasks;

    public Epic(String nameTask, String discription) {
        super(nameTask, discription);
        idSubtasks = new ArrayList<>();
    }

    public void addSubtaskInList(int idSub) {
        idSubtasks.add(idSub);
    }
}

