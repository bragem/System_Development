package edu.ntnu.idatt1002.k2g10.utils.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test password hashing algorithm")
public class PasswordHashAlgorithmTest {
    private final PasswordHashAlgorithm alg = PasswordHashAlgorithm.PBKDF2;
    private final String password = "mysupersecretpassword";
    private final String wrongCasePassword = "MySuperSecretPassword";
    private final String wrongPassword = "notmysupersecretpassword";
    private final String salt = alg.generateSalt();
    private String hash;

    public PasswordHashAlgorithmTest() {
        try {
            hash = alg.getSaltedHash(password, salt);
        } catch (HashException e) {
            hash = null;
        }
    }

    @Test
    @DisplayName("Correct password and correct salt verifies")
    void testCorrectPasswordAndCorrectSaltVerifies() {
        assertTrue(alg.verifyHash(password, salt, hash));
    }

    @Test
    @DisplayName("Wrong password and correct salt does not verify")
    void testIncorrectPasswordAndCorrectSaltDoesNotVerify() {
        assertFalse(alg.verifyHash(wrongPassword, salt, hash));
    }

    @Test
    @DisplayName("Correct password and wrong salt does not verify")
    void testCorrectPasswordAndIncorrectSaltDoesNotVerify() {
        assertFalse(alg.verifyHash(password, alg.generateSalt(), hash));
    }

    @Test
    @DisplayName("Wrong password and wrong salt does not verify")
    void testIncorrectPasswordAndIncorrectSaltDoesNotVerify() {
        assertFalse(alg.verifyHash(wrongPassword, alg.generateSalt(), hash));
    }

    @Test
    @DisplayName("Wrong password case and correct salt does not verify")
    void testIncorrectPasswordCaseAndCorrectSaltDoesNotVerify() {
        assertFalse(alg.verifyHash(wrongCasePassword, salt, hash));
    }

    @Test
    @DisplayName("Wrong password case and wrong salt does not verify")
    void testIncorrectPasswordCaseAndIncorrectSaltDoesNotVerify() {
        assertFalse(alg.verifyHash(wrongCasePassword, alg.generateSalt(), hash));
    }

    @Test
    @DisplayName("Two hashes of the same string with different salts are not equal")
    void testHashesOfSameStringWithDifferentSaltsAreDifferent() throws HashException {
        String otherHash = alg.getSaltedHash(password, alg.generateSalt());
        assertNotEquals(otherHash, hash);
    }
}
