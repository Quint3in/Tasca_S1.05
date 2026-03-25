package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void listAlphabetically_sortsCaseInsensitive() throws IOException {
        Files.createFile(tempDir.resolve("b.txt"));
        Files.createFile(tempDir.resolve("A.txt"));
        Files.createFile(tempDir.resolve("c.txt"));

        FileManager fileManager = new FileManager(tempDir.toString());

        List<String> result = fileManager.listAlphabetically();

        assertThat(result).containsExactly("A.txt", "b.txt", "c.txt");
    }
}
