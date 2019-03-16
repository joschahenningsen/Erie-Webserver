package Server.Database;

public class Cond implements QueryComponent{
    Cond lCond;
    Cond rCond;
    CondOp condOp;

    Expr lExpr;
    Expr rExpr;
    CompOp compOp;

    private boolean isLeaf;

    public Cond(Cond lCond, CondOp condOp, Cond rCond){
        this.lCond=lCond;
        this.rCond=rCond;
        this.condOp=condOp;
        isLeaf=false;
    }

    public Cond(Expr lExpr, CompOp compOp, Expr rExpr){
        this.lExpr=lExpr;
        this.rExpr=rExpr;
        this.compOp=compOp;
        isLeaf=true;
    }

    public Cond getlCond() {
        return lCond;
    }

    public Cond getrCond() {
        return rCond;
    }

    public CondOp getCondOp() {
        return condOp;
    }

    public Expr getlExpr() {
        return lExpr;
    }

    public Expr getrExpr() {
        return rExpr;
    }

    public CompOp getCompOp() {
        return compOp;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
