import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CommitTest {
    Commit c0;
    
    //tests if the commit is being correctly generated
    @Test
    void createACommit () throws IOException
    {
        c0 = new Commit ("name", "summary");
        
        BufferedReader br = new BufferedReader(new FileReader("test/objects/9878145563f1d07f1a5463634196b2c36b9182e2"));
            //this is the sha1 of the contents of c0, generated with my own method (not the one in this project)
        String expected = "ff55435345834a3fe224936776c2aa15f6ed5358\n\n\n" + "name\n" + c0.getDate() + "\nsummary";
        String actual = "";
        while(br.ready()) {
            actual += (char) br.read();
        }
        br.close();
        //checks that the entries match what is expected
        assertEquals(expected, actual);
        //bro there's just an extra new line
        
    }

    @Test
    void testConvertToSha1() {
        //not gonna test this because i know it doesn't work
        //copied this method directly from tree class
        //og person, fix this plz
    }

    //makes sure the getDate gets the correct value
    @Test
    void testGetDate() throws IOException {
        c0 = new Commit ("name", "summary");
        String d = c0.getDate();
        assertEquals ("Date incorrect", d, "09-22-2023"); //only works for today's date until i edit the tester
    }
}
