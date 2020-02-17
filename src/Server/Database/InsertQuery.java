package Server.Database;

public class InsertQuery extends Query {
    private Val[] vals;

    InsertQuery(Val[] vals) {
        this.vals = vals;
    }

    Val[] getVals() {
        return vals;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
