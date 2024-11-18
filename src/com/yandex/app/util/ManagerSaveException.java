package com.yandex.app.util;

import java.io.IOException;

public class ManagerSaveException extends IOException {
    public ManagerSaveException(IOException message) {
        super(message);
    }
}
