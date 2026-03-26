package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FileManagerTest {
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

    @Test
    void listRecursivelyAlphabetically_listsTreeInOrderWithPrefixAndType() throws IOException {
        Path alpha = Files.createDirectory(tempDir.resolve("alpha"));
        Files.createFile(alpha.resolve("b.txt"));
        Files.createFile(alpha.resolve("a.txt"));
        Files.createDirectory(tempDir.resolve("beta"));
        Files.createFile(tempDir.resolve("root.txt"));

        FileManager fileManager = new FileManager(tempDir.toString());

        List<String> result = fileManager.listRecursivelyAlphabetically();

        assertThat(result).hasSize(6);
        assertThat(result.get(0)).startsWith("(D) " + tempDir.getFileName()).contains(" | ");
        assertThat(result.get(1)).startsWith("  (D) alpha").contains(" | ");
        assertThat(result.get(2)).startsWith("    (F) a.txt").contains(" | ");
        assertThat(result.get(3)).startsWith("    (F) b.txt").contains(" | ");
        assertThat(result.get(4)).startsWith("  (D) beta").contains(" | ");
        assertThat(result.get(5)).startsWith("  (F) root.txt").contains(" | ");
    }

    @Test
    void listRecursivelyAlphabeticallyIntoFile_writesOutputFile() throws IOException {
        Path alpha = Files.createDirectory(tempDir.resolve("alpha"));
        Files.createFile(alpha.resolve("b.txt"));
        Files.createFile(alpha.resolve("a.txt"));
        Files.createDirectory(tempDir.resolve("beta"));
        Files.createFile(tempDir.resolve("root.txt"));

        FileManager fileManager = new FileManager(tempDir.toString());
        Path output = Files.createTempFile("file-manager-output-", ".txt");

        fileManager.listRecursivelyAlphabeticallyIntoFile(output.toString());

        assertThat(Files.exists(output)).isTrue();
        assertThat(Files.readAllLines(output)).containsExactlyElementsOf(
                fileManager.listRecursivelyAlphabetically()
        );
        Files.deleteIfExists(output);
    }

}
