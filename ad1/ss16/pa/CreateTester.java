package ad1.ss16.pa;

import java.io.FileWriter;
import java.io.IOException;

public class CreateTester {

    private static final String path = "Instances/my";

    public static void main(String[] args) {
        String text = "nodes 1000\n";
        for(int i=1; i < 1000; i++) {
            text += (i-1) + " " + i + "\n";
        }
        text += "structure 999 1 false\n" +
                "add\n" +
                "0 999\n" +
                "structure 1000 1 true";
        try {
            FileWriter fw = new FileWriter(path);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
