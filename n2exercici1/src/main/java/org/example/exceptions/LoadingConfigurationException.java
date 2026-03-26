package org.example.exceptions;

public class LoadingConfigurationException extends RuntimeException {
    public LoadingConfigurationException(String message) {
        super(message);
    }
}
