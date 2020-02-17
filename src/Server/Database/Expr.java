package Server.Database;

/**
 * Expression in DB query
 *
 * @author Joscha Henningsen
 */
public class Expr implements QueryComponent {
    private Var var;

    private Val val;

    Expr(Var var) {
        this.var = var;
    }

    Expr(Val val) {
        this.val = val;
    }

    Var getVar() {
        return var;
    }

    Val getVal() {
        return val;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
