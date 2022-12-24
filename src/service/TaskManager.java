package service;

import model.Task;
import model.Subtask;
import model.Epic;
import model.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int counter = 1;

    private HashMap<Integer, Task> tasksStorages = new HashMap();
    private HashMap<Integer, Epic> epicsStorage = new HashMap();
    private HashMap<Integer, Subtask> subtaksStorage = new HashMap();

    public int addNewTask(Task task) {
        task.setId(counter++);
        tasksStorages.put(task.getId(), task);
        return task.getId();
    }

    public int updatingTask(Task task) {
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

    public int addNewEpic(Epic epic){
        epic.setId(counter++);
        epicsStorage.put(epic.getId(), epic);
        return epic.getId();
    }

    public int updatingEpic(Epic epic){
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

    public int addNewSubtaskAndUpdate(Subtask subtask) {
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
    public ArrayList<Task> getListTask() {
        return new ArrayList(tasksStorages.values());
    }

    public ArrayList<Epic> getListEpic() {
        return new ArrayList(epicsStorage.values());
    }

    public ArrayList<Subtask> getListSubtask() {
        return new ArrayList(subtaksStorage.values());
    }

    // Методы очистки хэш мапы
    public HashMap<Integer, Task> clearTasks() {
        tasksStorages.clear();
        return new HashMap(tasksStorages);
    }

    public HashMap<Integer, Epic> clearEpics() {
        epicsStorage.clear();
        return new HashMap(epicsStorage);
    }

    public HashMap<Integer, Subtask> clearSubtasks() {
        subtaksStorage.clear();
        return new HashMap(subtaksStorage);
    }

    // Методы получения по ID
    public Task getWithIdTask(int id) {
        return tasksStorages.get(id);
    }

    public Epic getWithIdEpics(int id) {
        return epicsStorage.get(id);
    }

    public Subtask getWithIdSubtasks(int id) {
        return subtaksStorage.get(id);
    }

    // Методы удаления задач по ID
    public HashMap<Integer, Task> removeTask(int idRemovedTask) {
        tasksStorages.remove(idRemovedTask);
        return tasksStorages;
    }

    public HashMap<Integer, Epic> removeEpic(int idRemovedEpic) {
        epicsStorage.remove(idRemovedEpic);
        return epicsStorage;
    }

    public HashMap<Integer, Subtask> removeSubtask(int idRemovedSubtasks) {
        subtaksStorage.remove(idRemovedSubtasks);
        return subtaksStorage;
    }

    public void printTask() {
        for (Integer task : tasksStorages.keySet()) {
            System.out.println(task + " " + tasksStorages.get(task));
        }
    }

    public void printEpic() {
        for (Integer task : epicsStorage.keySet()) {
            System.out.println(task + " " + epicsStorage.get(task));
        }
    }

    public void printSubtask() {
        for (Integer task : subtaksStorage.keySet()) {
            System.out.println(task + " " + subtaksStorage.get(task));
        }
    }
}
