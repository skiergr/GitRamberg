import java.io.*;

public class Blob {
    public Blob(String fileName) {

    }

    public String getFileContents(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder contents = new StringBuilder();
        while (reader.ready()) {
            contents.append((char) reader.read());
        }
        reader.close();
        return contents.toString();
    }
}
