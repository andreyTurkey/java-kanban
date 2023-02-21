package testing;

import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    void setTaskManager(){
        taskManager = new InMemoryTaskManager();
    }
}
