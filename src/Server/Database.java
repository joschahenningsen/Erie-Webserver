package Server;

import java.io.*;
import java.util.ArrayList;

public class Database {
    private String[] headers;
    private String name;
    private ArrayList<String> data;
    private int cacheTime;
    private long lastUpdate;
    private String fileExtention=".jwsdb";

    /**
     * Database Constructor.
     * Loads Database from filesystem if it exists, creates it otherwise
     * @param name name for database table
     * @param headers String for the table columns, separate names with ";"
     * @param cacheTime time for the database to be cached in ram in seconds
     */
    public Database(String name, String headers, int cacheTime){
        this.name=name;
        this.headers=headers.split(";");
        this.cacheTime=cacheTime;
        data=new ArrayList<>();
        File f=new File(name+fileExtention);
        if(f.exists()){
            loadDatabase();
        }else {
            saveDatabase();
        }
    }

    private void saveDatabase() {
        File f = new File(name+fileExtention);
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileOutputStream);
        for (int i = 0; i < headers.length; i++) {
            if (i<headers.length-1) {
                printWriter.print(headers[i] + ";");
            }else{
                printWriter.print(headers[i]+"\n");
            }
        }
        data.forEach(date -> printWriter.println(date));
        printWriter.close();
        printWriter.flush();
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastUpdate = System.currentTimeMillis() / 1000;
    }

    private void loadDatabase() {
        File f = new File(name+fileExtention);
        FileInputStream fileInputStream = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(f);
            bufferedReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            saveDatabase();
            return;
        }
        try {
            headers = bufferedReader.readLine().split(";");
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferedReader.lines().forEach(l -> data.add(l));
        data.forEach(d->System.out.println(d));
    }

    public void put(String[] date){
        synchronized (data){
            loadDatabase();
            String dateTemp="";
            for (int i = 0; i < date.length; i++) {
                dateTemp += date[i];
                if (i<date.length-1)
                    dateTemp += ";";
            }
            data.add(dateTemp);
            saveDatabase();
        }
    }

}
