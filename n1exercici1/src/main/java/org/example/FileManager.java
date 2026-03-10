package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileManager {

    private File file;

    public FileManager(String fileName) {
        this.file = new File(fileName);
    }

    public List<String> listAlphabetically() {
        List<String> list = new ArrayList<>(List.of(Objects.requireNonNull(file.list())));
        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;
    }
}
