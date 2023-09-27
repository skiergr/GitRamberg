import java.io.BufferedReader;

import java.io.File;

import java.io.FileReader;

import java.io.FileWriter;

import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;

import java.util.Formatter;

public class Tree {

    String sha;

    public static void main(String[] args) throws Exception {
        Tree tree = new Tree();
    }

    private ArrayList<String> entries;

    private String currentFileName;

    public Tree() throws IOException {

        entries = new ArrayList<String>();

        sha = convertToSha1("");

        File file = new File("test/objects/" + sha);

        this.currentFileName = "test/objects/" + sha;

        if (!file.exists()) {

            file.createNewFile();

        }
    }

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

    private static String byteToHex(final byte[] hash) {

        Formatter formatter = new Formatter();

        for (byte b : hash) {

            formatter.format("%02x", b);

        }

        String result = formatter.toString();

        formatter.close();

        return result;

    }

    public void add(String line) throws IOException {

        entries.add(line);

        String contents = "";

        BufferedReader br = new BufferedReader(new FileReader(currentFileName));

        while (br.ready()) {

            contents += (char) br.read();
        }

        br.close();

        String sha = convertToSha1(contents);
        File file = new File("test/objects/" + sha);
        File file2 = new File(currentFileName);

        file2.delete();
        file.createNewFile();

        currentFileName = "test/objects/" + sha;

        printHash();

    }

    public void remove(String enter) throws IOException {

        for (int i = 0; i < entries.size(); i++) {

            if (entries.get(i).contains(enter)) {
                entries.remove(i);
            }
        }

        String contents = "";

        BufferedReader br = new BufferedReader(new FileReader(currentFileName));

        while (br.ready()) {
            contents += (char) br.read();
        }
        br.close();

        String sha = convertToSha1(contents);

        File file = new File("test/objects/" + sha);
        File file2 = new File(currentFileName);

        file2.delete();
        file.createNewFile();

        currentFileName = "test/objects/" + sha;
        printHash();

    }

    public void printHash() throws IOException {

        File file = new File(currentFileName);

        FileWriter writer = new FileWriter(file);

        for (int i = 0; i < entries.size(); i++) {
            writer.write(entries.get(i)
                    + "\n");
        }

        writer.close();
    }

    public String addDirectory(String directoryPath) throws Exception {
        Tree tree = new Tree();
        File directory = new File(directoryPath);

        File[] list = directory.listFiles();
        for (int i = 0; i < list.length; i++) {
            File file = list[i];
            if (file.exists()) {
                if (file.isDirectory()) {
                    String childTree = addDirectory(file.getPath());
                    tree.add("tree : " + childTree + " : " + file.getName());
                }
                if (file.isFile()) {
                    Blob blob = new Blob(file.getPath());
                    tree.add("blob : " + blob.getsha1Contents() + " : " + file.getName());
                }
            } else {
                throw new Exception("File or directy path does not exist");
            }

        }

        sha = tree.getSha();
        return sha;

    }

    public String getSha() {
        return sha;
    }

}
