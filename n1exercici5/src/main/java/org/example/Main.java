package org.example;

public class Main {
    public static void main(String[] args) {
        FileManager a = new FileManager(".\\");
        String path = "n1exercici5/";
        Person p = new Person("Maria",25);

        FileManager.serializePersonToFile(p,path);
        System.out.println(FileManager.deserializePersonFromFile(path+"/"+p.getName()+".ser"));
    }
}
