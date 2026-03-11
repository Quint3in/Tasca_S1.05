package org.example;

import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.cipher.CryptoCipherFactory;
import org.apache.commons.crypto.utils.Utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileManager {

    private final File file;
    private static final int AES_KEY_BYTES = 16;
    private static final int AES_BLOCK_BYTES = 16;
    private static final int BUFFER_SIZE = 4096;
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public FileManager(String fileName) {
        this.file = new File(fileName);
    }

    public List<String> listAlphabetically() {
        List<String> list = new ArrayList<>(List.of(Objects.requireNonNull(file.list())));
        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    public List<String> listRecursivelyAlphabetically() {
        List<String> list = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        listRecursivelyAlphabetically(file, list, fmt, "");

        return list;
    }
    private void listRecursivelyAlphabetically(File current, List<String> out, SimpleDateFormat fmt, String prefix) {
        if (current == null || !current.exists()) {
            return;
        }

        String type = current.isDirectory() ? "(D)" : "(F)";
        String date = " | " + fmt.format(current.lastModified());
        out.add(prefix + type + " " + current.getName() + date);

        if (!current.isDirectory()) {
            return;
        }
        File[] children = current.listFiles();
        if(children == null || children.length == 0) {
            return;
        }
        Arrays.sort(children, Comparator.comparing(File::getName));
        for (File child : children) {
            listRecursivelyAlphabetically(child, out, fmt, prefix + "  ");
        }
    }

    public boolean listRecursivelyAlphabeticallyIntoFile(String output) {
        try {
            Files.write(Paths.get(output), listRecursivelyAlphabetically(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException ex) {
            System.out.println("Error while writing to file: " + ex.getMessage());
        }
        return false;
    }

    public static void readFile(String inputFile) {
        try (Stream<String> lines = Files.lines(Paths.get(inputFile), StandardCharsets.UTF_8)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }
    }

    public static boolean serializePersonToFile(Person p, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(path + "/" + p.getName()+".ser"))) {
            oos.writeObject(p);
            return true;
        } catch (IOException ex)  {
            System.out.println("Error while serializing person: " + ex.getMessage());
        }
        return false;
    }

    public static Person deserializePersonFromFile(String inputFile) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(inputFile))) {
            return (Person) ois.readObject();
        }catch (IOException | ClassNotFoundException ex)  {
            System.out.println("Error while deserializing person: " + ex.getMessage());
        }
        return null;
    }

    public static boolean encryptFile(String inputFile, String outputFile, String password) {
        return processFile(Cipher.ENCRYPT_MODE, inputFile, outputFile, password);
    }

    public static boolean decryptFile(String inputFile, String outputFile, String password) {
        return processFile(Cipher.DECRYPT_MODE, inputFile, outputFile, password);
    }

    private static boolean processFile(int mode, String inputFile, String outputFile, String password) {
        Properties properties = new Properties();
        properties.setProperty(CryptoCipherFactory.CLASSES_KEY,
                CryptoCipherFactory.CipherProvider.JCE.getClassName());

        byte[] key = deriveKey(password);

        try (CryptoCipher cipher = Utils.getCipherInstance(TRANSFORMATION, properties);
             FileInputStream in = new FileInputStream(inputFile);
             FileOutputStream out = new FileOutputStream(outputFile)) {

            byte[] iv;
            if (mode == Cipher.ENCRYPT_MODE) {
                iv = randomIv();
                out.write(iv);
            } else {
                iv = readIv(in);
            }

            cipher.init(mode, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            processStream(cipher, in, out);
            return true;
        } catch (IOException | GeneralSecurityException ex) {
            String action = mode == Cipher.ENCRYPT_MODE ? "encrypting" : "decrypting";
            System.out.println("Error while " + action + " file: " + ex.getMessage());
        }
        return false;
    }

    private static void processStream(CryptoCipher cipher, InputStream in, OutputStream out)
            throws IOException, GeneralSecurityException {
        byte[] inBuffer = new byte[BUFFER_SIZE];
        byte[] outBuffer = new byte[BUFFER_SIZE + AES_BLOCK_BYTES];
        int n;
        while ((n = in.read(inBuffer)) != -1) {
            ByteBuffer inBb = ByteBuffer.wrap(inBuffer, 0, n);
            ByteBuffer outBb = ByteBuffer.wrap(outBuffer);
            int outLen = cipher.update(inBb, outBb);
            if (outLen > 0) {
                out.write(outBuffer, 0, outLen);
            }
        }
        ByteBuffer outBb = ByteBuffer.wrap(outBuffer);
        int outLen = cipher.doFinal(ByteBuffer.allocate(0), outBb);
        if (outLen > 0) {
            out.write(outBuffer, 0, outLen);
        }
    }

    private static byte[] deriveKey(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Arrays.copyOf(hash, AES_KEY_BYTES);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Unable to derive key: " + ex.getMessage(), ex);
        }
    }

    private static byte[] randomIv() {
        byte[] iv = new byte[AES_BLOCK_BYTES];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private static byte[] readIv(InputStream in) throws IOException {
        byte[] iv = new byte[AES_BLOCK_BYTES];
        int total = 0;
        while (total < AES_BLOCK_BYTES) {
            int read = in.read(iv, total, AES_BLOCK_BYTES - total);
            if (read == -1) {
                throw new EOFException("Missing IV bytes in encrypted file.");
            }
            total += read;
        }
        return iv;
    }

}
