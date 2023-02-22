package testing;

import service.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    void setTaskManager() {
        taskManager = new InMemoryTaskManager();
    }
}
