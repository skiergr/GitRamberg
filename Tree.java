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
    Index index;

    public static void main(String[] args) throws Exception {
        Tree tree = new Tree();
        tree.addDirectory("directorytest");

    }

    private ArrayList<String> entries;

    private String currentFileName;

    public Tree() throws IOException {
        index = new Index();
        index.init();

        entries = new ArrayList<String>();

        sha = convertToSha1("");

        File file = new File("test/objects/" + sha);

        this.currentFileName = "test/objects/" + sha;

        if (!file.exists()) {

            file.createNewFile();

        }
    }

    public static String convertToSha1(File file) throws Exception {
        String contents = "";

        BufferedReader br = new BufferedReader(new FileReader(file));

        while (br.ready()) {

            contents += (char) br.read();
        }

        br.close();

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
        contents += line + "\n";
        sha = convertToSha1(contents);
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

        /*
         * BufferedReader br = new BufferedReader(new FileReader(currentFileName));
         * 
         * while (br.ready()) {
         * contents += (char) br.read();
         * }
         * br.close();
         */

        for (int i = 0; i < entries.size(); i++) {
            contents += entries.get(i) + "\n";
        }

        sha = convertToSha1(contents);

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
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            throw new Exception("Directory does not exist");
        }
        File[] list = directory.listFiles();
        for (int i = 0; i < list.length; i++) {
            File file = list[i];
            if (file.exists()) {
                if (file.isDirectory()) {
                    Tree childTree = new Tree();
                    add("tree : " + childTree.addDirectory(file.getPath()) + " : " + file.getName());
                } else if (file.isFile()) {
                    String shaContents = convertToSha1(file);
                    add("blob : " + shaContents + " : " + file.getName());
                }
            } else {
                throw new Exception("File or directy path does not exist");
            }

        }

        currentFileName = "test/objects/" + sha;
        printHash();
        return sha;

    }

    public String getSHA() {
        return sha;

    }

}
