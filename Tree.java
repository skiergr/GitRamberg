import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tree {
    private ArrayList<String> entries;

    public Tree() throws IOException {
        entries = new ArrayList<String>();
        File file = new File("test/objects/tree");
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    
    public void add (String line ) throws IOException {
        entries.add(line);
        printHash();
    }

    public void remove(String enter) throws IOException {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).contains(enter)) {
                entries.remove(i);
            }
        }
        printHash();
    }
    
    public void printHash () throws IOException {
        File file = new File("./test/objects/tree");
        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < entries.size(); i++) {
            writer.write(entries.get(i) + "\n");
        }
        writer.close();
    }

}
