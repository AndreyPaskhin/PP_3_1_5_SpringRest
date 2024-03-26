package ru.kata.spring.boot_security.demo.exception;

import java.util.HashMap;
import java.util.Map;

public class UserIncorectData {
    private String info;
    private Map<String, String> fieldErrors;

    public UserIncorectData() {
        fieldErrors = new HashMap<>();
    }
    public void addError(String fieldName, String errorMessage) {
        fieldErrors.put(fieldName, errorMessage);
    }
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
