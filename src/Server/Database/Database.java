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
    private ArrayList<String> data;
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
        FileReader fileReader;
        BufferedReader bufferedReader;
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


    /**
     * Querys the database.
     * At the moment only Select statement usable, example:
     * @param query input query like "Select user, password Where (id='5') And (time>=42)"
     * @return ArrayList with results
     */
    public ArrayList<String[]> query(String query){
        String[] queryKeywords = query.split(" ");
        if (queryKeywords[0].toUpperCase().equals("SELECT")){
            int splitPos=0;
            for (int i = 1; i < queryKeywords.length; i++) {
                if (queryKeywords[i].toUpperCase().equals("WHERE")){
                    splitPos = i;
                    break;
                }
            }
            String vars="";
            for (int i = 1; i < splitPos; i++) {
                vars+=queryKeywords[i].replaceAll(" ", "");
            }
            String[] varNames = vars.split(",");
            Var[] varsArr = new Var[varNames.length];
            for (int i = 0; i < varsArr.length; i++) {
                varsArr[i] = new Var(varNames[i]);
            }

            String conditions = "";
            for (int i = splitPos+1; i < queryKeywords.length; i++) {
                conditions += queryKeywords[i]+" ";
            }

            Cond cond = new Cond(conditions, 0, 0);
            SelectQuery q = new SelectQuery(varsArr, cond);
            EvaluationVisitor evaluationVisitor = new EvaluationVisitor(this, q);
            return evaluationVisitor.evaluate();
        }
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
