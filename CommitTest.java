import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CommitTest {
    Commit c0;

    // tests if the commit is being correctly generated
    @Test
    void createACommit() throws Exception {
        c0 = new Commit("name", "summary");

        BufferedReader br = new BufferedReader(new FileReader("test/objects/" + Utils.getSHA(
                "da39a3ee5e6b4b0d3255bfef95601890afd80709\n\n\nname\n" + Utils.getDate() + "\nsummary")));
        // this is the sha1 of the contents of c0, generated with my own method (not the
        // one in this project)
        String expected = "da39a3ee5e6b4b0d3255bfef95601890afd80709\n\n\nname\n" + Utils.getDate() + "\nsummary";
        String actual = "";
        while (br.ready()) {
            actual += (char) br.read();
        }
        br.close();
        // checks that the entries match what is expected
        assertEquals(expected, actual);
        // bro there's just an extra new line

    }

    @Test
    void testConvertToSha1() throws Exception {
        c0 = new Commit("author", "summary");
        assertEquals("convert to sha does not work", Utils.getSHA("hello"), c0.convertToSha1("hello"));
    }

    // makes sure the getDate gets the correct value
    @Test
    void testGetDate() throws Exception {
        c0 = new Commit("name", "summary");

        String d = c0.getDate();
        assertEquals("Date incorrect", d, Utils.getDate()); // only works for today's date until i edit the tester
    }

    @Test
    void testCreating1Commit() throws Exception {
        File test1 = new File("test1");
        Utils.createNewFile(test1, "testing1");
        File test2 = new File("test2");
        Utils.createNewFile(test2, "testing2");
        Index index = new Index();
        index.init();
        index.add("test1");
        index.add("test2");
        c0 = new Commit("da39a3ee5e6b4b0d3255bfef95601890afd80709", "name", "summary");

        String test1Sha = Utils.getSHA(Utils.getFileContents(test1));
        String test2Sha = Utils.getSHA(Utils.getFileContents(test2));
        String treeContents = "blob : " + test2Sha + " : test2\nblob : " + test1Sha + " : test1\n";
        String treeSha = Utils.getSHA(treeContents);
        String commitContents = treeSha + "\nda39a3ee5e6b4b0d3255bfef95601890afd80709\n\nname\n" + Utils.getDate()
                + "\nsummary";
        String commitSha = Utils.getSHA(commitContents);

        File treeFile = new File("./test/objects/" + treeSha);
        File commitFile = new File("./test/objects/" + commitSha);

        assertTrue("tree does not exist", treeFile.exists());
        assertEquals("tree has wrong contents", Utils.getFileContents(treeFile), treeContents);

        assertTrue("commit does not exist", commitFile.exists());
        assertEquals("commit has wrong contents", Utils.getFileContents(commitFile), commitContents);

    }

}
