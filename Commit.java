import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.LinkedList;

public class Commit {

    static String prevCommitSha;
    static String prevCommitContents1; //commit contents before the line we skip
    static String prevCommitContents2; //commit contents after the line we skip

    //first commit ever
    public Commit (String author, String summary) throws IOException
    {
        StringBuilder total = new StringBuilder (""); //contains everything needed to make sha1 of commit name
        String treeSha = treeToObjects ();
        String date = getDate ();

        total.append (treeSha);
        total.append ("\n");//no parent commit
        total.append ("\n\n" + author);
        total.append ("\n" + date);
        total.append ("\n" + summary);
        String commitSha = convertToSha1 (total.toString());

        prevCommitSha = commitSha; //so that the later commits can find the previous commit
        prevCommitContents1 = treeSha + " \n";
        prevCommitContents2 = author + "\n" + date + "\n" + summary;

        File commitList = new File ("test/objects/" + commitSha);
        PrintWriter pw = new PrintWriter (commitList);
        pw.println (treeSha);
        pw.println (""); //no parent because it's the first one
        pw.println (""); //no next commit yet
        pw.println (author);
        pw.println (date);
        pw.println (summary);

        pw.close();
    }

    //not the first commit
    public Commit (String parentCommit, String author, String summary) throws IOException
    {
        StringBuilder total = new StringBuilder ("");
        String treeSha = treeToObjects ();
        String date = getDate ();

        total.append (treeSha);
        total.append ("\n" + parentCommit);
        total.append ("\n\n" + author);
        total.append ("\n" + date);
        total.append (summary);
        String commitSha = convertToSha1 (total.toString());

        File commitList = new File ("test/objects/" + commitSha);
        PrintWriter pw = new PrintWriter (commitList);
        pw.println (treeSha);
        pw.println (parentCommit);
        pw.println (""); //no next commit yet
        pw.println (author);
        pw.println (date);
        pw.println (summary);

        pw.close();

        //now i need to update the old commit so that it points to the next
        PrintWriter pww = new PrintWriter (prevCommitSha);
        pww.println (prevCommitContents1);
        pww.println ("\n");
        pww.println (commitSha); //this is the sha value for the current commit
        pww.println (prevCommitContents2);
        pww.close();

        prevCommitSha = commitSha; //updates the prevCommitSha to get ready for the next one
    }

    //creates an object Tree and saves it to the objects folder, returns the sha value of the tree contents
    public String treeToObjects () throws IOException
    {
        Tree tr = new Tree ();
        String s  = tr.convertToSha1 ("placeholder");
        File treeFile = new File ("test/objects/" + s);
        treeFile.createNewFile ();
        return s;
    }
    public String getDate ()
    {
        return "July. 14. 2004";
    }

    /*
     * copied from Tree class, converts given String into a sha1 code
     */
    public String convertToSha1(String fileContents) {

    String sha1 = "";

    try {
        MessageDigest 
        crypt = 
        MessageDigest.getInstance("SHA-1");
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
private static
String byteToHex(final
byte[] hash) {
Formatter 
formatter = 
new Formatter();
for (byte b : hash) {
formatter.format("%02x",b);
}
String result = formatter.toString();
formatter.close();
return result;
}


public static void main (String [] args) throws IOException
{
    //i still haven't figured out junit tests, so i test this way
    Commit c0 = new Commit ("Mr. Stout", "Did I ever tell you I like durian?");
    Commit c1 = new Commit ("dc80b65beb1bf8398a6d7fd3e6c15d7524624276", "Mr. Stout", "I like pugs");
    
    //well, it's certainly creating files. However, the sha1 method does not appear to be working properly
    //at least, the sha1 values being generated are not correct
    //talk to grady/whoever got this before me during class

    //also the 'next' pointer is not working
}
}
