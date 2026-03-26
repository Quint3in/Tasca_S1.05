package org.example;

public class Main {
    public static void main(String[] args) {
        FileManager a = new FileManager(".\\");
        String outputFile = "n1exercici3/salida.txt";

        a.listRecursivelyAlphabeticallyIntoFile(outputFile);
    }
}
