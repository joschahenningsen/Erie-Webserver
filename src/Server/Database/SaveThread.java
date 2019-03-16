package Server.Database;

import java.io.*;
import java.util.ArrayList;

public class SaveThread implements Runnable{
    private final String[] headers;
    private ArrayList<String> data;
    private String fileName;

    public SaveThread(ArrayList<String> data, String fileName, String[] headers){
        this.data = data;
        this.fileName = fileName;
        this.headers=headers;
    }

    @Override
    public void run() {
        System.out.println("Saving db");
        synchronized (data) {
            File f = new File(fileName);
            FileOutputStream fileOutputStream=null;
            try {
                fileOutputStream=new FileOutputStream(f, false);
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
            data.forEach(date -> printWriter.println(date));
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
