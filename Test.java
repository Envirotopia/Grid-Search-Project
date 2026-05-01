import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;
import java.io.File ;

public class Test {

    public static void main(String[] args) throws Exception {


        //Redirect their print statements so it doesn't mess up the test
        PrintStream out = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int arg0) throws IOException {
            }
        }));

        double score = 0.; //1 point for compiling
        final StringBuilder testNotes = new StringBuilder(); // Track errors

        try {
             double heapScore = HeapTests.heapTests();
             score += heapScore ;
             if ( heapScore < 6 ) {
                testNotes.append( "Your code didn't pass all of the Heap tests\n" );
             }
        } catch (Exception E) {
            testNotes.append( "Your program threw one or more errors while testing your Heap\n" );
        }

        try {
            double searchScore = SearchTests.searchTests();
            score += searchScore ;
            if ( searchScore < 6 ) {
                testNotes.append( "Your code didn't pass all of the Search tests\n" );
            }
        } catch (Exception E) {
            testNotes.append( "Your program threw one or more errors while testing the Search class\n" );
        }

        //Finish up notes
        testNotes.append( "Your score is " + score + "/12" );
        
        FileWriter fw = new FileWriter("sysout.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(testNotes.toString());
        bw.close();

        System.setOut(out);
        System.out.println("{ \"score\": " + score + ", \"stdout_visibility\": \"visible\"}");
    }
}
