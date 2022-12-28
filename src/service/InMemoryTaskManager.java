package service;

import model.Task;
import model.Subtask;
import model.Epic;
import model.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int counter = 1;
    private HashMap<Integer, Task> tasksStorages = new HashMap();
    private HashMap<Integer, Epic> epicsStorage = new HashMap();
    private HashMap<Integer, Subtask> subtaksStorage = new HashMap();

    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    @Override
    public int addNewTask(Task task) {
        task.setId(counter++);
        tasksStorages.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int updateTask(Task task) {
        int idTask = task.getId();
        for (Integer idMap : tasksStorages.keySet()) {
            if (idTask == idMap) {
                tasksStorages.put(idTask, task);
            }
            if (!(idTask == idMap)) {
                task.setId(idTask);
                tasksStorages.put(idTask, task);
            }
        }
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(counter++);
        epicsStorage.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int updateEpic(Epic epic) {
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
        return epic.getId();
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int idSubtask = subtask.getId();
        if (subtaksStorage.isEmpty()) {
            if (subtask.getId() == 0) {
                subtask.setId(counter++);
                subtaksStorage.put(subtask.getId(), subtask);
                for (Integer idEpic : epicsStorage.keySet()) {
                    if (idEpic == subtask.getId()) {
                        Epic epic = epicsStorage.get(idEpic);
                        epic.addSubtaskInList(subtask.getId());
                    }
                }
            }
        } else if (idSubtask == 0) {
            subtask.setId(counter++);
            subtaksStorage.put(subtask.getId(), subtask);
            for (Integer idEpic : epicsStorage.keySet()) {
                if (idEpic == subtask.getId()) {
                    Epic epic = epicsStorage.get(idEpic);
                    epic.addSubtaskInList(subtask.getId());
                }
            }
        } else if (subtaksStorage.isEmpty()) {
            subtaksStorage.put(idSubtask, subtask);
            for (Integer idEpic : epicsStorage.keySet()) {
                if (idEpic == subtask.getId()) {
                    Epic epic = epicsStorage.get(idEpic);
                    epic.addSubtaskInList(subtask.getId());
                }
            }
        } else {
            for (Integer idMapSubtask : subtaksStorage.keySet()) {
                if (idSubtask == idMapSubtask) {
                    subtaksStorage.put(idSubtask, subtask);
                }
                if (!(idSubtask == idMapSubtask)) {
                    subtask.setId(idSubtask);
                    subtaksStorage.put(idSubtask, subtask);
                    for (Integer idEpic : epicsStorage.keySet()) {
                        if (idEpic == subtask.getId()) {
                            Epic epic = epicsStorage.get(idEpic);
                            epic.addSubtaskInList(subtask.getId());
                        }
                    }
                }
            }
        }
        return subtask.getId();
    }

    @Override
    public void updateStatus(Epic epic) {
        ArrayList<Integer> idlistSubtasks = epic.getSubtaskIds();

        ArrayList<Subtask> listSubtasks = new ArrayList<>();
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
    public ArrayList<Task> getListTask() {
        return new ArrayList(tasksStorages.values());
    }

    @Override
    public ArrayList<Epic> getListEpic() {
        return new ArrayList(epicsStorage.values());
    }

    @Override
    public ArrayList<Subtask> getListSubtask() {
        return new ArrayList(subtaksStorage.values());
    }

    // Методы очистки хэш мапы
    @Override
    public HashMap<Integer, Task> clearTasks() {
        tasksStorages.clear();
        return new HashMap(tasksStorages);
    }

    @Override
    public HashMap<Integer, Epic> clearEpics() {
        epicsStorage.clear();
        return new HashMap(epicsStorage);
    }

    @Override
    public HashMap<Integer, Subtask> clearSubtasks() {
        subtaksStorage.clear();
        return new HashMap(subtaksStorage);
    }

    // Методы получения по ID
    @Override
    public Task getWithIdTask(int id) {
        inMemoryHistoryManager.add(tasksStorages.get(id));
        return tasksStorages.get(id);
    }

    @Override
    public Epic getWithIdEpics(int id) {
        Task task = epicsStorage.get(id);
        inMemoryHistoryManager.add(task);
        return epicsStorage.get(id);
    }

    @Override
    public Subtask getWithIdSubtasks(int id) {
        Task task = subtaksStorage.get(id);
        inMemoryHistoryManager.add(task);
        return subtaksStorage.get(id);
    }

    // Методы удаления задач по ID
    @Override
    public HashMap<Integer, Task> removeTask(int idRemovedTask) {
        tasksStorages.remove(idRemovedTask);
        return tasksStorages;
    }

    @Override
    public HashMap<Integer, Epic> removeEpic(int idRemovedEpic) {
        epicsStorage.remove(idRemovedEpic);
        return epicsStorage;
    }

    @Override
    public HashMap<Integer, Subtask> removeSubtask(int idRemovedSubtasks) {
        subtaksStorage.remove(idRemovedSubtasks);
        return subtaksStorage;
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

}



