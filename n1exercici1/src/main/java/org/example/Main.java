package org.example;

public class Main {
    public static void main(String[] args) {
        FileManager a = new FileManager(".\\");
        a.listAlphabetically().forEach(System.out::println);
    }
}
