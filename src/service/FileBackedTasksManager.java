package service;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static String path = "History.csv";
    private static final int TYPE_INDEX = 1;

    public static void main(String[] args) throws IOException {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File(path));

        /*TaskManager taskManager = Managers.getDefaultFileBacked();

        taskManager.addNewTask(new Task("Задача Один", "Описание"));
        taskManager.addNewTask(new Task("Задача Два", "Описание"));
        taskManager.addNewTask(new Task("Задача Три", "Описание"));

        taskManager.addNewEpic(new Epic("Эпик Задача Один", "Описание"));
        taskManager.addNewEpic(new Epic("Эпик Задача Два", "Описание"));

        taskManager.addSubtask(new Subtask("Подзадача один", "Описание"));
        taskManager.addSubtask(new Subtask("Подзадача два", "Описание"));
        taskManager.addSubtask(new Subtask("Подзадача три", "Описание"));

        taskManager.getWithIdTask(3);
        taskManager.getWithIdTask(1);
        taskManager.getWithIdTask(2);
        taskManager.getWithIdEpics(4);
        taskManager.getWithIdEpics(5);
        taskManager.getWithIdSubtasks(6);
        taskManager.getWithIdSubtasks(7);
        taskManager.getWithIdSubtasks(8);*/
    }

    static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        List<String> unpackedFile = fileBackedTasksManager.unpackFile(file);

        String stringHistory = fileBackedTasksManager.getStringHistory(unpackedFile);

        List<String> stringsTasks = fileBackedTasksManager.getListTask(unpackedFile);

        fileBackedTasksManager.makingTasks(stringsTasks);

        List<Integer> historyFromFile = historyFromString(stringHistory);

        fileBackedTasksManager.recoveryHistory(historyFromFile);

        return fileBackedTasksManager;
    }

    public void recoveryHistory(List<Integer> historyFromFile) {
        List<Task> listTasks = getListTask();
        List<Epic> listEpics = getListEpic();
        List<Subtask> listSubtaks = getListSubtask();

        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(listTasks);
        allTasks.addAll(listEpics);
        allTasks.addAll(listSubtaks);

        for (Integer id : historyFromFile) {
            for (Task task : allTasks) {
                if (id == task.getId()) {
                    inMemoryHistoryManager.add(task);
                }
            }
        }
    }

    public List<String> unpackFile(File file) throws IOException {
        List<String> taskFromFile = new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                taskFromFile.add(line);
            }
        } catch (IOException exc) {
            System.out.println(String.format(
                    "Невозможно прочитать файл. Возможно файл не находится в %s.", path));
        }
        taskFromFile.remove(0);
        return taskFromFile;
    }

    public void makingTasks(List<String> tasks) throws IOException {
        for (String str : tasks) {
            String[] lines = str.split(",");
            if (Type.valueOf(lines[TYPE_INDEX]) == Type.TASK) {
                Task task = new Task();
                task = task.fromString(str);
                taskFromFile(task);
            }
            else if (Type.valueOf(lines[TYPE_INDEX]) == Type.EPIC) {
                Epic epic = new Epic();
                epic = epic.fromString(str);
                epicFromFile(epic);
            } else {
                Subtask subtask = new Subtask();
                subtask = subtask.fromString(str);
                subtaskFromFile(subtask);
            }
        }
    }

    private List<String> getListTask(List<String> stringsFromFile) {
        List<String> stringsTasks = new ArrayList<>();
        for (String str : stringsFromFile) {
            if (str.equals("")) {
                break;
            }
            stringsTasks.add(str);
        }
        return stringsTasks;
    }

    public String getStringHistory(List<String> stringsFromFile) {
        String str = stringsFromFile.get(stringsFromFile.size() - 1);
        return str;
    }

    public void save() throws IOException {
        List<Task> taskToHistory = new ArrayList<>();
        taskToHistory.addAll(getListTask());
        taskToHistory.addAll(getListEpic());
        taskToHistory.addAll(getListSubtask());
        String history = historyToString(inMemoryHistoryManager);

        try (Writer writer = new FileWriter(path)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : taskToHistory) {
                if (task.getType() != Type.SUBTASK) {
                    writer.write(task.toString(task));
                } else {
                    Subtask subtask = (Subtask) task;
                    writer.write(subtask.toString(subtask));
                }
            }
            writer.write("\n");
            writer.write(history);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    static String historyToString(HistoryManager manager) { // Для сохранения истории в файл
        List<Task> history = manager.getHistory();
        List<String> id = new ArrayList<>();
        for (Task task : history) {
            int x = task.getId();
            String newId = String.format("%d", x);
            id.add(newId);
        }
        String str = String.join(",", id);
        return str;
    }

    static List<Integer> historyFromString(String value) { // Восстановление ИСТОРИИ из файла
        List<Integer> history = new ArrayList<>();
        String[] parts = value.split(",");
        for (String id : parts) {
            int idFromValue = Integer.parseInt(id);
            history.add(idFromValue);
        }
        return history;
    }

    @Override
    public void addNewTask(Task task) throws IOException {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) throws IOException {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Task getWithIdTask(int id) throws IOException {
        super.getWithIdTask(id);
        save();
        return tasksStorages.get(id);
    }

    @Override
    public Epic getWithIdEpics(int id) throws IOException {
        super.getWithIdEpics(id);
        save();
        return epicsStorage.get(id);
    }

    @Override
    public Subtask getWithIdSubtasks(int id) throws IOException {
        super.getWithIdSubtasks(id);
        save();
        return subtaksStorage.get(id);
    }
}

