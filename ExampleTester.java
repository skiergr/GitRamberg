import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExampleTester {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        /*
         * Utils.writeStringToFile("junit_example_file_data.txt", "test file contents");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }


    @Test
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {

        // Run the person's code
        // TestHelper.runTestSuiteMethods("testInitialize");
        Index index = new Index();
        index.init();

        // check if the file exists
        File file = new File("./test/index");
        Path path = Paths.get("./test/objects");
        //tests if index exists
        assertTrue(file.exists());
        //tests if objects exist
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {

        try {

            // Manually create the files and folders before the 'testAddFile'
            // MyGitProject myGitClassInstance = new MyGitProject();
            // myGitClassInstance.init();
            Index index = new Index();
            index.init();
            index.add("testFile");

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        // Check blob exists in the objects folder
        BufferedReader reader = new BufferedReader(new FileReader(new File("testFile")));
            String fileContents = "";
            while (reader.ready()) {
                fileContents += reader.readLine();
            }
            String sha = Blob.convertToSha1(fileContents);
        reader.close();
        File file_junit1 = new File("objects/" + sha);
        assertTrue("Blob file to add not found", file_junit1.exists());

        // Read file contents
        BufferedReader reader2 = new BufferedReader(new FileReader(sha));
        String newContents = "";
        while (reader2.ready()) {
            newContents += reader2.readLine();
        }
        reader2.close();
        assertEquals("File contents of Blob don't match file contents pre-blob creation", newContents,
                fileContents);
    }
    @Test
    @DisplayName("[15] Test if removing a blob works.")
    void testRemoveBlob() throws Exception {

    }
}
