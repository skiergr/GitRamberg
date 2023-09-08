import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class Index {
    HashMap<String, String> indexMap;
    String indexRelativePath;
    String objectsRelativePath;

    public Index() {
        indexMap = new HashMap<String, String>();
        objectsRelativePath = ".\\test\\objects";
        indexRelativePath = ".\\test\\objects\\index.txt";
    }

    public void init() throws IOException {
        File objectsFile = new File(objectsRelativePath);
        File indexFile = new File(indexRelativePath);
        if (objectsFile.exists()) {
            if (!indexFile.isFile()) {
                indexFile.createNewFile();
            }
        } else {
            objectsFile.mkdirs();
            indexFile.createNewFile();
        }
    }
}
