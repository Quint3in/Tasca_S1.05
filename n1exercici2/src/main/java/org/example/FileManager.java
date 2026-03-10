package org.example;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public List<String> listRecursivelyAlphabetically() {
        List<String> list = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        listRecursivelyAlphabetically(file, list, fmt, "");

        return list;
    }
    private void listRecursivelyAlphabetically(File current, List<String> out, SimpleDateFormat fmt, String prefix) {
        if (current == null || !current.exists()) {
            return;
        }

        String type = current.isDirectory() ? "(D)" : "(F)";
        String date = " | " + fmt.format(current.lastModified());
        out.add(prefix + type + " " + current.getName() + date);

        if (!current.isDirectory()) {
            return;
        }
        File[] children = current.listFiles();
        if(children == null || children.length == 0) {
            return;
        }
        Arrays.sort(children, Comparator.comparing(File::getName));
        for (File child : children) {
            listRecursivelyAlphabetically(child, out, fmt, prefix + "  ");
        }
    }
}
