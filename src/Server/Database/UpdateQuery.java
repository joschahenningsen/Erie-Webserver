package Server.Database;

/**
 * A Update Query
 * @author Joscha Henningsen
 */
public class UpdateQuery extends Query {
    private Var[] vars;
    private Cond cond;
    private Val[] vals;

    UpdateQuery(Var[] vars, Cond cond, Val[] vals) {
        this.vars = vars;
        this.cond = cond;
        this.vals = vals;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public Var[] getVars() {
        return vars;
    }

    public Cond getCond() {
        return cond;
    }

    public Val[] getVals() {
        return vals;
    }
}
