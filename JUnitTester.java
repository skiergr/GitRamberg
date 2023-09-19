import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JUnitTester {
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
    @DisplayName("Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {

        // Manually create the files and folders before the 'testAddFile'
        // MyGitProject myGitClassInstance = new MyGitProject();
        // myGitClassInstance.init();
        Blob blob = new Blob (testFile);
        String blobSha = blob.getsha1Contents();
        String contents = blob.getFileContents(testFile);
        File file = new File ("./test/objects/"+blobSha);
        assertEquals("Contents are the same", testFileContents, contents);
        assertTrue(file.exists());
    }
    
    @Test
    @DisplayName("Test if initialize works")
    void testInitialize() throws Exception {
        File index = new File("./test/index");
        if (index.exists()) {
            index.delete();
        }
        File objects = new File("./test/objects");
        if (objects.exists()) {
            objects.mkdirs();
            objects.delete();
        }
        // Run the person's code
        // TestHelper.runTestSuiteMethods("testInitialize");
        Index idx = new Index();
        idx.init();

        // check if the file exists
        File file = new File("./test/index");
        Path path = Paths.get("./test/objects");
        //tests if index exists
        assertTrue(file.exists());
        //tests if objects exist
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("Calling the Add Function.")
    void testAdd() throws Exception {
        Index idx = new Index();
        idx.init();
        idx.add(testFile);
        idx.add(testFile2);
        idx.add(testFile3);

        //testing that blobs are created
        File file1 = new File("test/objects/" + testFileSha);
        File file2 = new File("test/objects/" + testFile2Sha);
        File file3 = new File("test/objects/" + testFile3Sha);

        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());

        //testing that the index is correct
        File index = new File("test/index");
        BufferedReader br = new BufferedReader(new FileReader(index));
        String indexContents = "";
        while (br.ready()) {
            indexContents += (char) br.read();
        }
        br.close();
        assertEquals("index is correct", indexContents, expectedIndex);

        //testing remove
        idx.remove("testFile");
        BufferedReader br1 = new BufferedReader(new FileReader(index));
        String newIndexContents = "";
        while (br1.ready()) {
            newIndexContents += (char) br1.read();
        }
        br1.close();
        String newExpectedIndex = "testfile2 : f4b774b6be2cbfab5d69687fa6445453d0527bde\ntestfile3 : b055f09351c99e93474bf62e55504c9b115214ca\n";
        assertEquals("index is correct", newIndexContents, newExpectedIndex);
    }
    @Test
    @DisplayName("Testing Trees.")
    void TestTree () throws IOException {
        Index index = new Index ();
        index.init();
        Tree tree = new Tree();
        File treeFile = new File("test/objects/tree");
        assertTrue(treeFile.exists());

        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");

        BufferedReader br = new BufferedReader(new FileReader(treeFile));
        String expected = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b\nblob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt\n";
        String actual = "";
        while(br.ready()) {
            actual += (char) br.read();
        }
        br.close();
        assertEquals(expected, actual);

        tree.remove("file1.txt");
        tree.remove("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        BufferedReader br1 = new BufferedReader(new FileReader(treeFile));
        String actual1 = "";
        while(br1.ready()) {
            actual1 += (char) br1.read();
        }
        br1.close();
        assertEquals("", actual1);
    }
}
