import java.io.IOException;

public class IndexTester {
    public static void main(String[] args) throws IOException {
        Index index = new Index();
        index.init();
        index.add("testFile");
        index.add("testfile2");
        index.add("testfile3");
        //index.remove("testFile");
    }
}
