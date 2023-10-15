import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;

public class Utils {
    public static String getSHA(String contents) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(contents.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    // Used for sha1
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static void createNewFile(File file, String contents) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        writeToFile(file, contents);
    }

    public static void createNewDirectory(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void writeToFile(File file, String contents) throws IOException {
        PrintWriter pw1 = new PrintWriter(new FileWriter(file));
        pw1.print(contents);
        pw1.close();
    }

    public static String getFileContents(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
        StringBuilder contents = new StringBuilder();
        while (reader.ready()) {
            contents.append((char) reader.read());
        }
        reader.close();
        return contents.toString();
    }

    public static String getDate() {
        String timeStamp = new SimpleDateFormat("MM-dd-YYYY").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}
