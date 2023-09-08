import java.io.IOException;

public class tester {
    public static void main(String[] args) throws IOException {
        Blob blob = new Blob(".\\testfile.txt");
        System.out.println(blob.getContents());
        System.out.println(blob.getsha1Contents());
        System.out.println(blob.getContents());
        System.out.println(blob.getsha1Contents());

    }
}
