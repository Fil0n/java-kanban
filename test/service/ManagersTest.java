package service;

import com.yandex.app.service.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {
    @Test
    void checkManadgers() {
        assertNotNull(Managers.getDefault(), "Менеджер тасков не загружен");
        assertNotNull(Managers.getDefaultHistory(), "Менеджер истории не загружен");
    }
}
