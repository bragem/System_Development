package edu.ntnu.idatt1002.k2g10.utils.crypto;

import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Used to securely encrypt files with a password.
 *
 * @author trthingnes
 */
public class FileEncryptionAlgorithm {
    // Encryption algorithm presets
    public static final FileEncryptionAlgorithm AES_CBC_PKCS5P = new FileEncryptionAlgorithm("AES/CBC/PKCS5Padding",
            "AES", PasswordHashAlgorithm.PBKDF2);

    public static final String ENCRYPTED_EXTENSION = ".encrypted";
    public static final String SALT_IV_EXTENSION = ".encrypted.keys";

    private final String algorithm;
    private final String keyAlgorithm;
    private final PasswordHashAlgorithm hashAlgorithm;

    /**
     * Creates a new {@link FileEncryptionAlgorithm} instance.
     *
     * @param algorithm
     *            Name of the {@link Cipher} algorithm to use for encryption
     * @param keyAlgorithm
     *            Name of the {@link SecretKeySpec} algorithm to use for key generation
     * @param hashAlgorithm
     *            Hash algorithm instance to use for hashing password
     * 
     * @see PasswordHashAlgorithm
     * 
     * @author trthingnes
     */
    public FileEncryptionAlgorithm(String algorithm, String keyAlgorithm, PasswordHashAlgorithm hashAlgorithm) {
        this.algorithm = algorithm;
        this.keyAlgorithm = keyAlgorithm;
        this.hashAlgorithm = hashAlgorithm;
    }

    /**
     * Encrypts the given file and saves the result in a new file in the same directory with a .encrypted extension.
     *
     * @param file
     *            File to encrypt
     * @param password
     *            Password to use for encryption
     * 
     * @throws EncryptionException
     *             If a problem is encountered while encrypting the given file.
     * 
     * @author trthingnes
     */
    public void encryptFile(File file, String password) throws EncryptionException {
        try {
            // Salt and hash password to use for encryption.
            String salt = hashAlgorithm.generateSalt();
            String hash = hashAlgorithm.getSaltedHash(password, salt);
            SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(hash), keyAlgorithm);
            IvParameterSpec iv = generateIv();

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            // Write salt and IV to use for decryption.
            try (FileWriter writer = new FileWriter(file.getPath().concat(SALT_IV_EXTENSION))) {
                writer.write(salt);
                writer.write("\n");
                writer.write(Base64.getEncoder().encodeToString(iv.getIV()));
            }

            runCipherOnFiles(cipher, file, new File(file.getPath().concat(ENCRYPTED_EXTENSION)));
        } catch (Exception e) {
            throw new EncryptionException(
                    String.format("A %s was caught with the message \"%s\"", e.getClass(), e.getMessage()));
        }
    }

    /**
     * Decrypts the given file and saves the result in a new file in the same directory without a .encrypted extension.
     *
     * @param file
     *            File to decrypt
     * @param password
     *            Password to use for decryption
     * 
     * @throws EncryptionException
     *             If a problem is encountered while decrypting the given file.
     *
     * @throws IncorrectPasswordException
     *             If the given password cannot be used to decrypt the file.
     * 
     * @author trthingnes
     */
    public void decryptFile(File file, String password) throws EncryptionException, IncorrectPasswordException {
        // If the given path is to unencrypted file, add encrypted extension to it.
        if (!file.getPath().contains(ENCRYPTED_EXTENSION)) {
            file = new File(file.getPath().concat(ENCRYPTED_EXTENSION));
        }

        try {
            String salt;
            IvParameterSpec iv;

            // Read salt and IV to use for decryption.
            try (Scanner reader = new Scanner(
                    new File(file.getPath().replace(ENCRYPTED_EXTENSION, "").concat(SALT_IV_EXTENSION)))) {
                salt = reader.nextLine();
                iv = new IvParameterSpec(Base64.getDecoder().decode(reader.nextLine()));
            }

            // Hash password and salt and use key for decryption.
            Cipher cipher = Cipher.getInstance(algorithm);
            String hash = hashAlgorithm.getSaltedHash(password, salt);
            SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(hash), keyAlgorithm);

            // Setup cipher with the key and IV.
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            // Translate the file with the cipher.
            runCipherOnFiles(cipher, file, new File(file.getPath().replace(ENCRYPTED_EXTENSION, "")));
        } catch (BadPaddingException e) {
            throw new IncorrectPasswordException("Could not decrypt file with the given password.");
        } catch (Exception e) {
            throw new EncryptionException(
                    String.format("A %s was caught with the message \"%s\".", e.getClass(), e.getMessage()));
        }
    }

    /**
     * Uses the given {@link Cipher} to encrypt/decrypt the source file into the target file.
     *
     * @param cipher
     *            Cipher to use to encrypt/decrypt file
     * @param source
     *            File to encrypt/decrypt
     * @param target
     *            Result of encryption/decryption
     * 
     * @throws BadPaddingException
     *             If the padding is incorrect (usually because of an incorrect password)
     * @throws IllegalBlockSizeException
     *             If the block size is incorrect
     * @throws IOException
     *             If an IO problem is encountered
     * 
     * @author trthingnes
     */
    private void runCipherOnFiles(Cipher cipher, File source, File target)
            throws BadPaddingException, IllegalBlockSizeException, IOException {
        try (FileInputStream inputStream = new FileInputStream(source);
                FileOutputStream outputStream = new FileOutputStream(target)) {
            // Decrypt and write the file.
            byte[] buffer = new byte[64];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    outputStream.write(output);
                }
            }

            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
        }
    }

    /**
     * Generates an IV to use for encryption.
     *
     * @return 16 byte IV
     * 
     * @author trthingnes
     */
    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
