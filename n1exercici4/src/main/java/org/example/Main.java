package org.example;

public class Main {
    static void main() {
        FileManager a = new FileManager(".\\");
        String inputFile = "n1exercici4/input.txt";
        //a.listAlphabetically().forEach(System.out::println);

        //a.listRecursivelyAlphabetically().forEach(System.out::println);

        //a.listRecursivelyAlphabeticallyIntoFile(outputFile);

        FileManager.readFile(inputFile);
    }
}
