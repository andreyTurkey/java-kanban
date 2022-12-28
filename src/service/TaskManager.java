package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    public int addNewTask(Task task);

    public int updatingTask(Task task);

    public int addNewEpic(Epic epic);

    public int updatingEpic(Epic epic);

    public int addNewSubtaskAndUpdate(Subtask subtask);

    public void updateStatus(Epic epic);

    // Методы получение списка задач
    public ArrayList<Task> getListTask();

    public ArrayList<Epic> getListEpic();

    public ArrayList<Subtask> getListSubtask();

    // Методы очистки хэш мапы
    public HashMap<Integer, Task> clearTasks();

    public HashMap<Integer, Epic> clearEpics();

    public HashMap<Integer, Subtask> clearSubtasks();

    // Методы получения по ID
    public Task getWithIdTask(int id);

    public Epic getWithIdEpics(int id);

    public Subtask getWithIdSubtasks(int id);

    // Методы удаления задач по ID
    public HashMap<Integer, Task> removeTask(int idRemovedTask);

    public HashMap<Integer, Epic> removeEpic(int idRemovedEpic);

    public HashMap<Integer, Subtask> removeSubtask(int idRemovedSubtasks);

    public void printTask();

    public void printEpic();

    public void printSubtask();

    public void printHistory();
}


