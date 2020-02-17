package Server.Database;

/**
 * Value in query's condition
 *
 * @author Joscha Henningsen
 */
public class Val implements QueryComponent {
    private String content;

    Val(String content) {
        this.content = content;
    }

    String getContent() {
        return content;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
