package taskManager;

import tasks.Task;
import tasks.Subtask;
import tasks.Epic;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int counter = 1;

    HashMap<Integer, Task> listTasks = new HashMap();
    HashMap<Integer, Epic> listEpics = new HashMap();
    HashMap<Integer, Subtask> listSubtasks = new HashMap();

    public int addNewTaskAndUpdate(Task anyTask) {
        int idTask = anyTask.getId();
        if (listTasks.isEmpty()) {
            if (anyTask.getId() == 0) {
                anyTask.setId(counter++);
                listTasks.put(anyTask.getId(), anyTask);
            }
        } else if (idTask == 0) {
            anyTask.setId(counter++);
            listTasks.put(anyTask.getId(), anyTask);
        } else if (listTasks.isEmpty()) {
            listTasks.put(idTask, anyTask);
        } else {
            for (Integer idMap : listTasks.keySet()) {
                if (idTask == idMap) {
                    listTasks.put(idTask, anyTask);
                }
                if (!(idTask == idMap)) {
                    anyTask.setId(idTask);
                    listTasks.put(idTask, anyTask);
                }
            }
        }
        return anyTask.getId();
    }

    public int addNewEpicAndUpdate(Epic anyEpic) {
        int idEpic = anyEpic.getId();
        if (listEpics.isEmpty()) {
            if (anyEpic.getId() == 0) {
                anyEpic.setId(counter++);
                listEpics.put(anyEpic.getId(), anyEpic);
            }
        } else if (idEpic == 0) {
            anyEpic.setId(counter++);
            listEpics.put(anyEpic.getId(), anyEpic);
        } else if (listEpics.isEmpty()) {
            listEpics.put(idEpic, anyEpic);
        } else {
            for (Integer idMapEpic : listEpics.keySet()) {
                if (idEpic == idMapEpic) {

                    updateStatus(anyEpic);

                    listEpics.put(idEpic, anyEpic);
                }
                if (!(idEpic == idMapEpic)) {
                    anyEpic.setId(idEpic);
                    updateStatus(anyEpic);
                    listEpics.put(idEpic, anyEpic);
                }
            }
        }
        return anyEpic.getId();
    }

    public int addNewSubtaskAndUpdate(Subtask anySubtask) {
        int idSubtask = anySubtask.getId();
        if (listSubtasks.isEmpty()) {
            if (anySubtask.getId() == 0) {
                anySubtask.setId(counter++);
                listSubtasks.put(anySubtask.getId(), anySubtask);
                for (Integer idEpic : listEpics.keySet()) {
                    if (idEpic == anySubtask.getId()) {
                        Epic epic = listEpics.get(idEpic);
                        epic.addSubtaskInList(anySubtask.getId());
                    }
                }
            }
        } else if (idSubtask == 0) {
            anySubtask.setId(counter++);
            listSubtasks.put(anySubtask.getId(), anySubtask);
            for (Integer idEpic : listEpics.keySet()) {
                if (idEpic == anySubtask.getId()) {
                    Epic epic = listEpics.get(idEpic);
                    epic.addSubtaskInList(anySubtask.getId());
                }
            }
        } else if (listSubtasks.isEmpty()) {
            listSubtasks.put(idSubtask, anySubtask);
            for (Integer idEpic : listEpics.keySet()) {
                if (idEpic == anySubtask.getId()) {
                    Epic epic = listEpics.get(idEpic);
                    epic.addSubtaskInList(anySubtask.getId());
                }
            }
        } else {
            for (Integer idMapSubtask : listSubtasks.keySet()) {
                if (idSubtask == idMapSubtask) {
                    listSubtasks.put(idSubtask, anySubtask);
                }
                if (!(idSubtask == idMapSubtask)) {
                    anySubtask.setId(idSubtask);
                    listSubtasks.put(idSubtask, anySubtask);
                    for (Integer idEpic : listEpics.keySet()) {
                        if (idEpic == anySubtask.getId()) {
                            Epic epic = listEpics.get(idEpic);
                            epic.addSubtaskInList(anySubtask.getId());
                        }
                    }
                }
            }
        }
        return anySubtask.getId();
    }

    public ArrayList<Integer> getSubtasksOfEpic(Epic anyEpic) {
        ArrayList<Integer> newListSubtasks = new ArrayList<>();
        for (Integer idSubs : listSubtasks.keySet()) {
            Subtask newSub = listSubtasks.get(idSubs);
            if (anyEpic.getId() == newSub.getEpicId()) {
                newListSubtasks.add(idSubs);
            }
        }
        return newListSubtasks;
    }

    public void updateStatus(Epic anyEpic) {
        ArrayList<Integer> idlistSub = getSubtasksOfEpic(anyEpic);

        ArrayList<Subtask> listSub = new ArrayList<>();
        String[] allStatuses = anyEpic.getStatuses();
        for (Integer idSub : listSubtasks.keySet()) {
            for (Integer idSubFromList : idlistSub) {
                if (idSubFromList == idSub) {
                    listSub.add(listSubtasks.get(idSub));
                }
            }
        }
        for (Subtask anySub : listSub) {
            if (!(anySub.getStatus() == allStatuses[0])) {
                anyEpic.setStatus(allStatuses[1]);
                break;
            }
        }
        for (Subtask anySub : listSub) {
            if (!(anySub.getStatus() == allStatuses[2])) {
                break;
            } else {
                anyEpic.setStatus(allStatuses[2]);
            }
        }
        if (listSub.isEmpty()) {
            anyEpic.setStatus(allStatuses[0]);
        }
        for (Subtask anySub : listSub) {
            if (anySub.getStatus() == allStatuses[1]) {
                anyEpic.setStatus(allStatuses[1]);
                break;
            }
        }
    }

    // Методы получение списка задач
    public ArrayList<Task> getListTask() {
        HashMap<Integer, Task> tasks = listTasks;
        ArrayList<Task> listThisTasks = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (Task taskFromMap : tasks.values()) {
                listThisTasks.add(taskFromMap);
            }
        }
        return listThisTasks;
    }

    public ArrayList<Epic> getListEpic() {
        HashMap<Integer, Epic> epics = listEpics;
        ArrayList<Epic> listThisEpic = new ArrayList<>();
        if (!epics.isEmpty()) {
            for (Epic taskFromMap : epics.values()) {
                listThisEpic.add(taskFromMap);
            }
        }
        return listThisEpic;
    }

    public ArrayList<Subtask> getListSubtask() {
        HashMap<Integer, Subtask> subtasks = listSubtasks;
        ArrayList<Subtask> listThisSubtasks = new ArrayList<>();
        if (!subtasks.isEmpty()) {
            for (Subtask taskFromMap : subtasks.values()) {
                listThisSubtasks.add(taskFromMap);
            }
        }
        return listThisSubtasks;
    }

    // Методы очистки хэш мапы
    public HashMap<Integer, Task> clearTasks() {
        HashMap<Integer, Task> clearsHashMap = listTasks;
        clearsHashMap.clear();
        return clearsHashMap;
    }

    public HashMap<Integer, Epic> clearEpics() {
        HashMap<Integer, Epic> clearsHashMap = listEpics;
        clearsHashMap.clear();
        return clearsHashMap;
    }

    public HashMap<Integer, Subtask> clearSubtasks() {
        listSubtasks.clear();
        return listSubtasks;
    }

    // Методы получения по ID
    public Task getWithIdTask(Integer anyId) {
        Task anyTask = listTasks.get(anyId);
        return anyTask;
    }

    public Epic getWithIdEpics(Integer anyId) {
        Epic anyTask = listEpics.get(anyId);
        return anyTask;
    }

    public Subtask getWithIdSubtasks(Integer anyId) {
        Subtask anyTask = listSubtasks.get(anyId);
        return anyTask;
    }

    // Методы удаления задач по ID
    public HashMap<Integer, Task> removeTask(int idRemovedTask) {
        listTasks.remove(idRemovedTask);
        return listTasks;
    }

    public HashMap<Integer, Epic> removeEpic(int idRemovedEpic) {
        listEpics.remove(idRemovedEpic);
        return listEpics;
    }

    public HashMap<Integer, Subtask> removeSubtask(int idRemovedSubtasks) {
        listSubtasks.remove(idRemovedSubtasks);
        return listSubtasks;
    }

    // Возможный сценарий
    public void printMap() {
        Task task1 = new Task("Задача Один", "Описание");
        Task task2 = new Task("Задача Два", "Описание");
        addNewTaskAndUpdate(task1);
        addNewTaskAndUpdate(task2);
        Epic epic1 = new Epic("Эпик Задача Один", "Описание");
        Epic epic2 = new Epic("Эпик Задача Два", "Описание");
        addNewEpicAndUpdate(epic1);
        addNewEpicAndUpdate(epic2);
        Subtask subtask1 = new Subtask("Подзадача один", "Описание");
        Subtask subtask2 = new Subtask("Подзадача два", "Описание");
        Subtask subtask3 = new Subtask("Подзадача три", "Описание");
        addNewSubtaskAndUpdate(subtask1);
        addNewSubtaskAndUpdate(subtask2);
        addNewSubtaskAndUpdate(subtask3);
        subtask1.setEpicId(1);
        subtask2.setEpicId(2);
        subtask3.setEpicId(2);
        for (Integer task : listTasks.keySet()) {
            System.out.println(task + " " + listTasks.get(task));
        }
        System.out.println();
        for (Integer epic : listEpics.keySet()) {
            System.out.println(epic + " " + listEpics.get(epic));
        }
        System.out.println();
        for (Integer subtask : listSubtasks.keySet()) {
            System.out.println(subtask + " " + listSubtasks.get(subtask));
        }
        System.out.println();
        String[] subStatus = subtask1.getStatuses();

        task1.setStatus(subStatus[1]);
        epic1.setStatus(subStatus[2]);
        subtask1.setStatus(subStatus[2]);
        subtask2.setStatus(subStatus[2]);
        subtask3.setStatus(subStatus[2]);

        for (Integer task : listTasks.keySet()) {
            System.out.println(task + " " + listTasks.get(task));
        }
        System.out.println();
        for (Integer epic : listEpics.keySet()) {
            System.out.println(epic + " " + listEpics.get(epic));
        }
        System.out.println();
        for (Integer subtask : listSubtasks.keySet()) {
            System.out.println(subtask + " " + listSubtasks.get(subtask));
        }
        System.out.println();

        HashMap<Integer, Task> removedTasks = removeTask(1);
        HashMap<Integer, Epic> removedEpics = removeEpic(3);
        HashMap<Integer, Subtask> removedSubtasks = removeSubtask(5);

        for (Integer task : listTasks.keySet()) {
            System.out.println(task + " " + listTasks.get(task));
        }
        System.out.println();
        for (Integer epic : listEpics.keySet()) {
            System.out.println(epic + " " + listEpics.get(epic));
        }
        System.out.println();
        for (Integer subtask : listSubtasks.keySet()) {
            System.out.println(subtask + " " + listSubtasks.get(subtask));
        }
        System.out.println();

        clearSubtasks();

        if (listSubtasks.isEmpty()) {
            System.out.println("Все подзадачи удалены");
        }
        for (Integer subtask : listSubtasks.keySet()) {
            System.out.println(subtask + " " + listSubtasks.get(subtask));
        }
        System.out.println();
    }
}
