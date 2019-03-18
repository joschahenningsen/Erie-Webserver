package Server.Database;

/**
 * Class visitors extend
 * @author Joscha Henningsen
 */
public abstract class Visitor {
    public abstract void visit(Var var);

    public abstract void visit(Val val);

    public abstract void visit(SelectQuery selectQuery);

    public abstract void visit(Expr expr);

    public abstract void visit(Cond cond);

    public abstract void visit(Limit limit);

    public abstract void visit(UpdateQuery updateQuery);
}
