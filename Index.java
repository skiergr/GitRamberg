import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Index {
    // Key = original file name
    // Value = sha1 file contents
    HashMap<String, String> indexMap;
    String indexRelativePath;
    String objectsRelativePath;

    public Index() {
        indexMap = new HashMap<String, String>();
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

    public void add(String fileName) throws IOException {
        Blob blob = new Blob(fileName);
        String sha1 = blob.getsha1Contents();
        indexMap.put(fileName, sha1);
        rewriteIndex();
    }

    public void remove(String fileName) throws IOException {
        String sha1 = indexMap.get(fileName);
        File blob = new File("./test/objects/" + sha1);
        blob.delete();
        indexMap.remove(fileName);
        rewriteIndex();
    }

    public void rewriteIndex() throws IOException {
        FileWriter writer = new FileWriter(indexRelativePath);
        for (String fileName : indexMap.keySet()) {
            writer.write(fileName + " : " + indexMap.get(fileName) + "\n");
        }
        writer.close();
    }
}