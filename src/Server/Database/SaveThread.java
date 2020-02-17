package Server.Database;

import java.io.*;
import java.util.ArrayList;

/**
 * Separate Thread that saves the database after changes, in order to prevent
 * long waiting times after changing db fields
 *
 * @author Joscha Henningsen
 */
public class SaveThread implements Runnable {
    private final String[] headers;
    private final ArrayList<String> data;
    private String fileName;

    SaveThread(ArrayList<String> data, String fileName, String[] headers) {
        this.data = data;
        this.fileName = fileName;
        this.headers = headers;
    }

    @Override
    public void run() {
        System.out.println("Saving db");
        synchronized (data) {
            File f = new File(fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(f, false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            for (int i = 0; i < headers.length; i++) {
                if (i < headers.length - 1) {
                    printWriter.print(headers[i] + ";");
                } else {
                    printWriter.print(headers[i] + "\n");
                }
            }
            data.forEach(printWriter::println);
            printWriter.flush();
            printWriter.close();
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
