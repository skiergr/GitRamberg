import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JUnitTreeTester {
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
        File index = new File("test/index");
        if (index.exists()) {
            index.delete();
        }
        File objects = new File("test/objects");
        if (objects.exists()) {
            objects.mkdirs();
            objects.delete();
        }
    }

    @Test
    @DisplayName("Testing that the tree file is created properly.")
    void testTreeFileCreation() throws IOException {
        Index index = new Index();
        index.init();
        Tree tree = new Tree();
        tree.printHash();
        File treeFile = new File("test/objects/da39a3ee5e6b4b0d3255bfef95601890afd80709");
        assertTrue(treeFile.exists());
    }

    // look man, idk where you are testing convertToSha, but it isn't working
    // as in, it runs, but is generating the wrong sha1 code

    @Test
    @DisplayName("Testing Add function.")
    void testAdd() throws IOException {
        Index index = new Index();
        index.init();
        Tree tree = new Tree();
        File treeFile = new File("test/objects/" + Utils.getSHA(
                "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b\nblob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt\n"));

        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.printHash();
        BufferedReader br = new BufferedReader(new FileReader(treeFile));
        String expected = "tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b\nblob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt\n";
        String actual = "";
        while (br.ready()) {
            actual += (char) br.read();
        }
        br.close();
        // checks that the entries match what is expected
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Testing Remove.")
    void testRemove() throws IOException {
        Index index = new Index();
        index.init();
        Tree tree = new Tree();
        File treeFile = new File("test/objects/da39a3ee5e6b4b0d3255bfef95601890afd80709");
        assertTrue(treeFile.exists());

        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.remove("file1.txt");
        tree.remove("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        BufferedReader br1 = new BufferedReader(new FileReader(treeFile));
        String actual1 = "";
        while (br1.ready()) {
            actual1 += (char) br1.read();
        }
        br1.close();
        // tests that all the entries were removed from the tree file as expected
        assertEquals("", actual1);
    }

    @Test
    @DisplayName("Testing Add Directory.")
    void testAddDirectory() throws Exception {
        File directory1 = new File("directory1");
        Utils.createNewDirectory(directory1);

        File directory1File1 = new File("directory1/file1.txt");
        Utils.createNewFile(directory1File1, "testing1");

        File directory1File2 = new File("directory1/file2.txt");
        Utils.createNewFile(directory1File2, "testing2");

        File directory1File3 = new File("directory1/file3.txt");
        Utils.createNewFile(directory1File3, "testing3");

        Index index = new Index();
        index.init();
        Tree tree = new Tree();
        String treeSHA1 = tree.addDirectory("directory1");
        File treeFile1 = new File("test/objects/" + treeSHA1);

        assertTrue("tree1 was not created", treeFile1.exists());

        assertEquals("tree1 has the right contents", Utils.getFileContents(treeFile1),
                "blob : 596b29ec9afea9e461a20610d150939b9c399d93 : file2.txt\nblob : e0a56a88c41f712d460ff97c54a499641685762b : file3.txt\nblob : ac250e4a00ff3144ae7689f0d23e8b26d06aa929 : file1.txt\n");

    }
}