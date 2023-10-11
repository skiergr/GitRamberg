import java.io.File;

public class commitmain {
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
        File directory3 = new File("directory3");
        Utils.createNewDirectory(directory3);
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
        String absolutePath = treeFile.getAbsolutePath();
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
                + " : test7\ntree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : directory3\n";
        String treeSha4 = Utils.getSHA(treeContents4);
        String commitContents4 = treeSha4 + "\n" + c2.getCommitSha() + "\n\nname\n" + Utils.getDate()
                + "\nsummary";
        String commitSha4 = Utils.getSHA(commitContents4);
        File treeFile4 = new File("./test/objects/" + treeSha4);
        File commitFile4 = new File("./test/objects/" + commitSha4);
    }
}
