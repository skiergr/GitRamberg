import java.io.IOException;

public class IndexTester {
    public static void main(String[] args) throws IOException {
        Index index = new Index();
        index.init();
        index.add("testfile.txt");
    }
}
