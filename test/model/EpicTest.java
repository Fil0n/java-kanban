package model;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertNull;

public class EpicTest {
    public final TaskManager taskManager = Managers.getDefault();

    @Test
    void addNewEpicAndSubtask() {
        Epic epic = new Epic("Епик 1", "Описание 1");
        final int epicId = taskManager.addEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        TestUtils.checkTask(epic, savedEpic, taskManager.getEpics());
    }

    @Test
    void addSubtaskAsEpic() {
        Epic epic = new Epic("Епик 1", "Описание 1");
        final Integer epicId = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Сабтаск 1", "Описание 1", epicId);
        final Integer subtask1Id = taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск 2", "Описание 2", subtask1Id);
        final Integer subtask2Id = taskManager.addSubtask(subtask2);
        assertNull(subtask2Id, "Cабтаск может быть своим эпиком.");
    }
}
