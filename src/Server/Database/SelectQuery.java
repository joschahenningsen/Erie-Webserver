package Server.Database;

/**
 * A Select query
 * @author Joscha Henningsen
 */
public class SelectQuery extends Query {
    private Var[] vars;
    private Cond cond;

    SelectQuery(Var[] vars, Cond cond){
        this.vars = vars;
        this.cond = cond;
    }

    Var[] getVars() {
        return vars;
    }

    Cond getCond() {
        return cond;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
