package Server.Database;

/**
 * Limits amount results, helps improving performance in big databases
 *
 * @author Joscha Henningsen
 */
public class Limit implements QueryComponent {
    private int limit;

    Limit(int limit) {
        this.limit = limit;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    int getLimit() {
        return this.limit;
    }
}
