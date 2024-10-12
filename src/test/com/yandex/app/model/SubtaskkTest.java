package test.com.yandex.app.model;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.Test;
import test.com.yandex.app.utils.TestUtils;

public class SubtaskkTest {
    public final TaskManager taskManager = Managers.getDefault();

    @Test
    void addNewEpicAndSubtask() {
        Epic epic = new Epic("Епик 1", "Описание 1");
        final int epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        TestUtils.checkTask(epic, savedEpic, taskManager.getEpics());

        Subtask subtask = new Subtask("Сабтаск 1", "Описание Сабтаск 1", epicId);
        final int subtaskId = taskManager.addSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        TestUtils.checkTask(subtask, savedSubtask, taskManager.getSubtasks());
    }

}
