package Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Logger implements Runnable{
    private File logfile;
    private ArrayList<String> lines;

    public Logger(){
        Config c = new Config();
        logfile = new File(c.logfile);
        lines = new ArrayList<>();
    }

    public void addLine(String line){
        this.lines.add(line);
    }

    @Override
    public void run() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(logfile, true));
            synchronized (logfile){
                lines.forEach(l->printWriter.println(l));
                printWriter.flush();
                lines = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
