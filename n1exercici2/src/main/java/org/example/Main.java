package org.example;

public class Main {
    public static void main(String[] args) {
        FileManager a = new FileManager(".\\");

        for (String name : a.listRecursivelyAlphabetically()) {
            System.out.println(name);
        }
    }
}
