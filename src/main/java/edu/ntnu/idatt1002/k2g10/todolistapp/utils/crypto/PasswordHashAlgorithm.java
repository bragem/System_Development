package edu.ntnu.idatt1002.k2g10.todolistapp.utils.crypto;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Used to securely salt and hash passwords.
 *
 * @author trthingnes
 */
public class PasswordHashAlgorithm {
    // Hash algorithm presets
    public static final PasswordHashAlgorithm PBKDF2 = new PasswordHashAlgorithm("PBKDF2WithHmacSHA256", 256, 65536);

    private final String algorithm;
    private final int keyLength;
    private final int iterations;

    /**
     * Constructs a new instance of the {@link PasswordHashAlgorithm} class.
     *
     * @param algorithm
     *            The hashing algorithm to use
     * @param keyLength
     *            The length of the derived key
     * @param iterations
     *            The number of iterations to derive the key
     * 
     * @see SecretKeyFactory#getInstance(String)
     * @see PBEKeySpec#PBEKeySpec(char[], byte[], int, int)
     */
    public PasswordHashAlgorithm(String algorithm, int keyLength, int iterations) {
        this.algorithm = algorithm;
        this.keyLength = keyLength;
        this.iterations = iterations;
    }

    /**
     * {@inheritDoc}
     *
     * @param password
     *            Password to salt and hash.
     * @param salt
     *            Salt to hash with the password.
     * 
     * @return Hash encoded as a {@link Base64} string.
     * 
     * @throws HashException
     *             If the hashing cannot be completed.
     */
    public String getSaltedHash(String password, String salt) throws HashException {
        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes(StandardCharsets.UTF_8);

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, iterations, keyLength);
        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            byte[] passwordHash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(passwordHash);
        } catch (Exception e) {
            throw new HashException(
                    String.format("Hashing failed because of a %s with the message %s.", e.getClass(), e.getMessage()));
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Runs the hashing algorithm with the provided password and a generated salt.
     *
     * @param password
     *            Password to salt and hash.
     * 
     * @return {@code String[]} with {@link Base64} salt as the first element and {@link Base64} hash as the second.
     * 
     * @throws HashException
     *             If the hashing cannot be completed.
     */
    public String[] getSaltedHash(String password) throws HashException {
        String salt = generateSalt();
        return new String[] { salt, getSaltedHash(password, salt) };
    }

    /**
     * Verify that the given password and salt gives the hash provided.
     *
     * @param password
     *            Password to hash.
     * @param salt
     *            Salt to hash with given password.
     * @param hash
     *            Hash to compare the hashing result to.
     * 
     * @return True if the result and the given hash match, false if not.
     */
    public boolean verifyHash(String password, String salt, String hash) {
        String passwordHash;
        try {
            passwordHash = getSaltedHash(password, salt);
        } catch (HashException e) {
            return false;
        }

        return passwordHash.equals(hash);
    }

    /**
     * Generates a salt with the default length.
     *
     * @return Salt encoded as a {@link Base64} string.
     * 
     * @see #generateSalt(int)
     */
    public String generateSalt() {
        final int DEFAULT_SALT_SIZE = 32;
        return generateSalt(DEFAULT_SALT_SIZE);
    }

    /**
     * Generates a salt with given length using the {@link SecureRandom} class. The {@code length} parameter defines the
     * number of bytes in the salt, and not the number of characters in the returned Base64 string.
     *
     * @param length
     *            Number of bytes in the returned salt.
     * 
     * @return Salt encoded as a {@link Base64} string.
     */
    public String generateSalt(int length) {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[length];
        sr.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }
}
