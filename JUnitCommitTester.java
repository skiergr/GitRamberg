import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class JUnitCommitTester {

        // tests if the commit is being correctly generated

        @Test
        void createACommit() throws Exception {
                Commit c0 = new Commit("name", "summary");

                BufferedReader br = new BufferedReader(new FileReader("test/objects/" +
                                Utils.getSHA(
                                                "da39a3ee5e6b4b0d3255bfef95601890afd80709\n\n\nname\n" + Utils.getDate()
                                                                +
                                                                "\nsummary")));
                // this is the sha1 of the contents of c0, generated with my own method (no the
                // one in this project)
                String expected = "da39a3ee5e6b4b0d3255bfef95601890afd80709\n\n\nname\n" +
                                Utils.getDate() + "\nsummary";
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
                Commit c0 = new Commit("author", "summary");
                assertEquals("convert to sha does not work", Utils.getSHA("hello"),
                                c0.convertToSha1("hello"));
        }

        // makes sure the getDate gets the correct value

        @Test
        void testGetDate() throws Exception {
                Commit c0 = new Commit("name", "summary");

                String d = c0.getDate();
                assertEquals("Date incorrect", d, Utils.getDate()); // only works for today's date until i edit the
                                                                    // tester
        }

        @Test
        void testCReatingCommit1() throws Exception {
                File test1 = new File("test1");
                Utils.createNewFile(test1, "testing1");
                File test2 = new File("test2");
                Utils.createNewFile(test2, "testing2");
                Index index = new Index();
                index.init();
                index.add("test1");
                index.add("test2");
                Commit c0 = new Commit("name", "summary");

                String test1Sha = Utils.getSHA(Utils.getFileContents(test1));
                String test2Sha = Utils.getSHA(Utils.getFileContents(test2));
                String treeContents = "blob : " + test2Sha + " : test2\nblob : " + test1Sha +
                                " : test1\n";
                String treeSha = Utils.getSHA(treeContents);
                String commitContents = treeSha + "\n\n\nname\n" + Utils.getDate()
                                + "\nsummary";
                String commitSha = Utils.getSHA(commitContents);

                File treeFile = new File("./test/objects/" + treeSha);
                File commitFile = new File("./test/objects/" + commitSha);

                assertTrue("tree does not exist", treeFile.exists());
                assertEquals("tree has wrong contents", Utils.getFileContents(treeFile),
                                treeContents);

                assertTrue("commit does not exist", commitFile.exists());
                assertEquals("commit has wrong contents", Utils.getFileContents(commitFile),
                                commitContents);
        }

        @Test
        void testCreatingCommit1() throws Exception {
                File test1 = new File("test1");
                Utils.createNewFile(test1, "testing1");
                File test2 = new File("test2");
                Utils.createNewFile(test2, "testing2");
                Index index = new Index();
                index.init();
                index.add("test1");
                index.add("test2");
                Commit c0 = new Commit("name", "summary");

                String test1Sha = Utils.getSHA(Utils.getFileContents(test1));
                String test2Sha = Utils.getSHA(Utils.getFileContents(test2));
                String treeContents = "blob : " + test2Sha + " : test2\nblob : " + test1Sha +
                                " : test1\n";
                String treeSha = Utils.getSHA(treeContents);
                String commitContents = treeSha + "\n\n\nname\n" + Utils.getDate()
                                + "\nsummary";
                String commitSha = Utils.getSHA(commitContents);

                File treeFile = new File("./test/objects/" + treeSha);
                File commitFile = new File("./test/objects/" + commitSha);

                assertTrue("tree does not exist", treeFile.exists());
                assertEquals("tree has wrong contents", Utils.getFileContents(treeFile),
                                treeContents);

                assertTrue("commit does not exist", commitFile.exists());
                assertEquals("commit has wrong contents", Utils.getFileContents(commitFile),
                                commitContents);

        }

        @Test
        void testCreatingCommit2() throws Exception {
                File test1 = new File("test1");
                Utils.createNewFile(test1, "testing1");
                File test2 = new File("test2");
                Utils.createNewFile(test2, "testing2");
                Index index = new Index();
                index.init();
                index.add("test1");
                index.add("test2");
                Commit c0 = new Commit("name", "summary");

                File test3 = new File("test3");
                Utils.createNewFile(test3, "testing3");
                File test4 = new File("test4");
                Utils.createNewFile(test4, "testing4");
                File directory = new File("directory");
                Utils.createNewDirectory(directory);
                Index index2 = new Index();
                index2.init();
                index2.add("test3");
                index2.add("test4");
                index2.add("directory");
                Commit c1 = new Commit(c0.getCommitSha(), "name", "summary");

                String test1Sha = Utils.getSHA(Utils.getFileContents(test1));
                String test2Sha = Utils.getSHA(Utils.getFileContents(test2));
                String treeContents = "blob : " + test2Sha + " : test2\nblob : " + test1Sha + " : test1\n";
                String treeSha = Utils.getSHA(treeContents);
                String commitContents = treeSha + "\n\n\nname\n" + Utils.getDate()
                                + "\nsummary";
                String newCommitContents = treeSha + "\n\n" + c1.getCommitSha() + "\nname\n" + Utils.getDate()
                                + "\nsummary";
                String commitSha = Utils.getSHA(commitContents);

                File treeFile = new File("./test/objects/" + treeSha);
                File commitFile = new File("./test/objects/" + commitSha);

                String test3Sha = Utils.getSHA(Utils.getFileContents(test3));
                String test4Sha = Utils.getSHA(Utils.getFileContents(test4));
                String treeContents2 = "tree : " + treeSha + "\nblob : " + test4Sha + " : test4\nblob : " + test3Sha
                                + " : test3\ntree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : directory\n";
                String treeSha2 = Utils.getSHA(treeContents2);
                String commitContents2 = treeSha2 + "\n" + c0.getCommitSha() + "\n\nname\n" + Utils.getDate()
                                + "\nsummary";
                String commitSha2 = Utils.getSHA(commitContents2);

                File treeFile2 = new File("test/objects/" + treeSha2);
                File commitFile2 = new File("test/objects/" + commitSha2);

                // testing commit 1
                assertTrue("tree does not exist", treeFile.exists());
                assertEquals("tree has wrong contents", Utils.getFileContents(treeFile), treeContents);

                assertTrue("commit does not exist", commitFile.exists());
                assertEquals("commit has wrong contents", Utils.getFileContents(commitFile), newCommitContents);

                // testing commit 2
                // assertEquals("", treeSha2, treeContents2);
                assertTrue("tree has does not exist", treeFile2.exists());
                assertEquals("tree has wrong contents", Utils.getFileContents(treeFile2), treeContents2);

                assertTrue("commit does not exist", commitFile2.exists());
                assertEquals("commit has wrong contents", Utils.getFileContents(commitFile2), commitContents2);
        }

        @Test
        void testCreatingCommit3() throws Exception {
                File test1 = new File("test1");
                Utils.createNewFile(test1, "testing1");
                File test2 = new File("test2");
                Utils.createNewFile(test2, "testing2");
                Index index = new Index();
                index.init();
                index.add("test1");
                index.add("test2");
                Commit c0 = new Commit("name", "summary");

                File test3 = new File("test3");
                Utils.createNewFile(test3, "testing3");
                File test4 = new File("test4");
                Utils.createNewFile(test4, "testing4");
                File directory = new File("directory");
                Utils.createNewDirectory(directory);
                Index index2 = new Index();
                index2.init();
                index2.add("test3");
                index2.add("test4");
                index2.add("directory");
                Commit c1 = new Commit(c0.getCommitSha(), "name", "summary");

                File test5 = new File("test5");
                Utils.createNewFile(test5, "testing5");
                File test6 = new File("test6");
                Utils.createNewFile(test6, "testing6");
                Index index3 = new Index();
                index3.init();
                index3.add("test5");
                index3.add("test6");
                Commit c2 = new Commit(c1.getCommitSha(), "name", "summary");

                File test7 = new File("test7");
                Utils.createNewFile(test7, "testing7");
                File test8 = new File("test8");
                Utils.createNewFile(test8, "testing8");
                File directory2 = new File("directory2");
                Utils.createNewDirectory(directory2);
                Index index4 = new Index();
                index4.init();
                index4.add("test7");
                index4.add("test8");
                index4.add("directory2");
                Commit c3 = new Commit(c2.getCommitSha(), "name", "summary");

                //

                String test1Sha = Utils.getSHA(Utils.getFileContents(test1));
                String test2Sha = Utils.getSHA(Utils.getFileContents(test2));
                String treeContents = "blob : " + test2Sha + " : test2\nblob : " + test1Sha + " : test1\n";
                String treeSha = Utils.getSHA(treeContents);
                String commitContents = treeSha + "\n\n\nname\n" + Utils.getDate()
                                + "\nsummary";
                String newCommitContents = treeSha + "\n\n" + c1.getCommitSha() + "\nname\n" + Utils.getDate()
                                + "\nsummary";
                String commitSha = Utils.getSHA(commitContents);
                File treeFile = new File("./test/objects/" + treeSha);
                File commitFile = new File("./test/objects/" + commitSha);

                String test3Sha = Utils.getSHA(Utils.getFileContents(test3));
                String test4Sha = Utils.getSHA(Utils.getFileContents(test4));
                String treeContents2 = "tree : " + treeSha + "\nblob : " + test4Sha + " : test4\nblob : " + test3Sha
                                + " : test3\ntree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : directory\n";
                String treeSha2 = Utils.getSHA(treeContents2);
                String commitContents2 = treeSha2 + "\n" + c0.getCommitSha() + "\n\nname\n" + Utils.getDate()
                                + "\nsummary";
                String newCommitContents2 = treeSha2 + "\n" + c0.getCommitSha() + "\n" + c2.getCommitSha() + "\nname\n"
                                + Utils.getDate()
                                + "\nsummary";
                String commitSha2 = Utils.getSHA(commitContents2);
                File treeFile2 = new File("test/objects/" + treeSha2);
                File commitFile2 = new File("test/objects/" + commitSha2);

                String test5Sha = Utils.getSHA(Utils.getFileContents(test5));
                String test6Sha = Utils.getSHA(Utils.getFileContents(test6));
                String treeContents3 = "tree : " + treeSha2 + "\nblob : " + test5Sha + " : test5\nblob : " + test6Sha
                                + " : test6\n";
                String treeSha3 = Utils.getSHA(treeContents3);
                String commitContents3 = treeSha3 + "\n" + c1.getCommitSha() + "\n\nname\n" + Utils.getDate()
                                + "\nsummary";
                String newCommitContents3 = treeSha3 + "\n" + c1.getCommitSha() + "\n" + c3.getCommitSha() + "\nname\n"
                                + Utils.getDate()
                                + "\nsummary";
                String commitSha3 = Utils.getSHA(commitContents3);
                File treeFile3 = new File("./test/objects/" + treeSha3);
                File commitFile3 = new File("./test/objects/" + commitSha3);

                String test7Sha = Utils.getSHA(Utils.getFileContents(test7));
                String test8Sha = Utils.getSHA(Utils.getFileContents(test8));
                String treeContents4 = "tree : " + treeSha3 + "\nblob : " + test8Sha + " : test8\nblob : " + test7Sha
                                + " : test7\ntree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : directory2\n";
                String treeSha4 = Utils.getSHA(treeContents4);
                String commitContents4 = treeSha4 + "\n" + c2.getCommitSha() + "\n\nname\n" + Utils.getDate()
                                + "\nsummary";
                String commitSha4 = Utils.getSHA(commitContents4);
                File treeFile4 = new File("./test/objects/" + treeSha4);
                File commitFile4 = new File("./test/objects/" + commitSha4);

                // testing commit 1
                assertTrue("tree does not exist", treeFile.exists());
                assertEquals("tree has wrong contents", Utils.getFileContents(treeFile), treeContents);

                assertTrue("commit does not exist", commitFile.exists());
                assertEquals("commit has wrong contents", Utils.getFileContents(commitFile), newCommitContents);

                // testing commit 2
                assertTrue("tree does not exist", treeFile2.exists());
                assertEquals("tree has wrong contents", Utils.getFileContents(treeFile2), treeContents2);

                assertTrue("commit does not exist", commitFile2.exists());
                assertEquals("commit has wrong contents", Utils.getFileContents(commitFile2), newCommitContents2);

                // testing commit 3
                assertTrue("tree does not exist", treeFile3.exists());
                assertEquals("tree has wrong contents", Utils.getFileContents(treeFile3), treeContents3);

                assertTrue("commit does not exist", commitFile3.exists());
                assertEquals("commit has wrong contents", Utils.getFileContents(commitFile3), newCommitContents3);

                // testing commit 4
                assertEquals("difu", treeContents4, treeSha4);
                assertTrue("tree does not exist", treeFile4.exists());
                assertEquals("tree has wrong contents", Utils.getFileContents(treeFile4), treeContents4);

                assertTrue("commit does not exist", commitFile4.exists());
                assertEquals("commit has wrong contents", Utils.getFileContents(commitFile4), commitContents4);

        }

}
