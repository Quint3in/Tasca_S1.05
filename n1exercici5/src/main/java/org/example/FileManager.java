package org.example;

import org.example.exceptions.FileReadingException;
import org.example.exceptions.FileWritingException;
import org.example.exceptions.ReadSerializedFileException;
import org.example.exceptions.SerializePersonIntoFileException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

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
        formatEntry(current, out, prefix, fmt);
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
    private void formatEntry(File current,List<String> out, String prefix, SimpleDateFormat fmt) {
        String type = current.isDirectory() ? "(D)" : "(F)";
        String date = " | " + fmt.format(current.lastModified());
        out.add(prefix + type + " " + current.getName() + date);
    }

    public void listRecursivelyAlphabeticallyIntoFile(String output) {
        try {
            Files.write(Paths.get(output), listRecursivelyAlphabetically(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            throw new FileWritingException("Error while writing to file.");
        }
    }

    public static void readFile(String inputFile) {
        try (Stream<String> lines = Files.lines(Paths.get(inputFile), StandardCharsets.UTF_8)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            throw new FileReadingException("Error while writing to file.");
        }
    }

    public static void serializePersonToFile(Person p, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(path + "/" + p.getName()+".ser"))) {
            oos.writeObject(p);
        } catch (IOException ex)  {
            throw new SerializePersonIntoFileException("Error while serializing person");
        }
    }

    public static Person deserializePersonFromFile(String inputFile) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(inputFile))) {
            return (Person) ois.readObject();
        } catch (IOException | ClassNotFoundException ex)  {
            throw new ReadSerializedFileException("Error while deserializing person");
        }
    }

}
