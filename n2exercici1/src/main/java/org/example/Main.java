package org.example;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class Main {
    static void main() {
        Configuration config = null;
        try {
            Configurations configs = new Configurations();
            config = configs.properties("n2exercici1/app.properties");
        } catch (ConfigurationException e) {
            System.err.println("Cannot load configuration: " + e.getMessage());
            System.exit(1);
        }
        String inputDir = config.getString("input.dir");
        String outputFile = config.getString("output.file");

        System.out.println("Input dir: " + inputDir);
        System.out.println("Output file: " + outputFile);
        FileManager a = new FileManager(inputDir);
        //a.listAlphabetically().forEach(System.out::println);

        //a.listRecursivelyAlphabetically().forEach(System.out::println);

        a.listRecursivelyAlphabeticallyIntoFile(outputFile);
    }
}
