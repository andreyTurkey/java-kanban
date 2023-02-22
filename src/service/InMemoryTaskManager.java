package service;

import model.*;

import java.io.IOException;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int counter = 1;
    protected Map<Integer, Task> tasksStorages = new HashMap();
    protected Map<Integer, Epic> epicsStorage = new HashMap();
    protected Map<Integer, Subtask> subtaksStorage = new HashMap();

    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public Map<Integer, Task> getTasksStorages() {
        return tasksStorages;
    }

    public Map<Integer, Epic> getEpicsStorage() {
        return epicsStorage;
    }

    public Map<Integer, Subtask> getSubtaksStorage() {
        return subtaksStorage;
    }

    protected Comparator<Task> comparator = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.getStartTime() != null && task2.getStartTime() != null) {
                if (task1.getStartTime().isAfter(task2.getStartTime())) {
                    return 1;
                } else if (task1.getStartTime().isBefore(task2.getStartTime())) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        }
    };

    protected Set<Task> prioritizedTasks = new TreeSet<>(comparator);

    @Override
    public void timeValidator(Task newTask) {
        if (newTask.getStartTime() != null) {
            for (Task task : prioritizedTasks) {
                if (task.getStartTime() != null) {
                    if (!newTask.getStartTime().isAfter(task.getEndTime().minusSeconds(1))
                            && !newTask.getEndTime().isBefore(task.getStartTime().plusSeconds(1))) {
                        throw new IllegalStateException("Введенное время для задачи " + newTask +
                                " занято задачей " + task.getName() + "\n" +
                                "Выберите другое время");
                    }
                    if (newTask.getStartTime().isBefore(task.getStartTime())
                            && newTask.getEndTime().isAfter(task.getEndTime())) {
                        throw new IllegalStateException("Введенное время для задачи " + newTask +
                                " занято задачей " + task.getName() + "\n" +
                                "Выберите другое время");
                    }

                }
            }
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public void addNewTask(Task task) throws IOException, IllegalStateException {
        try {
            timeValidator(task);
        } catch (IllegalStateException exc) {
            System.out.println(exc.getMessage());
            return;
        }
        task.setId(counter++);
        tasksStorages.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateTask(Task task) throws IllegalStateException {
        try {
            timeValidator(task);
        } catch (IllegalStateException exc) {
            System.out.println(exc.getMessage());
            return;
        }
        int idTask = task.getId();
        for (Integer idMap : tasksStorages.keySet()) {
            if (idTask == idMap) {
                tasksStorages.put(idTask, task);
                prioritizedTasks.add(task);
            } else {
                tasksStorages.put(idTask, task);
                prioritizedTasks.add(task);
            }
        }
    }


    @Override
    public void addNewEpic(Epic epic) throws IOException {
        epic.setId(counter++);
        epicsStorage.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null) {
            int idEpic = epic.getId();
            for (Integer idMapEpic : epicsStorage.keySet()) {
                if (idEpic == idMapEpic) {

                    updateStatus(epic);

                    epicsStorage.put(idEpic, epic);
                }
                if (!(idEpic == idMapEpic)) {
                    epic.setId(idEpic);
                    updateStatus(epic);
                    epicsStorage.put(idEpic, epic);
                }
            }
        }
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException, IllegalStateException {
        try {
            timeValidator(subtask);
        } catch (IllegalStateException exc) {
            System.out.println(exc.getMessage());
            return;
        }
        int idSubtask = subtask.getId();
        if (subtaksStorage.isEmpty()) {
            if (subtask.getId() == 0) {
                subtask.setId(counter++);
                subtaksStorage.put(subtask.getId(), subtask);
                prioritizedTasks.add(subtask);
                for (Integer idEpic : epicsStorage.keySet()) {
                    if (idEpic == subtask.getEpicId()) {
                        Epic epic = epicsStorage.get(idEpic);
                        epic.addSubtaskInList(subtask.getId());
                        epic.addSubtasks(subtask);
                        updateEpic(epic);
                    }
                }
            }
        } else if (idSubtask == 0) {
            subtask.setId(counter++);
            subtaksStorage.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            for (Integer idEpic : epicsStorage.keySet()) {
                if (idEpic == subtask.getEpicId()) {
                    Epic epic = epicsStorage.get(idEpic);
                    epic.addSubtaskInList(subtask.getId());
                    epic.addSubtasks(subtask);
                    updateEpic(epic);
                }
            }
        } else if (subtaksStorage.isEmpty()) {
            subtaksStorage.put(idSubtask, subtask);
            prioritizedTasks.add(subtask);
            for (Integer idEpic : epicsStorage.keySet()) {
                if (idEpic == subtask.getEpicId()) {
                    Epic epic = epicsStorage.get(idEpic);
                    epic.addSubtaskInList(subtask.getId());
                    epic.addSubtasks(subtask);
                    updateEpic(epic);
                }
            }
        } else {
            for (Integer idMapSubtask : subtaksStorage.keySet()) {
                if (idSubtask == idMapSubtask) {
                    subtaksStorage.put(idSubtask, subtask);
                    prioritizedTasks.add(subtask);
                }
                if (!(idSubtask == idMapSubtask)) {
                    subtask.setId(idSubtask);
                    subtaksStorage.put(idSubtask, subtask);
                    for (Integer idEpic : epicsStorage.keySet()) {
                        if (idEpic == subtask.getEpicId()) {
                            Epic epic = epicsStorage.get(idEpic);
                            epic.addSubtaskInList(subtask.getId());
                            epic.addSubtasks(subtask);
                            updateEpic(epic);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateStatus(Epic epic) {
        List<Integer> idlistSubtasks = epic.getSubtaskIds();
        List<Subtask> listSubtasks = new ArrayList<>();
        for (Integer idSub : subtaksStorage.keySet()) {
            for (Integer idSubFromList : idlistSubtasks) {
                if (idSubFromList == idSub) {
                    listSubtasks.add(subtaksStorage.get(idSub));
                }
            }
        }
        for (Subtask anySub : listSubtasks) {
            if (!(anySub.getStatus() == Status.NEW)) {
                epic.setStatus(Status.IN_PROGRESS);
                break;
            }
        }
        for (Subtask anySub : listSubtasks) {
            if (!(anySub.getStatus() == Status.DONE)) {
                break;
            } else {
                epic.setStatus(Status.DONE);
            }
        }
        if (listSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        for (Subtask anySub : listSubtasks) {
            if (anySub.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                break;
            }
        }
    }

    // Методы получение списка задач
    @Override
    public List<Task> getListTask() {
        return new ArrayList(tasksStorages.values());
    }

    @Override
    public List<Epic> getListEpic() {
        return new ArrayList(epicsStorage.values());
    }

    @Override
    public List<Subtask> getListSubtask() {
        return new ArrayList(subtaksStorage.values());
    }

    // Методы очистки хэш мапы
    @Override
    public Map<Integer, Task> clearTasks() throws IOException {
        tasksStorages.clear();
        return new HashMap(tasksStorages);
    }

    @Override
    public Map<Integer, Epic> clearEpics() throws IOException {
        epicsStorage.clear();
        return new HashMap(epicsStorage);
    }

    @Override
    public Map<Integer, Subtask> clearSubtasks() throws IOException {
        Set<Integer> epicsId = new HashSet<>();
        for (Subtask subtask : subtaksStorage.values()) {
            prioritizedTasks.remove(subtask);
            int id = subtask.getEpicId();
            epicsId.add(id);
        }
        subtaksStorage.clear();
        for (int id : epicsId) {
            Epic epic = epicsStorage.get(id);
            if (epic != null) {
                epic.clearSubtasks();
                updateEpic(epic);
            }
        }
        return new HashMap(subtaksStorage);
    }

    // Методы получения по ID
    @Override
    public Task getWithIdTask(int id) throws IOException {
        Task task = tasksStorages.get(id);
        inMemoryHistoryManager.add(task);
        return tasksStorages.get(id);
    }

    @Override
    public Epic getWithIdEpics(int id) throws IOException {
        Task task = epicsStorage.get(id);
        inMemoryHistoryManager.add(task);
        return epicsStorage.get(id);
    }

    @Override
    public Subtask getWithIdSubtasks(int id) throws IOException {
        Task task = subtaksStorage.get(id);
        inMemoryHistoryManager.add(task);
        return subtaksStorage.get(id);
    }

    // Методы удаления задач по ID
    @Override
    public Map<Integer, Task> removeTask(int idRemovedTask) throws IOException {
        Task task = tasksStorages.get(idRemovedTask);
        tasksStorages.remove(idRemovedTask);
        prioritizedTasks.remove(task);
        inMemoryHistoryManager.remove(idRemovedTask);
        return new HashMap(tasksStorages);
    }

    @Override
    public Map<Integer, Epic> removeEpic(int idRemovedEpic) throws IOException {
        Epic epic = epicsStorage.get(idRemovedEpic);
        if (epic.getSubtaskIds().size() != 0) {
            List<Integer> subtasksId = epic.getSubtaskIds();
            for (Integer id : subtasksId) {
                subtaksStorage.remove(id);
                Subtask subtask = subtaksStorage.get(id);
                prioritizedTasks.remove(subtask);
                inMemoryHistoryManager.remove(id);
            }
        }
        epicsStorage.remove(idRemovedEpic);
        inMemoryHistoryManager.remove(idRemovedEpic);
        return new HashMap(epicsStorage);
    }

    @Override
    public Map<Integer, Subtask> removeSubtask(int idRemovedSubtasks) throws IOException {
        Subtask subtask = subtaksStorage.get(idRemovedSubtasks);
        prioritizedTasks.remove(subtask);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            Epic epic = epicsStorage.get(epicId);
            epic.deleteSubtask(subtask);
            List<Integer> idSubtasks = epic.getSubtaskIds();
            if (idSubtasks.size() != 0) {
                int index = idSubtasks.indexOf(idRemovedSubtasks);
                idSubtasks.remove(index);
            }
            epic.setSubtaskIds(idSubtasks);
            subtaksStorage.remove(idRemovedSubtasks);
            if (idSubtasks.size() == 0) {
                epic.setStatus(Status.NEW);
            }
            updateEpic(epic);
            inMemoryHistoryManager.remove(idRemovedSubtasks);
        }
        return new HashMap(subtaksStorage);
    }

    @Override
    public void printTask() {
        for (Integer task : tasksStorages.keySet()) {
            System.out.println(task + " " + tasksStorages.get(task));
        }
    }

    @Override
    public void printEpic() {
        for (Integer task : epicsStorage.keySet()) {
            System.out.println(task + " " + epicsStorage.get(task));
        }
    }

    @Override
    public void printSubtask() {
        for (Integer task : subtaksStorage.keySet()) {
            System.out.println(task + " " + subtaksStorage.get(task));
        }
    }

    @Override
    public void printHistory() {
        List<Task> newHistory = inMemoryHistoryManager.getHistory();
        for (Task task : newHistory) {
            System.out.println(task);
        }
    }

    @Override
    public void printMap() {
        Map<Integer, Node> taskForPrint = inMemoryHistoryManager.fromMap();
        for (Integer id : taskForPrint.keySet()) {
            Node node = taskForPrint.get(id);
            System.out.println(id + " " + node);
        }
    }

    @Override
    public void taskFromFile(Task task) {
        tasksStorages.put(task.getId(), task);
    }

    @Override
    public void epicFromFile(Epic epic) {
        epicsStorage.put(epic.getId(), epic);
    }

    @Override
    public void subtaskFromFile(Subtask subtask) {
        int idSubtask = subtask.getId();
        subtaksStorage.put(idSubtask, subtask);
        for (Integer idEpic : epicsStorage.keySet()) {
            if (idEpic == subtask.getId()) {
                Epic epic = epicsStorage.get(idEpic);
                epic.addSubtaskInList(subtask.getId());
            }
        }
    }
}



