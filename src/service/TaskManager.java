package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    public void addNewTask(Task task) throws IOException;

    public void updateTask(Task task) throws IOException;

    public void addNewEpic(Epic epic) throws IOException;

    public void updateEpic(Epic epic) throws IOException;

    public void addSubtask(Subtask subtask) throws IOException;

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
    public Task getWithIdTask(int id) throws IOException;

    public Epic getWithIdEpics(int id) throws IOException;

    public Subtask getWithIdSubtasks(int id) throws IOException;

    // Методы удаления задач по ID
    public HashMap<Integer, Task> removeTask(int idRemovedTask);

    public HashMap<Integer, Epic> removeEpic(int idRemovedEpic);

    public HashMap<Integer, Subtask> removeSubtask(int idRemovedSubtasks);

    public void printTask();

    public void printEpic();

    public void printSubtask();

    public void printHistory();

    public void printMap();

    public void taskFromFile(Task task);

    public void epicFromFile(Epic epic);

    public void subtaskFromFile(Subtask subtask);

}


