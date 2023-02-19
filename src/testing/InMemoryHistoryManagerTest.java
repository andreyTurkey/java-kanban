package testing;

import model.Task;

import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    private final HistoryManager inHistoryManager = Managers.getDefaultHistory();

    @Test
    public void add() {
        Task task = new Task("Test add", "Test add description");
        inHistoryManager.add(task);

        List<Task> tasks = inHistoryManager.getHistory();

        assertNotNull(tasks.size(), "История задач пустая.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void getHistory() {
        Task task = new Task("Test add", "Test add description");
        inHistoryManager.add(task);
        inHistoryManager.add(task);
        inHistoryManager.add(task);

        List<Task> tasks = inHistoryManager.getHistory();

        assertNotNull(tasks.size(), "История задач пустая.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void remove() {
        Task task1 = new Task("Test1 add", "Test add description");
        task1.setId(1);
        inHistoryManager.add(task1);
        Task task2 = new Task("Test2 add", "Test add description");
        task2.setId(2);
        inHistoryManager.add(task2);
        Task task3 = new Task("Test3 add", "Test add description");
        task3.setId(3);
        inHistoryManager.add(task3);
        Task task4 = new Task("Test4 add", "Test add description");
        task4.setId(4);
        inHistoryManager.add(task4);

        inHistoryManager.remove(1);

        List<Task> firstTasks = inHistoryManager.getHistory();

        assertEquals(task2, firstTasks.get(0), "Удаление задачи из начала работает неправильно");

        inHistoryManager.remove(3);

        List<Task> secondTasks = inHistoryManager.getHistory();

        assertEquals(task2, secondTasks.get(0), "Удаление задачи из середины работает неправильно");

        inHistoryManager.remove(4);

        List<Task> fourthTasks = inHistoryManager.getHistory();

        assertEquals(task2, fourthTasks.get(0), "Удаление задачи с конца работает неправильно");
    }
}
