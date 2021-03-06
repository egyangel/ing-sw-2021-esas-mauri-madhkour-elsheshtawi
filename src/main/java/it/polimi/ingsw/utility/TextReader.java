package it.polimi.ingsw.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Class that read text files in json format and returns string for JsonConverter
 * */
public class TextReader {

    public  String readMyText(String fname) {

        InputStream is;
        is = this.getClass().getClassLoader().getResourceAsStream(fname);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = null;
        try {
            line = buf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line).append("\n");
            try {
                line = buf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String fileAsString = sb.toString();

        return fileAsString;
    }
}
