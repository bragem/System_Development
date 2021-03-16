package edu.ntnu.idatt1002.k2g10.utils.files;

import edu.ntnu.idatt1002.k2g10.App;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Objects;

/**
 * {@link ObjectFile} acts like a normal {@code File} instance but it also could contain an object of type {@code T}.
 * This class can be used to easily write {@link Serializable} objects to a file.
 *
 * @param <T>
 *            Contained type
 * 
 * @author trthingnes
 */
public class ObjectFile<T extends Serializable> extends File {
    private T object = null;

    /**
     * Creates a new {@code ObjectFile} by converting the given pathname string into an abstract pathname. If the given
     * string is the empty string, then the result is the empty abstract pathname.
     *
     * <p>
     * After file creating, the object is added to the instance and contained.
     *
     * @param pathname
     *            A pathname string
     * @param object
     *            An object to store
     * 
     * @throws NullPointerException
     *             If the {@code pathname} argument is {@code null}
     * 
     * @see File#File(String)
     * 
     * @author trthingnes
     */
    public ObjectFile(String pathname, T object) {
        super(pathname);
        this.object = object;
    }

    /**
     * Creates a new empty {@code ObjectFile} instance by converting the given pathname string into an abstract
     * pathname. If the given string is the empty string, then the result is the empty abstract pathname.
     *
     * @param pathname
     *            A pathname string
     * 
     * @throws NullPointerException
     *             If the {@code pathname} argument is {@code null}
     * 
     * @see File#File(String)
     * 
     * @author trthingnes
     */
    public ObjectFile(String pathname) {
        super(pathname);
    }

    /**
     * Gets the associated object from file or from cache. Will only get a new version from file if one is not already
     * contained in this {@link ObjectFile}.
     *
     * @return Object of the defined type {@code T}, or {@code null} if file read fails.
     * 
     * @author trthingnes
     */
    public T getObject() {
        if (!hasObject()) {
            refreshObject();
        }

        return object;
    }

    /**
     * Ignores cached object and gets the associated object from file. Should only be used if it is vital to have the
     * newest version of an object.
     *
     * @return Object of the defined type {@code T}, or {@code null} if file read fails.
     * 
     * @author trthingnes
     */
    public T readObjectFromFile() {
        if (!refreshObject()) {
            return null;
        }

        return object;
    }

    /**
     * Set the object in the cache.
     *
     * @param object
     *            Object to put in cache
     * 
     * @author trthingnes
     */
    public void setObject(T object) {
        this.object = object;
    }

    /**
     * Writes the cached object to file.
     *
     * @return True if write is successful, and false if not
     * 
     * @author trthingnes
     */
    public boolean writeObjectToFile() {
        try {
            if (!this.exists() && !this.createNewFile()) {
                return false;
            }

            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(this));
            objOut.writeObject(object);
            objOut.close();
        } catch (IOException e) {
            App.getLogger()
                    .warning(String.format("Unable to write %s object to file.", object.getClass().getSimpleName()));
            return false;
        }

        return true;
    }

    /**
     * Writes the given object to both the cache and file.
     *
     * @param object
     *            Object to write to file
     * 
     * @return True if write is successful, and false if not
     * 
     * @author trthingnes
     */
    public boolean writeObjectToFile(T object) {
        try (ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(this))) {
            objOut.writeObject(object);
            this.object = object;
        } catch (IOException e) {
            App.getLogger()
                    .warning(String.format("Unable to write %s object to file.", object.getClass().getSimpleName()));
            return false;
        }

        return true;
    }

    /**
     * Reads the object stored in the {@link ObjectFile}.
     *
     * @return True if read is successful, and false if not
     * 
     * @throws InputMismatchException
     *             If the object read is not the same as type parameter T
     * 
     * @author trthingnes
     */
    private boolean refreshObject() {
        if (!this.exists()) {
            return false;
        }

        try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(this))) {
            Object readObject = objIn.readObject();

            object = (T) readObject;
        } catch (IOException | ClassNotFoundException e) {
            App.getLogger().warning(
                    String.format("Unable to read object from file because of an %s.", e.getClass().getSimpleName()));
            return false;
        } catch (ClassCastException e) {
            // This is thrown if the class of the read object is not the same as T.
            App.getLogger().warning("Unable to read object from file because object types do not match.");
            throw new InputMismatchException("The object in the given file is not an instance of type parameter.");
        }

        return true;
    }

    /**
     * Returns whether or not the {@link ObjectFile} contains an object.
     *
     * @return True if {@link ObjectFile} contains object, false if not.
     * 
     * @author trthingnes
     */
    public boolean hasObject() {
        return !Objects.isNull(object);
    }
}
