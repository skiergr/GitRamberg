import java.io.IOException;

public class tester {
    public static void main(String[] args) throws IOException {
        Blob blob = new Blob("testFile.txt");
        System.out.println(blob.getContents());
        System.out.println(blob.getsha1Contents());

    }
}
