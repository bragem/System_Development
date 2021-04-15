package edu.ntnu.idatt1002.k2g10.utils.files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test object files")
public class ObjectFileTest {
    private String path;
    private String string;
    private ObjectFile<String> stringObjectFile;

    @BeforeEach
    void prepareTest() {
        path = String.format("src/test/java/%s/string.ser", this.getClass().getPackageName().replace(".", "/"));
        string = "This is my test string.";
        stringObjectFile = new ObjectFile<>(path, string);

    }

    @AfterEach
    void cleanupTest() {
        assertTrue(stringObjectFile.delete());
    }

    @Test
    @DisplayName("Object writes to file successfully")
    void testObjectWritesToFileSuccessfully() {
        assertDoesNotThrow(() -> stringObjectFile.writeObjectToFile());
    }

    @Test
    @DisplayName("Object reads from file successfully")
    void testObjectReadsFromFileSuccessfully() throws IOException {
        stringObjectFile.writeObjectToFile();

        assertDoesNotThrow(() -> {
            stringObjectFile.readObjectFromFile();
        });
    }

    @Test
    @DisplayName("Object is the same after being written and read")
    void testObjectIsTheSameAfterBeingWrittenAndRead() throws IOException, ClassNotFoundException {
        stringObjectFile.writeObjectToFile();

        assertEquals(string, stringObjectFile.readObjectFromFile());
    }
}
