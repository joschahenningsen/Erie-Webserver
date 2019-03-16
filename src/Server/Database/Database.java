package Server.Database;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple to use in memory Database
 * @author Joscha Henningsen
 */
public class Database implements Iterator<String[]>{
    private String[] headers;
    private String name;
    public ArrayList<String> data;
    private int cacheTime;
    private long lastUpdate;
    private String fileExtention=".jwsdb";
    private int index;

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
        this.cacheTime=cacheTime*1000;
        data=new ArrayList<>();
        File f=new File(name+fileExtention);
        if(f.exists()){
            loadDatabase();
        }else {
            saveDatabase();
        }
    }


    private void saveDatabase() {
        Thread saveThread=new Thread(new SaveThread(data, name+fileExtention, headers));
        saveThread.start();
        lastUpdate = System.currentTimeMillis();
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
        data = new ArrayList<>();
        synchronized (data) {
            bufferedReader.lines().forEach(l -> data.add(l));
        }
    }

    public void put(String[] date){
        synchronized (data){
            String dateTemp="";
            for (int i = 0; i < date.length; i++) {
                dateTemp += date[i];
                if (i<date.length-1)
                    dateTemp += ";";
            }
            data.add(dateTemp);
            if ((System.currentTimeMillis())-(lastUpdate+cacheTime)>=0)
                saveDatabase();
        }
    }


    public ArrayList<String[]> get(String query){
        String[] queryKeywords = query.split(" ");

        return null;
    }

    public String[] getHeaders() {
        return headers;
    }

    @Override
    public boolean hasNext() {
        return index<data.size();
    }

    @Override
    public String[] next() {
        return data.get(index++).split(";");
    }
}
