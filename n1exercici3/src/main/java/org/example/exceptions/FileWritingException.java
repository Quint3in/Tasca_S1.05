package org.example.exceptions;

public class FileWritingException extends RuntimeException {
    public FileWritingException(String message) {
        super(message);
    }
}
