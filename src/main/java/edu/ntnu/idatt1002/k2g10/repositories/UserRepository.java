package edu.ntnu.idatt1002.k2g10.repositories;

import edu.ntnu.idatt1002.k2g10.Session;
import edu.ntnu.idatt1002.k2g10.utils.crypto.IncorrectPasswordException;
import edu.ntnu.idatt1002.k2g10.models.User;
import edu.ntnu.idatt1002.k2g10.utils.crypto.EncryptionException;
import edu.ntnu.idatt1002.k2g10.utils.files.EncryptedFile;
import edu.ntnu.idatt1002.k2g10.utils.files.ObjectFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * {@link UserRepository} deals offers methods related to the {@link User} model.
 * 
 * @author trthingnes
 */
public class UserRepository {
    private static final String USER_FILES_FOLDER = "src/main/resources/users";
    private static final String USER_FILE_EXTENSION = "user";

    private UserRepository() {
        // not called
    }

    /**
     * Loads the given {@link User} and attempts to use the given password for decryption.
     * 
     * @param username
     *            Username of the user to access.
     * @param password
     *            Password the user file was encrypted with.
     * 
     * @return User object deserialized from file.
     * 
     * @throws IncorrectPasswordException
     *             If the password for the user file is incorrect.
     * @throws IOException
     *             If something goes wrong while handling files.
     * @throws EncryptionException
     *             If something goes wrong while decrypting.
     */
    public static User load(String username, String password)
            throws IncorrectPasswordException, IOException, EncryptionException {
        String filePath = String.format("%s/%s.%s", USER_FILES_FOLDER, username, USER_FILE_EXTENSION);

        // Attempt to decrypt the encrypted user file.
        new EncryptedFile(filePath).decrypt(password, false);

        // Attempt to deserialize user from file.
        try {
            ObjectFile<User> userFile = new ObjectFile<>(filePath);
            User user = userFile.readObjectFromFile();

            if (!userFile.delete()) {
                Session.getLogger().warning("Unable to delete decrypted file after reading into memory.");
            }

            return user;
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Unable to read object file.");
        }
    }

    /**
     * Saved the given {@link User} to the user file folder, and encrypts it with the given password.
     * 
     * @param user
     *            User to serialize into file.
     * @param password
     *            Password to use for file encryption.
     * 
     * @throws IOException
     *             If something goes wrong while handling files.
     * @throws EncryptionException
     *             If something goes wrong while encrypting.
     */
    public static void save(User user, String password) throws IOException, EncryptionException {
        // Attempt to make users folder.
        File usersFolder = new File(USER_FILES_FOLDER);
        usersFolder.mkdir();

        String filePath = String.format("%s/%s.%s", USER_FILES_FOLDER, user.getUsername(), USER_FILE_EXTENSION);

        // Attempt to write user to file.
        ObjectFile<User> userFile = new ObjectFile<>(filePath, user);
        userFile.writeObjectToFile();

        // Attempt to encrypt user file.
        EncryptedFile encryptedUserFile = new EncryptedFile(filePath);
        encryptedUserFile.encrypt(password);
    }

    /**
     * Get the username of all users that have a file in the application folder.
     * 
     * @return List of usernames.
     */
    public static String[] getAllUsernames() {
        File userFolder = new File(USER_FILES_FOLDER);
        HashSet<String> usernames = new HashSet<>();

        if (Objects.isNull(userFolder.listFiles()))
            return new String[0];

        Arrays.stream(userFolder.listFiles()).forEach(file -> usernames.add(file.getName().split("\\.")[0]));

        return usernames.toArray(new String[0]);
    }
}