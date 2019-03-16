package Server.Database;

public abstract class Visitor {
    public abstract void visit(Var var);

    public abstract void visit(Val val);

    public abstract void visit(SelectQuery selectQuery);

    public abstract void visit(Expr expr);

    public abstract void visit(Cond cond);
}
