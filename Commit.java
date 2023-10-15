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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.BufferedReader;

public class Commit {

    String commitSha;
    String prevCommitSha;
    String treeSha;
    String prevTree;

    public static void main(String[] args) throws Exception {
        File file = new File("test.txt");
        Utils.createNewFile(file, "testing");
        Commit commit = new Commit("name", "summary");
        Utils.deleteFile(file);

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
        String testSha = Utils.getSHA(Utils.getFileContents(new File("./test/objects/" + tree.getSHA())));
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
        prevTree = br.readLine();
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
            if (line.contains("*deleted*")) {
                tree.deleteFile(prevTree, line);
            }
            if (line.contains("*edited*")) {
                tree.editFile(prevTree, line);
            }
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

    public static void checkout(String SHA1) throws Exception {
        ArrayList<String> whyIsThisHardCoded = new ArrayList<String>(Arrays.asList(".vscode", "Commit Screenshots",
                "lib", "test", "Blob.java", "codeConfig.json", "Commit.java", "Index.java", "JUnitBlobTester.java",
                "JUniteCommitTester.java", "JUniteIndexTester.java", "JUniteTreeTester.java", "README.md", "Tree.java",
                "Utils.java"));
        deleteFiles(whyIsThisHardCoded);
        String rootTree = getTreeShaFromSha(SHA1);
        createFilesFromSha(rootTree, "./GITRAMBERG");

    }

    public static void deleteFiles(ArrayList<String> hardCoded) {
        File git = new File("./GITRAMBERG");
        File[] listFiles = git.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (!hardCoded.contains(listFiles[i].getName())) {
                listFiles[i].delete();
            }
        }
    }

    public static void createFilesFromSha(String treeSha, String relativePath) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File("./test/objects/" + treeSha)));
        String line;
        HashMap<String, String> fileMap = new HashMap<String, String>();

        while (br.ready()) {
            line = br.readLine();
            if (line.contains("blob")) {
                fileMap.put(line.substring(50, line.length()), line.substring(7, 47));
            }
            if (line.contains("tree")) {
                if (line.length() > 47) {
                    File dir = new File(relativePath + "/" + line.substring(50, line.length()));
                    dir.mkdir();
                    createFilesFromSha(line.substring(7, 47), relativePath + "/" + line.substring(50, line.length()));
                } else {
                    createFilesFromSha(treeSha, "");
                }
            }
        }
        createFiles(fileMap, "");
        br.close();
    }

    public static String getTreeShaFromSha(String sha) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File("./test/objects/" + sha)));
        String line = br.readLine();
        br.close();
        return line;
    }

    public static void createFiles(HashMap<String, String> map, String relativePath) throws Exception {
        String contents;
        File newFile;
        for (String key : map.keySet()) {
            contents = Utils.getFileContents(new File("./test/objects/" + map.get(key)));
            newFile = new File(relativePath + "/" + key);
            Utils.createNewFile(newFile, contents);
        }
    }

}
