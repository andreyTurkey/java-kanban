package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;

import java.util.*;

public interface TaskManager {

    public Map<Integer, Task> getTasksStorages();

    public Map<Integer, Epic> getEpicsStorage();

    public Map<Integer, Subtask> getSubtaksStorage();

    public void timeValidator(Task newTask);

    public Set<Task> getPrioritizedTasks();

    public void addNewTask(Task task) throws IOException;

    public void updateTask(Task task) throws IOException;

    public void addNewEpic(Epic epic) throws IOException;

    public void updateEpic(Epic epic) throws IOException;

    public void addSubtask(Subtask subtask) throws IOException;

    public void updateStatus(Epic epic);

    // Методы получение списка задач
    public List<Task> getListTask();

    public List<Epic> getListEpic();

    public List<Subtask> getListSubtask();

    // Методы очистки хэш мапы
    public Map<Integer, Task> clearTasks() throws IOException;

    public Map<Integer, Epic> clearEpics() throws IOException;

    public Map<Integer, Subtask> clearSubtasks() throws IOException;

    // Методы получения по ID
    public Task getWithIdTask(int id) throws IOException;

    public Epic getWithIdEpics(int id) throws IOException;

    public Subtask getWithIdSubtasks(int id) throws IOException;

    // Методы удаления задач по ID
    public Map<Integer, Task> removeTask(int idRemovedTask) throws IOException;

    public Map<Integer, Epic> removeEpic(int idRemovedEpic) throws IOException;

    public Map<Integer, Subtask> removeSubtask(int idRemovedSubtasks) throws IOException;

    public void printTask();

    public void printEpic();

    public void printSubtask();

    public void printHistory();

    public void printMap();

    public void taskFromFile(Task task);

    public void epicFromFile(Epic epic);

    public void subtaskFromFile(Subtask subtask);
}


