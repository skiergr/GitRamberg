import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JUnitIndexTester {
    private static String testFile = "testFile";
    private static String testFileContents = "SHA1 Test";
    private static String testFileSha = "8345afc8180a8ea7f76903ad3b70c1c9180bb2e0";
    private static String testFile2 = "testfile2";
    private static String testFile2Contents = "#2 testing ";
    private static String testFile2Sha = "f4b774b6be2cbfab5d69687fa6445453d0527bde";
    private static String testFile3 = "testfile3";
    private static String testFile3Contents = "#3 testing";
    private static String testFile3Sha = "b055f09351c99e93474bf62e55504c9b115214ca";
    private static String expectedIndex = "testFile : 8345afc8180a8ea7f76903ad3b70c1c9180bb2e0\n" + //
            "testfile2 : f4b774b6be2cbfab5d69687fa6445453d0527bde\n" + //
            "testfile3 : b055f09351c99e93474bf62e55504c9b115214ca\n";

    @BeforeAll
    static void setUp() throws Exception {
        File test = new File("test");
        if (!test.exists()) {
            test.mkdirs();
        }
        File index = new File("test/index");
        if (index.exists()) {
            index.delete();
        }
        File objects = new File("test/objects");
        if (objects.exists()) {
            objects.mkdirs();
            objects.delete();
        }
        File testFile = new File("testFile");
        testFile.createNewFile();
        PrintWriter pw1 = new PrintWriter(new FileWriter(testFile));
        pw1.print(testFileContents);
        pw1.close();
        File testfile2 = new File("testfile2");
        testfile2.createNewFile();
        PrintWriter pw2 = new PrintWriter(new FileWriter(testfile2));
        pw2.print(testFile2Contents);
        pw2.close();
        File testfile3 = new File("testfile3");
        testfile3.createNewFile();
        PrintWriter pw3 = new PrintWriter(new FileWriter(testfile3));
        pw3.print(testFile3Contents);
        pw3.close();
    }

    @AfterAll
    static void tearDown() throws Exception {
        File index = new File("./test/index");
        if (index.exists()) {
            index.delete();
        }
        File objects = new File("./test/objects");
        if (objects.exists()) {
            objects.mkdirs();
            objects.delete();
        }
    }

    @Test
    @DisplayName("Test if index file is created")
    void testIndexCreation() throws Exception {
        Index idx = new Index();
        idx.init();
        File file = new File("./test/index");
        //tests if index file exists
        assertTrue(file.exists());
    }

    @DisplayName("Test if objects directory is created")
    void testObjectsCreation() throws Exception {
        Index idx = new Index();
        idx.init();
        Path path = Paths.get("./test/objects");
        //tests if objects directory exist
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("Testing that the blob files are created when the add function is called.")
    void testBlobCreation() throws Exception {
        Index idx = new Index();
        idx.init();
        idx.add(testFile);
        idx.add(testFile2);
        idx.add(testFile3);

        //testing that blobs are created
        File file1 = new File("test/objects/" + testFileSha);
        File file2 = new File("test/objects/" + testFile2Sha);
        File file3 = new File("test/objects/" + testFile3Sha);

        //asserts that the files have been created
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());
    }

    @Test
    @DisplayName("Testing that the index content is correct.")
    void testIndexContent() throws Exception {
        Index idx = new Index();
        idx.init();
        idx.add(testFile);
        idx.add(testFile2);
        idx.add(testFile3);

        //testing that the index is correct
        File index = new File("test/index");
        BufferedReader br = new BufferedReader(new FileReader(index));
        String indexContents = "";
        while (br.ready()) {
            indexContents += (char) br.read();
        }
        br.close();
        //test that the index contents are right
        assertEquals("index is correct", indexContents, expectedIndex);
    }

    @Test
    @DisplayName("Testing that the remove changes the index as expected")
    void testRemove() throws Exception {
        Index idx = new Index();
        idx.init();
        idx.add(testFile);
        idx.add(testFile2);
        idx.add(testFile3);
        //testing remove
        idx.remove("testFile");
        BufferedReader br1 = new BufferedReader(new FileReader("test/index"));
        String newIndexContents = "";
        while (br1.ready()) {
            newIndexContents += (char) br1.read();
        }
        br1.close();
        String newExpectedIndex = "testfile2 : f4b774b6be2cbfab5d69687fa6445453d0527bde\ntestfile3 : b055f09351c99e93474bf62e55504c9b115214ca\n";
        //tests that the entries were removed from the index
        assertEquals("index is correct", newIndexContents, newExpectedIndex);

        idx.remove("testfile2");
        BufferedReader br2 = new BufferedReader(new FileReader("test/index"));
        String newIndexContents2 = "";
        while (br2.ready()) {
            newIndexContents2 += (char) br2.read();
        }
        br2.close();
        String newExpectedIndex2 = "testfile3 : b055f09351c99e93474bf62e55504c9b115214ca\n";
        //tests that the entries were removed from the index
        assertEquals("index is correct", newIndexContents2, newExpectedIndex2);
    }
}
