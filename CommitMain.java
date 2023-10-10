import java.io.File;
import java.io.IOException;

public class CommitMain {
    public static void main(String[] args) throws Exception {
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
        if (treeFile.exists()) {
            System.out.println("1 is true");
        }
        if (Utils.getFileContents(treeFile).equals(treeContents)) {
            System.out.println("2 is true");
        }

        if (commitFile.exists()) {
            System.out.println("3 is true");
        }
        if (Utils.getFileContents(commitFile).equals(newCommitContents)) {
            System.out.println("4 is true");
        }

        if (treeSha2.equals(Utils.getSHA(treeContents2))) {
            System.out.println("5 is true");
        }
        if (treeFile2.exists()) {
            System.out.println("Why is this trueeeee");
        }
    }
}
