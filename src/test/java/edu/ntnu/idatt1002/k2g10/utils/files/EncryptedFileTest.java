package edu.ntnu.idatt1002.k2g10.utils.files;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.idatt1002.k2g10.utils.crypto.EncryptionException;
import edu.ntnu.idatt1002.k2g10.utils.crypto.FileEncryptionAlgorithm;
import java.io.*;
import java.util.Scanner;

import edu.ntnu.idatt1002.k2g10.utils.crypto.IncorrectPasswordException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test encrypted files")
public class EncryptedFileTest {
    private final String path = String.format("src/test/java/%s/test.txt",
            this.getClass().getPackageName().replace(".", "/"));
    private final String string = "This is my test string <3";
    private final String passwordA = "password";
    private final String passwordB = "dorwssap";
    private EncryptedFile file;

    @BeforeEach
    void setupTest() throws IOException {
        file = new EncryptedFile(path);

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(string);
        }
    }

    @AfterEach
    void cleanupTest() {
        if (file.exists()) {
            assertTrue(file.delete());
        }

        File keys = new File(file.getPath().concat(FileEncryptionAlgorithm.SALT_IV_EXTENSION));
        if (keys.exists()) {
            assertTrue(keys.delete());
        }

        File enc = new File(file.getPath().concat(FileEncryptionAlgorithm.ENCRYPTED_EXTENSION));
        if (enc.exists()) {
            assertTrue(enc.delete());
        }
    }

    @Test
    @DisplayName("File encrypts successfully")
    void testFileEncryptsSuccessfully() {
        assertDoesNotThrow(() -> file.encrypt(passwordA));
    }

    @Test
    @DisplayName("File decrypts successfully with correct password")
    void testFileDecryptsSuccessfullyWithCorrectPassword() throws IOException, EncryptionException {
        file.encrypt(passwordA);

        assertDoesNotThrow(() -> file.decrypt(passwordA, true));
    }

    @Test
    @DisplayName("File decrypts successfully with correct password after failed decryption")
    void testFileDecryptsSuccessfullyWithCorrectPasswordAfterFailedDecryption()
            throws IOException, EncryptionException {
        file.encrypt(passwordA);

        assertThrows(IncorrectPasswordException.class, () -> file.decrypt(passwordB, true));
        assertDoesNotThrow(() -> file.decrypt(passwordA, true));
    }

    @Test
    @DisplayName("File does not decrypt with incorrect password")
    void testFileDoesNotDecryptWithIncorrectPassword() throws IOException, EncryptionException {
        file.encrypt(passwordA);

        assertThrows(IncorrectPasswordException.class, () -> file.decrypt(passwordB, true));
    }

    @Test
    @DisplayName("File content is the same after being encrypted and decrypted")
    void testObjectIsTheSameAfterBeingWrittenAndRead()
            throws IOException, EncryptionException, IncorrectPasswordException {
        file.encrypt(passwordA);
        file.decrypt(passwordA, true);

        String content;
        try (Scanner scanner = new Scanner(file)) {
            content = scanner.nextLine();
        }

        assertEquals(string, content);
    }
}
