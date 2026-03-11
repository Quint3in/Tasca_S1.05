package org.example;

public class Main {
    public static void main(String[] args) {
        FileManager a = new FileManager(".\\");
        String path = "n3exercici1/";
        Person p = new Person("Maria",25);

        String password = "12345";

        String outputFile = path + "/salida.txt";
        String encryptedOutputFile = outputFile + ".enc";
        String decryptedOutputFile = path + "/salidaDecrypted.txt";
        a.listRecursivelyAlphabeticallyIntoFile(outputFile);
        FileManager.encryptFile(outputFile,encryptedOutputFile,password);
        FileManager.decryptFile(encryptedOutputFile,decryptedOutputFile,password);

        FileManager.serializePersonToFile(p, path);
        String basePersonFile = path + "/" + p.getName() + ".ser";
        String encryptedPersonFile = basePersonFile + ".enc";
        String decryptedPersonFile = path + "/" + p.getName() + ".dec.ser";
        FileManager.encryptFile(basePersonFile, encryptedPersonFile, password);
        FileManager.decryptFile(encryptedPersonFile, decryptedPersonFile, password);
        System.out.println(FileManager.deserializePersonFromFile(decryptedPersonFile));




    }
}
