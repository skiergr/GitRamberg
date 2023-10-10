import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Formatter;
import java.util.LinkedList;
import java.io.BufferedReader;

public class Commit {

    String commitSha;
    String prevCommitSha;
    String treeSha;

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
        File directory = new File("/directory");
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

        System.out.println(treeFile2.exists());
        ;
    }

    // first commit ever
    public Commit(String author, String summary) throws Exception {
        Index index = new Index();
        index.init();
        StringBuilder total = new StringBuilder(""); // contains everything needed to make sha1 of commit name
        Tree tree = new Tree();
        String treeSha = createTree(tree);
        String date = getDate();

        total.append(treeSha);
        total.append("\n");// no parent commit
        total.append("\n\n" + author);
        total.append("\n" + date);
        total.append("\n" + summary);
        commitSha = convertToSha1(total.toString());

        File commitList = new File("test/objects/" + commitSha);
        PrintWriter pw = new PrintWriter(commitList);
        pw.println(treeSha);
        pw.println(""); // no parent because it's the first one
        pw.println(""); // no next commit yet
        pw.println(author);
        pw.println(date);
        pw.print(summary);

        pw.close();
    }

    // not the first commit
    public Commit(String parentCommit, String author, String summary) throws Exception {
        Index index = new Index();
        index.init();
        StringBuilder total = new StringBuilder();
        Tree tree = new Tree();
        addPreviousTree(tree, parentCommit);
        treeSha = createTree(tree);
        String date = getDate();

        total.append(treeSha);
        total.append("\n" + parentCommit);
        total.append("\n\n" + author);
        total.append("\n" + date);
        total.append("\n" + summary);
        commitSha = convertToSha1(total.toString());

        File commitList = new File("test/objects/" + commitSha);
        PrintWriter pw = new PrintWriter(commitList);
        pw.println(treeSha);
        pw.println(parentCommit);
        pw.println(""); // no next commit yet
        pw.println(author);
        pw.println(date);
        pw.print(summary);

        pw.close();
        prevCommitSha = parentCommit;
        updatePreviousCommit();

    }

    // creates an object Tree and saves it to the objects folder, returns the sha
    // value of the tree contents
    public String createTree(Tree tree) throws Exception {
        addIndexToTree(tree);
        clearFile("./test/index");

        return tree.getSHA();
    }

    public String getTreeSha() {
        return treeSha;
    }

    public void addPreviousTree(Tree tree, String parentCommmit) throws Exception {
        File parentCommitFile = new File("test/objects/" + parentCommmit);
        if (!parentCommitFile.exists()) {
            throw new Exception("parent commit doesn't exist");
        }
        BufferedReader br = new BufferedReader(new FileReader(parentCommitFile));
        String prevTree = br.readLine();
        br.close();
        tree.add("tree : " + prevTree);

    }

    public void addIndexToTree(Tree tree) throws Exception {
        File index = new File("./test/index");
        if (!index.exists()) {
            throw new Exception("file does not exist");
        }
        BufferedReader br = new BufferedReader(new FileReader(index));
        String line;
        while (br.ready()) {
            line = br.readLine();
            tree.add(line);
        }
        br.close();

    }

    public void clearFile(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new Exception("file does not exist");
        }
        file.delete();
    }

    public String getDate() {
        // return "July. 14. 2004";
        String timeStamp = new SimpleDateFormat("MM-dd-YYYY").format(Calendar.getInstance().getTime());
        return timeStamp;

    }

    /*
     * copied from Tree class, converts given String into a sha1 code
     */
    public String convertToSha1(String fileContents) {

        String sha1 = "";

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();

            crypt.update(fileContents.getBytes("UTF-8"));

            sha1 = byteToHex(crypt.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        return sha1;

    }

    // Used for sha1
    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public void updatePreviousCommit() throws Exception {
        File prevCommit = new File("./test/objects/" + prevCommitSha);
        if (prevCommit.exists()) {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(prevCommit));
            sb.append(br.readLine() + "\n");
            sb.append(br.readLine() + "\n");
            sb.append(commitSha + "\n");
            br.readLine();
            sb.append(br.readLine() + "\n");
            sb.append(br.readLine() + "\n");
            sb.append(br.readLine());
            br.close();
            PrintWriter pw = new PrintWriter(prevCommit);
            pw.write(sb.toString());
            pw.close();
        } else {
            throw new Exception("bro what");
        }
    }

    public String getCommitSha() {
        return commitSha;
    }

}
