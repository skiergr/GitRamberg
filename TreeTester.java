import java.io.IOException;

public class TreeTester {
    public static void main (String [] args) throws IOException {
        Index index = new Index ();
        index.init();
        Tree tree = new Tree();
        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        tree.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        tree.remove("file1.txt");
        tree.remove("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
    }
}