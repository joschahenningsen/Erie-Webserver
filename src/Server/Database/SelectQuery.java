package Server.Database;

public class SelectQuery extends Query {
    Var[] vars;
    Cond cond;

    public SelectQuery(Var[]vars, Cond cond){
        this.vars = vars;
        this.cond = cond;
    }

    public Var[] getVars() {
        return vars;
    }

    public Cond getCond() {
        return cond;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
