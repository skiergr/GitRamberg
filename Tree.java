import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Tree {
    private ArrayList<String> entries;

    public Tree() {
        entries = new ArrayList<String>();
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
    //Entries are a newline String in format  typeOfFile : shaOfFile : optionalFileName
    //typeOfFile: Can be either the String 'blob' or 'tree' 
    //shaOfFile: Is a valid sha1 of a file in the objects folder
    //optionalFileName: if it's a blob, please include the filename

}
