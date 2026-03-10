package org.example;

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

    public boolean listRecursivelyAlphabeticallyIntoFile(String output) {
        try {
            Files.write(Paths.get(output), listRecursivelyAlphabetically(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException ex) {
            System.out.println("Error while writing to file: " + ex.getMessage());
        }
        return false;
    }

    public static void readFile(String inputFile) {
        try (Stream<String> lines = Files.lines(Paths.get(inputFile), StandardCharsets.UTF_8)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }
    }

    public static boolean serializePersonToFile(Person p, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(path + "/" + p.getName()+".ser"))) {
            oos.writeObject(p);
            return true;
        } catch (IOException ex)  {
            System.out.println("Error while serializing person: " + ex.getMessage());
        }
        return false;
    }

    public static Person deserializePersonFromFile(String inputFile) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(inputFile))) {
            return (Person) ois.readObject();
        }catch (IOException ex)  {
            System.out.println("Error while deserializing person: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
