package Server.Database;

/**
 * A Select query
 *
 * @author Joscha Henningsen
 */
public class SelectQuery extends Query {
    private Var[] vars;
    private Cond cond;
    private Limit limit;

    SelectQuery(Var[] vars, Cond cond, Limit limit) {
        this.vars = vars;
        this.cond = cond;
        this.limit = limit;
    }

    Var[] getVars() {
        return vars;
    }

    Cond getCond() {
        return cond;
    }

    Limit getLimit() {
        return limit;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
