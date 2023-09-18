import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JUnitBlobTester {
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
        File objects = new File("test/objects");
        if (!objects.exists()) {
            objects.mkdirs();
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
    @DisplayName("Test if contents in the blob and the original file are the same")
    void testBlobContent() throws Exception {
        Blob blob = new Blob (testFile);
        String contents = blob.getFileContents(testFile);
        //ensures that the contents of the original file and the contents of the blob file are the same
        assertEquals("Contents are the same", testFileContents, contents);
    }

    @Test
    @DisplayName("Test if the blob file is created")
    void testBlobFile() throws Exception {
        Blob blob = new Blob (testFile);
        String blobSha = blob.getsha1Contents();
        File file = new File ("test/objects/"+blobSha);
        //tests that the created blob file is in the right place and has the right name
        assertTrue(file.exists());
    }
}
