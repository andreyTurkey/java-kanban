package service;

import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static String path = "History.csv";
    private static final int TYPE_INDEX = 1;
    protected static final int ID_INDEX = 0;
    protected static final int NAME_INDEX = 2;
    protected static final int STATUS_INDEX = 3;
    protected static final int DESCRIPTION_INDEX = 4;
    protected static final int DURATION_INDEX = 5;
    protected static final int STARTTIME_INDEX = 6;
    protected static final int EPIC_ID_INDEX = 5;
    protected static final int SUBTASK_DURATION_INDEX = 6;
    protected static final int SUBTASK_STARTTIME_INDEX = 7;
    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    public static void main(String[] args) throws IOException, InterruptedException {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File(path));

       /* TaskManager taskManager = Managers.getDefaultFileBacked();

        taskManager.addNewTask(new Task("Задача один", "Описание", "10.01.2023 10:00", 30));
        taskManager.addNewTask(new Task("Задача Два", "Описание", "10.01.2023 10:30", 30));
        taskManager.addNewTask(new Task("Задача Три", "Описание"));

        Epic epic1 = new Epic("Эпик 1", "Эпик 1");
        taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Эпик 2");
        taskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Сабтаска 1", "Сабтаска 1");
        subtask1.setEpicId(epic1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаска 2", "Сабтаска 2");
        subtask2.setEpicId(epic1);
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сабтаска 3", "Сабтаска 3", "10.01.2023 09:50", 10);
        subtask3.setEpicId(epic2);
        taskManager.addSubtask(subtask3);

        taskManager.getWithIdTask(1);
        taskManager.getWithIdTask(2);
        taskManager.getWithIdTask(3);
        taskManager.getWithIdEpics(4);
        taskManager.getWithIdEpics(5);*/
        //taskManager.removeEpic(5);
        //taskManager.removeEpic(4);
        List<Epic> epics = fileBackedTasksManager.getListEpic();
        for (Epic epic : epics) {
            System.out.println(epic);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
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
            throw new IOException(String.format(
                    "Невозможно прочитать файл. Возможно файл не находится в %s.", path));
        }
        try {
            taskFromFile.remove(0);
        } catch (IndexOutOfBoundsException exc) {
            throw new IndexOutOfBoundsException("Невозможно прочитать файл. Файл поврежден или имеет неверный формат.");
        }
        return taskFromFile;
    }

    public void makingTasks(List<String> tasks) {
        for (String str : tasks) {
            String[] lines = str.split(",");
            if (Type.valueOf(lines[TYPE_INDEX]) == Type.TASK) {
                Task newTask = fromStringTask(str);
                taskFromFile(newTask);
            } else if (Type.valueOf(lines[TYPE_INDEX]) == Type.SUBTASK) {
                Subtask subtask = fromStringSubtask(str);
                subtaskFromFile(subtask);
            } else {
                Epic epic = fromStringEpic(str);
                epicFromFile(epic);
            }
        }
    }

    public Task fromStringTask(String value) {
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
        if (!(parts.length < 7)) {
            task.setDuration(Duration.parse(parts[DURATION_INDEX]));
        }
        if (!(parts.length < 7)) {
            String str = parts[STARTTIME_INDEX].replaceAll("\n", "");
            LocalDateTime startTime = LocalDateTime.parse(str, formatter);
            task.setStart(startTime);
        }
        return task;
    }

    public Subtask fromStringSubtask(String value) {
        Subtask subtask = new Subtask();
        String[] parts = value.split(",");
        subtask.setId(Integer.parseInt(parts[ID_INDEX]));
        subtask.setType(Type.SUBTASK);
        subtask.setName(parts[NAME_INDEX]);
        if (Status.valueOf(parts[STATUS_INDEX]) == Status.NEW) {
            subtask.setStatus(Status.NEW);
        } else if (Status.valueOf(parts[STATUS_INDEX]) == Status.IN_PROGRESS) {
            subtask.setStatus(Status.IN_PROGRESS);
        } else {
            subtask.setStatus(Status.DONE);
        }
        subtask.setDescription(parts[DESCRIPTION_INDEX]);
        if (!(parts.length < 7)) {
            String str = parts[EPIC_ID_INDEX].replaceAll("\n", "");
            int idIndex = Integer.parseInt(str);
            subtask.setEpicId(idIndex);
            subtask.setDuration(Duration.parse(parts[SUBTASK_DURATION_INDEX]));
            String partSartTime = parts[SUBTASK_STARTTIME_INDEX].replaceAll("\n", "");
            subtask.setStart(LocalDateTime.parse(partSartTime, formatter));
        }
        return subtask;
    }

    public Epic fromStringEpic(String value) {
        Epic epic = new Epic();
        String[] parts = value.split(",");
        epic.setId(Integer.parseInt(parts[ID_INDEX]));
        epic.setType(Type.EPIC);
        epic.setName(parts[NAME_INDEX]);
        if (Status.valueOf(parts[STATUS_INDEX]) == Status.NEW) {
            epic.setStatus(Status.NEW);
        } else if (Status.valueOf(parts[STATUS_INDEX]) == Status.IN_PROGRESS) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.DONE);
        }
        epic.setDescription(parts[DESCRIPTION_INDEX]);
        List<Subtask> subtasksForCalculatingTime = new ArrayList<>();
        List<Subtask> subtasks = getListSubtask();
        for (Subtask subtask : subtasks) {
            if (epic.getId() == subtask.getEpicId()) {
                subtasksForCalculatingTime.add(subtask);
            }
        }
        epic.setSubtasks(subtasksForCalculatingTime);
        epic.calculateEpicTime();
        return epic;
    }

    public List<String> getListTask(List<String> stringsFromFile) {
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

    public void save() {
        List<Task> taskToHistory = getListTask();
        List<Epic> epicToHistory = getListEpic();
        List<Subtask> subtaskToHistory = getListSubtask();
        String history = historyToString(inMemoryHistoryManager);
        try (Writer writer = new FileWriter(path)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : taskToHistory) {
                writer.write(task.toStringForFile(task));
            }
            for (Epic task : epicToHistory) {
                writer.write(toStringEpic(task));
            }
            for (Subtask task : subtaskToHistory) {
                writer.write(toStringSubtask(task));
            }
            writer.write("\n");
            writer.write(history);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static String historyToString(HistoryManager manager) { // Для сохранения истории в файл
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

    protected static List<Integer> historyFromString(String value) { // Восстановление ИСТОРИИ из файла
        List<Integer> history = new ArrayList<>();
        String[] parts = value.split(",");
        for (String id : parts) {
            if (!parts[0].isBlank()) {
                int idFromValue = Integer.parseInt(id);
                history.add(idFromValue);
            }
        }
        return history;
    }

    public String toStringSubtask(Subtask task) {
        if (task.getDuration() != null || task.getStartTime() != null) {
            return String.format("%d,%S,%s,%S,%s,%d,%s,%s\n"
                    , task.getId()
                    , task.getType()
                    , task.getName()
                    , task.getStatus()
                    , task.getDescription()
                    , task.getEpicId()
                    , task.getDuration()
                    , task.getStartTime().format(formatter));
        }
        return String.format("%d,%S,%s,%S,%s,%d\n"
                , task.getId()
                , task.getType()
                , task.getName()
                , task.getStatus()
                , task.getDescription()
                , task.getEpicId());
    }

    public String toStringEpic(Epic task) {
        if (task.getDuration() != null || task.getStartTime() != null) {
            return String.format("%d,%S,%s,%S,%s,%s,%s,%s\n"
                    , task.getId()
                    , task.getType()
                    , task.getName()
                    , task.getStatus()
                    , task.getDescription()
                    , task.getDuration()
                    , task.getStartTime().format(formatter)
                    , task.getEndTime().format(formatter));
        }
        return String.format("%d,%S,%s,%S,%S\n"
                , task.getId()
                , task.getType()
                , task.getName()
                , task.getStatus()
                , task.getDescription());
    }


    public String stringHistory() {
        String history = historyToString(inMemoryHistoryManager);
        return history;
    }

    public List<Task> getTasksFromHistory() {
        List<Task> tasks = inMemoryHistoryManager.getHistory();
        return tasks;
    }

    @Override
    public void addNewTask(Task task) throws IOException, InterruptedException {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) throws IOException, InterruptedException {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException, InterruptedException {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Task getWithIdTask(int id) throws IOException, InterruptedException {
        super.getWithIdTask(id);
        save();
        return tasksStorages.get(id);
    }

    @Override
    public Epic getWithIdEpics(int id) throws IOException, InterruptedException {
        super.getWithIdEpics(id);
        save();
        return epicsStorage.get(id);
    }

    @Override
    public Subtask getWithIdSubtasks(int id) throws IOException, InterruptedException {
        super.getWithIdSubtasks(id);
        save();
        return subtaksStorage.get(id);
    }

    @Override
    public Map<Integer, Task> removeTask(int idRemovedTask) throws IOException, InterruptedException {
        super.removeTask(idRemovedTask);
        save();
        return new HashMap(tasksStorages);
    }

    @Override
    public Map<Integer, Epic> removeEpic(int idRemovedEpic) throws IOException, InterruptedException {
        super.removeEpic(idRemovedEpic);
        save();
        return new HashMap(epicsStorage);
    }

    @Override
    public Map<Integer, Subtask> removeSubtask(int idRemovedSubtasks) throws IOException, InterruptedException {
        super.removeSubtask(idRemovedSubtasks);
        save();
        return new HashMap(subtaksStorage);
    }

    @Override
    public Map<Integer, Task> clearTasks() throws IOException, InterruptedException {
        super.clearTasks();
        save();
        return new HashMap(tasksStorages);
    }

    @Override
    public Map<Integer, Epic> clearEpics() throws IOException, InterruptedException {
        super.clearEpics();
        save();
        return new HashMap(epicsStorage);
    }

    @Override
    public Map<Integer, Subtask> clearSubtasks() throws IOException, InterruptedException {
        super.clearSubtasks();
        save();
        return new HashMap(subtaksStorage);
    }
}

