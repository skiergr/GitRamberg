import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Index {
    // Key = original file name
    // Value = sha1 file contents
    HashMap<String, String> indexMap;
    HashMap<String, String> treeMap;
    ArrayList<String> deleteMap;
    String indexRelativePath;
    String objectsRelativePath;

    public Index() throws Exception {
        indexMap = new HashMap<String, String>();
        treeMap = new HashMap<String, String>();
        objectsRelativePath = "./test/objects";
        indexRelativePath = "./test/index";
    }

    public void init() throws IOException {
        File objectsFile = new File(objectsRelativePath);
        File indexFile = new File(indexRelativePath);
        if (!objectsFile.exists()) {
            objectsFile.mkdirs();
            indexFile.createNewFile();
        }
        if (!indexFile.isFile()) {
            indexFile.createNewFile();
        }
    }

    public void add(String fileName) throws Exception {
        File file = new File(fileName);
        if (file.isDirectory()) {
            Tree tree = new Tree();
            treeMap.put(fileName, tree.addDirectory(fileName));

        }
        if (file.isFile()) {
            Blob blob = new Blob(fileName);
            String sha1 = blob.getsha1Contents();
            indexMap.put(fileName, sha1);

        }
        rewriteIndex();
    }

    public void delete(String fileName) throws IOException {
        deleteMap.add(fileName);
        rewriteIndex();
    }

    public void rewriteIndex() throws IOException {
        FileWriter writer = new FileWriter(indexRelativePath);
        for (String fileName : indexMap.keySet()) {
            writer.write("blob : " + indexMap.get(fileName) + " : " + fileName + "\n");
        }
        for (String directoryName : treeMap.keySet()) {
            writer.write("tree : " + treeMap.get(directoryName) + " : " + directoryName + "\n");
        }
        for (int i = 0; i < deleteMap.size(); i++) {
            writer.write("*deleted* " + deleteMap.get(i) + "\n");
        }

        writer.close();
    }
}