package edu.ntnu.idatt1002.k2g10.utils.files;

import edu.ntnu.idatt1002.k2g10.utils.crypto.IncorrectPasswordException;
import edu.ntnu.idatt1002.k2g10.utils.crypto.EncryptionException;
import edu.ntnu.idatt1002.k2g10.utils.crypto.FileEncryptionAlgorithm;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * {@link EncryptedFile} behaves like a {@link File} but it can be encrypted using the {@link #encrypt} method. To
 * reverse the operation, the {@link #decrypt} method is used. Both methods take a password to do encryption with. This
 * class uses the {@link FileEncryptionAlgorithm} class to encrypt the given file
 *
 * @see FileEncryptionAlgorithm
 * @see File
 * 
 * @author trthingnes
 */
public class EncryptedFile extends File {
    private static final FileEncryptionAlgorithm ALGORITHM = FileEncryptionAlgorithm.AES_CBC_PKCS5P;

    /**
     * Creates a new {@code EncryptedFile} instance by converting the given pathname string into an abstract pathname.
     * If the given string is the empty string, then the result is the empty abstract pathname.
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
    public EncryptedFile(String pathname) {
        super(pathname);
    }

    /**
     * Creates a new {@code EncryptedFile} instance from a parent pathname string and a child pathname string.
     *
     * <p>
     * If {@code parent} is {@code null} then the new {@code File} instance is created as if by invoking the
     * single-argument {@code File} constructor on the given {@code child} pathname string.
     *
     * <p>
     * Otherwise the {@code parent} pathname string is taken to denote a directory, and the {@code
     * child} pathname string is taken to denote either a directory or a file. If the {@code child} pathname string is
     * absolute then it is converted into a relative pathname in a system-dependent way. If {@code parent} is the empty
     * string then the new {@code File} instance is created by converting {@code child} into an abstract pathname and
     * resolving the result against a system-dependent default directory. Otherwise each pathname string is converted
     * into an abstract pathname and the child abstract pathname is resolved against the parent.
     *
     * @param parent
     *            The parent pathname string
     * @param child
     *            The child pathname string
     * 
     * @throws NullPointerException
     *             If {@code child} is {@code null}
     * 
     * @see File#File(File, String)
     * 
     * @author trthingnes
     */
    public EncryptedFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Creates a new {@code EncryptedFile} instance from a parent abstract pathname and a child pathname string.
     *
     * <p>
     * If {@code parent} is {@code null} then the new {@code File} instance is created as if by invoking the
     * single-argument {@code File} constructor on the given {@code child} pathname string.
     *
     * <p>
     * Otherwise the {@code parent} abstract pathname is taken to denote a directory, and the {@code child} pathname
     * string is taken to denote either a directory or a file. If the {@code
     * child} pathname string is absolute then it is converted into a relative pathname in a system-dependent way. If
     * {@code parent} is the empty abstract pathname then the new {@code
     * File} instance is created by converting {@code child} into an abstract pathname and resolving the result against
     * a system-dependent default directory. Otherwise each pathname string is converted into an abstract pathname and
     * the child abstract pathname is resolved against the parent.
     *
     * @param parent
     *            The parent abstract pathname
     * @param child
     *            The child pathname string
     * 
     * @throws NullPointerException
     *             If {@code child} is {@code null}
     * 
     * @see File#File(File, String)
     * 
     * @author trthingnes
     */
    public EncryptedFile(File parent, String child) {
        super(parent, child);
    }

    /**
     * Creates a new {@code EncryptedFile} instance by converting the given {@code file:} URI into an abstract pathname.
     *
     * <p>
     * The exact form of a {@code file:} URI is system-dependent, hence the transformation performed by this constructor
     * is also system-dependent.
     *
     * <p>
     * For a given abstract pathname <i>f</i> it is guaranteed that
     *
     * <blockquote>
     *
     * <code>
     * new File(</code><i>&nbsp;f</i><code>.{@link #toURI()
     * toURI}()).equals(</code><i>&nbsp;f</i><code>.{@link #getAbsoluteFile() getAbsoluteFile}())
     * </code>
     *
     * </blockquote>
     *
     * <p>
     * so long as the original abstract pathname, the URI, and the new abstract pathname are all created in (possibly
     * different invocations of) the same Java virtual machine. This relationship typically does not hold, however, when
     * a {@code file:} URI that is created in a virtual machine on one operating system is converted into an abstract
     * pathname in a virtual machine on a different operating system.
     *
     * @param uri
     *            An absolute, hierarchical URI with a scheme equal to {@code "file"}, a non-empty path component, and
     *            undefined authority, query, and fragment components
     * 
     * @throws NullPointerException
     *             If {@code uri} is {@code null}
     * @throws IllegalArgumentException
     *             If the preconditions on the parameter do not hold
     * 
     * @see #toURI()
     * @see URI
     * @see File#File(URI)
     * 
     * @author trthingnes
     */
    public EncryptedFile(URI uri) {
        super(uri);
    }

    /**
     * Encrypts the {@link EncryptedFile} with the given password.
     *
     * @param password
     *            Password to use for encryption
     *
     * @throws IOException
     *             If files cannot be cleaned up after encryption
     * @throws EncryptionException
     *             If file could not be encrypted successfully
     * 
     * @author trthingnes
     */
    public void encrypt(String password) throws IOException, EncryptionException {
        ALGORITHM.encryptFile(this, password);
        if (!this.delete()) {
            throw new IOException("Could not delete unencrypted file after encryption.");
        }
    }

    /**
     * Attempts to decrypt the {@link EncryptedFile} with the given password.
     *
     * @param password
     *            Password to attempt for decryption
     *
     * @throws IOException
     *             If files cannot be cleaned up after encryption
     * @throws IncorrectPasswordException
     *             If password for file is incorrect
     * @throws EncryptionException
     *             If file could not be decrypted successfully
     *
     * @author trthingnes
     */
    public void decrypt(String password, boolean deleteEncrypted)
            throws IOException, IncorrectPasswordException, EncryptionException {
        File encFile = new File(this.getPath().concat(FileEncryptionAlgorithm.ENCRYPTED_EXTENSION));
        File keyFile = new File(this.getPath().concat(FileEncryptionAlgorithm.SALT_IV_EXTENSION));

        ALGORITHM.decryptFile(this, password);

        if (deleteEncrypted && (!encFile.delete() || !keyFile.delete())) {
            throw new IOException("Could not delete encrypted files after decryption.");
        }
    }
}
