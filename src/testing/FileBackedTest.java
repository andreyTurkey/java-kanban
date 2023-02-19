package testing;

import org.junit.jupiter.api.Test;
import model.Epic;
import model.Subtask;
import model.Task;

import service.FileBackedTasksManager;
import service.InMemoryTaskManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTest extends TaskManagerTest<InMemoryTaskManager> {

    private static String pathFile = "HistoryLoadTest.csv";
    private static String pathEmptyFile = "HistoryEptyTest.csv";

    @Test
    public void loadFromFile() throws IOException {
        try {
            FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File(pathFile));

            List<Task> taskHistory = new ArrayList<>();
            taskHistory.addAll(fileBackedTasksManager.getListTask());
            taskHistory.addAll(fileBackedTasksManager.getListEpic());
            taskHistory.addAll(fileBackedTasksManager.getListSubtask());

            assertEquals(8, taskHistory.size(), "Количество задач не совпадает с файлом.");

            String str = fileBackedTasksManager.stringHistory();

            assertEquals("3,1,2,4,5,6,7,8", str, "Количество задач не совпадает с файлом.");

        } catch (NumberFormatException exc) {
            System.out.println(exc.getMessage());
        }
    }

    @Test
    public void loadFromEmptyFile() throws IOException {
        try {
            FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File(pathEmptyFile));
        } catch (NumberFormatException exc) {
            System.out.println(exc.getMessage());
        } catch (IndexOutOfBoundsException exc) {
            System.out.println(exc.getMessage());
        }
    }

    @Test
    public void save() throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();

        fileBackedTasksManager.save();

        List<Task> tasksFromHistory = fileBackedTasksManager.getTasksFromHistory();

        assertEquals(0, tasksFromHistory.size(), "Сохранение менеджера некорректно.");

        fileBackedTasksManager.addNewTask(new Task("Задача Один", "Описание"));

        fileBackedTasksManager.addNewEpic(new Epic("Эпик Задача Один", "Описание"));

        fileBackedTasksManager.getWithIdTask(1);
        fileBackedTasksManager.getWithIdEpics(2);

        fileBackedTasksManager.save();

        List<Task> secondTasksFromHistory = fileBackedTasksManager.getTasksFromHistory();

        assertEquals(2, secondTasksFromHistory.size(), "Сохранение менеджера некорректно.");

        fileBackedTasksManager.addSubtask(new Subtask("Подзадача один", "Описание"));
        fileBackedTasksManager.addSubtask(new Subtask("Подзадача два", "Описание"));

        fileBackedTasksManager.getWithIdSubtasks(3);
        fileBackedTasksManager.getWithIdSubtasks(4);

        fileBackedTasksManager.save();

        List<Task> fourthTasksFromHistory = fileBackedTasksManager.getTasksFromHistory();

        assertEquals(4, fourthTasksFromHistory.size(), "Сохранение менеджера некорректно.");
    }
}
