package org.example;

public class Main {
    static void main() {
        FileManager a = new FileManager(".\\");
        String outputFile = "n1exercici3/salida.txt";
        //a.listAlphabetically().forEach(System.out::println);

        //a.listRecursivelyAlphabetically().forEach(System.out::println);

        a.listRecursivelyAlphabeticallyIntoFile(outputFile);
    }
}
