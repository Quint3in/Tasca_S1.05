package org.example;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.example.exceptions.LoadingConfigurationException;

public class Main {
    public static void main(String[] args) {
        Configuration config;
        try {
            Configurations configs = new Configurations();
            config = configs.properties("n2exercici1/app.properties");
        } catch (ConfigurationException e) {
            throw new LoadingConfigurationException("Cannot load configuration");
        }
        String inputDir = config.getString("input.dir");
        String outputFile = config.getString("output.file");

        System.out.println("Input dir: " + inputDir);
        System.out.println("Output file: " + outputFile);
        FileManager a = new FileManager(inputDir);

        a.listRecursivelyAlphabeticallyIntoFile(outputFile);
    }
}
