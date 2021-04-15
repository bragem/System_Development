package edu.ntnu.idatt1002.k2g10.utils.files;

import edu.ntnu.idatt1002.k2g10.Session;

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
     *            A pathname string.
     * @param object
     *            An object to store.
     * 
     * @throws NullPointerException
     *             If the {@code pathname} argument is {@code null}.
     * 
     * @see File#File(String)
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
     *            A pathname string.
     * 
     * @throws NullPointerException
     *             If the {@code pathname} argument is {@code null}.
     * 
     * @see File#File(String)
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
     * @throws IOException
     *             If something goes wrong while reading the file.
     * @throws ClassNotFoundException
     *             If object in file does not match type parameter.
     */
    public T getObject() throws IOException, ClassNotFoundException {
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
     * @throws IOException
     *             If something goes wrong while reading the file.
     * @throws ClassNotFoundException
     *             If object in file does not match type parameter.
     */
    public T readObjectFromFile() throws IOException, ClassNotFoundException {
        refreshObject();

        return object;
    }

    /**
     * Set the object in the cache.
     *
     * @param object
     *            Object to put in cache.
     */
    public void setObject(T object) {
        this.object = object;
    }

    /**
     * Writes the cached object to file.
     *
     * @throws IOException
     *             If file does not exist and cannot be created, or if file fails to write.
     */
    public void writeObjectToFile() throws IOException {
        if (!this.exists() && !this.createNewFile()) {
            throw new IOException(
                    "The object file at \"" + this.getPath() + "\" does not exist and could not be created.");
        }

        try (ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(this))) {
            objOut.writeObject(object);
        }
    }

    /**
     * Writes the given object to both the cache and file.
     *
     * @param object
     *            Object to write to file.
     *
     * @throws IOException
     *             If object cannot be written to file.
     */
    public void writeObjectToFile(T object) throws IOException {
        try (ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(this))) {
            objOut.writeObject(object);
            this.object = object;
        }
    }

    /**
     * Reads the object stored serialized file into the {@link ObjectFile} instance.
     *
     * @throws IOException
     *             If the file does not exist or if the file fails to read.
     * @throws ClassNotFoundException
     *             If the object read is not the same as type parameter T.
     */
    private void refreshObject() throws IOException, ClassNotFoundException {
        if (!this.exists()) {
            throw new IOException("The object file at \"" + this.getPath() + "\" does not exist.");
        }

        try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(this))) {
            Object readObject = objIn.readObject();

            object = (T) readObject;
        }
    }

    /**
     * Returns whether or not the {@link ObjectFile} contains an object.
     *
     * @return True if {@link ObjectFile} contains object, false if not.
     */
    public boolean hasObject() {
        return !Objects.isNull(object);
    }
}
