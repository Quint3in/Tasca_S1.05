package org.example;

public class Main {
    static void main() {
        FileManager a = new FileManager(".\\");
        a.listAlphabetically().forEach(System.out::println);
    }
}
