package Server.Database;

import java.util.ArrayList;

/**
 * Visitor that handles query evaluations
 *
 * @author Joscha Henningsen
 */
public class EvaluationVisitor extends Visitor {

    private Database database;
    private Query query;
    private ArrayList<String[]> results;
    private int[] selectedFields;
    private String[] currentData;
    private String currentEvaluation;
    private boolean currentEvaluationRes;
    private int limit;

    EvaluationVisitor(Database database, Query query) {
        this.database = database;
        this.query = query;
        results = new ArrayList<>();
    }

    @Override
    public void visit(Var var) {
        currentEvaluation = currentData[getIndices(new Var[]{var})[0]];
    }

    @Override
    public void visit(Val val) {
        currentEvaluation = val.getContent();
    }

    @Override
    public void visit(SelectQuery selectQuery) {
        selectedFields = getIndices(selectQuery.getVars());
        if (selectQuery.getLimit() != null) {
            selectQuery.getLimit().accept(this);
        } else {
            this.limit = -1;
        }
        while (database.hasNext() && (results.size() < limit || limit == -1)) {
            this.currentData = database.next();
            selectQuery.getCond().accept(this);
            if (currentEvaluationRes) {
                String[] res = new String[selectedFields.length];
                for (int i = 0; i < selectedFields.length; i++) {
                    res[i] = currentData[selectedFields[i]];
                }
                results.add(res);
            }
        }
    }

    @Override
    public void visit(Expr expr) {
        if (expr.getVal() != null)
            expr.getVal().accept(this);
        if (expr.getVar() != null)
            expr.getVar().accept(this);
    }

    @Override
    public void visit(Cond cond) {
        if (cond.getCompOp() != null) {
            cond.lExpr.accept(this);
            String l = currentEvaluation;
            cond.rExpr.accept(this);
            String r = currentEvaluation;
            try {
                switch (cond.getCompOp()) {
                    case Equals:
                        currentEvaluationRes = l.equals(r);
                        break;
                    case Greater:
                        currentEvaluationRes = Double.parseDouble(l) > Double.parseDouble(r);
                        break;
                    case Less:
                        currentEvaluationRes = Double.parseDouble(l) < Double.parseDouble(r);
                        break;
                    case GreaterEquals:
                        currentEvaluationRes = Double.parseDouble(l) >= Double.parseDouble(r);
                        break;
                    case LessEquals:
                        currentEvaluationRes = Double.parseDouble(l) <= Double.parseDouble(r);
                        break;
                    case NotEquals:
                        currentEvaluationRes = !l.equals(r);
                        break;
                    default:
                        currentEvaluationRes = false;
                        break;
                }
            } catch (NumberFormatException e) {
                throw new QueryException("Comparison " + cond.getCompOp() + " not suitable for Strings.");
            }

        } else {
            cond.lCond.accept(this);
            boolean lRes = currentEvaluationRes;
            cond.rCond.accept(this);
            boolean rRes = currentEvaluationRes;
            switch (cond.getCondOp()) {
                case And:
                    currentEvaluationRes = lRes && rRes;
                    break;
                case Or:
                    currentEvaluationRes = lRes || rRes;
                    break;
            }
        }
    }

    @Override
    public void visit(Limit limit) {
        this.limit = limit.getLimit();
    }

    @Override
    public void visit(UpdateQuery updateQuery) {
        selectedFields = getIndices(updateQuery.getVars());
        while (database.hasNext()) {
            this.currentData = database.next();
            updateQuery.getCond().accept(this);
            if (currentEvaluationRes) {
                for (int i = 0; i < selectedFields.length; i++) {
                    updateQuery.getVals()[i].accept(this);
                    currentData[selectedFields[i]] = currentEvaluation;
                }
                database.setCurrent(currentData);
            }
        }
    }

    @Override
    public void visit(InsertQuery insertQuery) {
        Val[] vals = insertQuery.getVals();
        if (vals.length != database.getHeaders().length)
            throw new QueryException("Invalid number of values for Insert");
        String[] fields = new String[vals.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = vals[i].getContent();
        }
        database.put(fields);
    }

    ArrayList<String[]> evaluate() {
        query.accept(this);
        database.setIndex(0);
        return results;
    }

    private int[] getIndices(Var[] vars) {
        int[] selectedFields = new int[vars.length];
        String[] databaseFields = database.getHeaders();
        for (int i = 0; i < selectedFields.length; i++) {
            for (int j = 0; j < databaseFields.length; j++) {
                if (vars[i].getName().equals(databaseFields[j])) {
                    selectedFields[i] = j;
                    break;
                } else if (j == databaseFields.length - 1) {
                    throw new QueryException("Column " + vars[i].getName() + " doesn't exist in Database");
                }
            }
        }
        return selectedFields;
    }

}
