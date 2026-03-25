package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FileManagerTest {

    @TempDir
    Path tempDir;

    @Test
    public void listAlphabetically_sortsCaseInsensitive() throws IOException {
        Files.createFile(tempDir.resolve("b.txt"));
        Files.createFile(tempDir.resolve("A.txt"));
        Files.createFile(tempDir.resolve("c.txt"));

        FileManager fileManager = new FileManager(tempDir.toString());

        List<String> result = fileManager.listAlphabetically();

        assertThat(result).containsExactly("A.txt", "b.txt", "c.txt");
    }

    @Test
    public void listRecursivelyAlphabetically_listsTreeInOrderWithPrefixAndType() throws IOException {
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
    public void listRecursivelyAlphabeticallyIntoFile_writesOutputFile() throws IOException {
        Path alpha = Files.createDirectory(tempDir.resolve("alpha"));
        Files.createFile(alpha.resolve("b.txt"));
        Files.createFile(alpha.resolve("a.txt"));
        Files.createDirectory(tempDir.resolve("beta"));
        Files.createFile(tempDir.resolve("root.txt"));

        FileManager fileManager = new FileManager(tempDir.toString());
        Path output = Files.createTempFile("file-manager-output-", ".txt");

        boolean wrote = fileManager.listRecursivelyAlphabeticallyIntoFile(output.toString());

        assertThat(wrote).isTrue();
        assertThat(Files.exists(output)).isTrue();
        assertThat(Files.readAllLines(output)).containsExactlyElementsOf(
                fileManager.listRecursivelyAlphabetically()
        );
        Files.deleteIfExists(output);
    }

    @Test
    public void readFile_printsFileContent() throws IOException {
        Path input = Files.createTempFile("file-manager-input-", ".txt");
        List<String> lines = List.of("first line", "second line");
        Files.write(input, lines);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(buffer));
        try {
            FileManager.readFile(input.toString());
        } finally {
            System.setOut(originalOut);
            Files.deleteIfExists(input);
        }

        String output = buffer.toString();
        assertThat(output).isEqualTo(String.join(System.lineSeparator(), lines) + System.lineSeparator());
    }

    @Test
    public void serializePersonToFile_writesSerializedFile() {
        Person person = new Person("Maria", 20);

        boolean wrote = FileManager.serializePersonToFile(person, tempDir.toString());

        assertThat(wrote).isTrue();
        assertThat(Files.exists(tempDir.resolve("Maria.ser"))).isTrue();
    }

    @Test
    public void deserializePersonFromFile_readsSerializedPerson() {
        Person person = new Person("Alejandro", 25);
        FileManager.serializePersonToFile(person, tempDir.toString());
        Path input = tempDir.resolve("Alejandro.ser");

        Person result = FileManager.deserializePersonFromFile(input.toString());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Alejandro");
        assertThat(result.getAge()).isEqualTo(25);
    }

    @Test
    public void encryptFile_writesEncryptedFile() throws IOException {
        Path input = tempDir.resolve("plain.txt");
        Files.writeString(input, "secret content");
        Path encrypted = tempDir.resolve("plain.txt.enc");

        boolean encryptedOk = FileManager.encryptFile(input.toString(), encrypted.toString(), "123");

        assertThat(encryptedOk).isTrue();
        assertThat(Files.exists(encrypted)).isTrue();
        assertThat(Files.size(encrypted)).isGreaterThan(0);
        assertThat(Files.readAllBytes(encrypted)).isNotEqualTo(Files.readAllBytes(input));
    }

    @Test
    public void decryptFile_restoresOriginalContent() throws IOException {
        Path input = tempDir.resolve("plain.txt");
        Files.writeString(input, "secret content");
        Path encrypted = tempDir.resolve("plain.txt.enc");
        Path decrypted = tempDir.resolve("plainDecrypted.txt");

        boolean encryptedOk = FileManager.encryptFile(input.toString(), encrypted.toString(), "123");
        boolean decryptedOk = FileManager.decryptFile(encrypted.toString(), decrypted.toString(), "123");

        assertThat(encryptedOk).isTrue();
        assertThat(decryptedOk).isTrue();
        assertThat(Files.readString(decrypted)).isEqualTo(Files.readString(input));
    }
}
