package Server.Database;

/**
 * Component in DB Queries
 *
 * @author Joscha Henningsen
 */
public interface QueryComponent {
    void accept(Visitor v);
}
