package org.example;

public class Main {
    static void main() {
        FileManager a = new FileManager(".\\");
        String path = "n1exercici5/";
        Person p = new Person("Maria",25);
        //a.listAlphabetically().forEach(System.out::println);

        //a.listRecursivelyAlphabetically().forEach(System.out::println);

        //a.listRecursivelyAlphabeticallyIntoFile(outputFile);

        //FileManager.readFile(inputFile);

        FileManager.serializePersonToFile(p,path);
        System.out.println(FileManager.deserializePersonFromFile(path+"/"+p.getName()+".ser"));




    }
}
