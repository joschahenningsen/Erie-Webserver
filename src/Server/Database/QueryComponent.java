package Server.Database;

public interface QueryComponent {
    public void accept(Visitor v);
}
