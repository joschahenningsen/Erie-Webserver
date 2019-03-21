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

    private void insertArray(String[] date, int index){
        String dateTemp="";
        for (int i = 0; i < date.length; i++) {
            dateTemp += date[i];
            if (i<date.length-1)
                dateTemp += ";";
        }
        if (index == -1)
            data.add(dateTemp);
        if (index != -1)
            data.set(index, dateTemp);
    }

    public void put(String[] date){
        synchronized (data){
            insertArray(date, -1);
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
        long s=System.currentTimeMillis();

        String[] queryKeywords = query.split(" ");
        if (queryKeywords[0].toUpperCase().equals("SELECT")){
            int splitPos = 0;
            int limitpos = -1;
            for (int i = 1; i < queryKeywords.length; i++) {
                if (queryKeywords[i].toUpperCase().equals("WHERE")){
                    splitPos = i;
                }else if (queryKeywords[i].toUpperCase().equals("LIMIT")){
                    limitpos = i + 1;
                }
            }
            Var[] varsArr = fetchVars(queryKeywords, splitPos);
            Cond cond = fetchCond(queryKeywords, limitpos, splitPos);

            Limit limit = null;
            if (limitpos!=-1){
                limit=new Limit(Integer.parseInt(queryKeywords[limitpos]));
            }

            SelectQuery q = new SelectQuery(varsArr, cond, limit);

            EvaluationVisitor evaluationVisitor = new EvaluationVisitor(this, q);

            long e=System.currentTimeMillis();
            System.out.println(e-s+" ms.");
            return evaluationVisitor.evaluate();
        }else if(queryKeywords[0].toUpperCase().equals("UPDATE")){
            int splitPos = -1;
            int valuepos = -1;
            for (int i = 1; i < queryKeywords.length; i++) {
                if (queryKeywords[i].toUpperCase().equals("WHERE")){
                    splitPos = i;
                }else if (queryKeywords[i].toUpperCase().equals("VALUES")){
                    valuepos = i + 1;
                }
            }
            Var[] vars = fetchVars(queryKeywords, splitPos);
            Cond cond = fetchCond(queryKeywords, valuepos, splitPos);
            Val[] vals = fetchVals(queryKeywords, valuepos);

            if (vars.length!=vals.length)
                throw new QueryException("Variable count doesn't match Value count in Update-query");

            UpdateQuery q = new UpdateQuery(vars, cond, vals);

            EvaluationVisitor evaluationVisitor = new EvaluationVisitor(this, q);
            evaluationVisitor.evaluate();
            saveDatabase();
            return new ArrayList<>();
        }else if (queryKeywords[0].toUpperCase().equals("INSERT")){
            Val[] vals = fetchVals(queryKeywords, 1);
            InsertQuery q = new InsertQuery(vals);

            EvaluationVisitor evaluationVisitor = new EvaluationVisitor(this, q);
            evaluationVisitor.evaluate();
            return new ArrayList<>();
        }
        throw new QueryException("Illegal query method");
    }

    private Val[] fetchVals(String[] queryKeywords, int splitPos) {
        String vals="";
        for (int i = splitPos; i < queryKeywords.length; i++) {
            vals+=queryKeywords[i].replaceAll(" ", "").replaceAll("'", "");
        }
        String[] varNames = vals.split(",");
        Val[] valsArr = new Val[varNames.length];
        for (int i = 0; i < valsArr.length; i++) {
            valsArr[i] = new Val(varNames[i]);
        }
        return valsArr;
    }

    private Cond fetchCond(String[] queryKeywords, int limitPos, int splitPos) {
        String conditions = "";
        int condEnd;
        condEnd = queryKeywords.length;
        if (limitPos != -1)
            condEnd=limitPos-1;
        for (int i = splitPos+1; i < condEnd; i++) {
            conditions += queryKeywords[i]+" ";
        }

        return new Cond(conditions, 0, 0);
    }

    private Var[] fetchVars(String[] queryKeywords,int splitPos) {
        String vars="";
        for (int i = 1; i < splitPos; i++) {
            vars+=queryKeywords[i].replaceAll(" ", "");
        }
        String[] varNames = vars.split(",");
        Var[] varsArr = new Var[varNames.length];
        for (int i = 0; i < varsArr.length; i++) {
            varsArr[i] = new Var(varNames[i]);
        }
        return varsArr;
    }

    String[] getHeaders() {
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

    void setIndex(int index) {
        this.index = index;
    }

    public void setCurrent(String[] currentData) {
        insertArray(currentData, index-1);
    }
}
