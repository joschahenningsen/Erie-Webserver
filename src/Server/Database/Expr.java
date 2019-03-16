package Server.Database;

public class Expr implements QueryComponent{
    Var var;

    Val val;

    public Expr(Var var){
        this.var = var;
    }

    public Expr(Val val){
        this.val = val;
    }

    public Var getVar() {
        return var;
    }

    public Val getVal() {
        return val;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
