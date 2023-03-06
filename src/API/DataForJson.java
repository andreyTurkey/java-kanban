package API;

import java.util.List;

public class DataForJson {
    private String[] tasks;
    private String history;
    private Object[] prioritize;

    public DataForJson(List<String> tasks, String history, List<String> prioritize) {
        this.tasks = getTasksArray(tasks);
        this.history = history;
        this.prioritize = getPrioritizeArray(prioritize);
    }

    public String[] getTasksArray(List<String> tasks) {
        String[] array = new String[tasks.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = tasks.get(i);
        }
        return array;
    }

    public Object[] getPrioritizeArray(List<String> tasks) {
        Object[] array = tasks.toArray();
        return array;
    }

    public String[] getTasks() {
        return tasks;
    }

    public void setTasks(String[] tasks) {
        this.tasks = tasks;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Object[] getPrioritize() {
        return prioritize;
    }

    public void setPrioritize(List<String> prioritize) {
        this.prioritize = getPrioritizeArray(prioritize);
    }
}

