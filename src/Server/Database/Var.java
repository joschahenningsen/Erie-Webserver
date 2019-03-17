package Server.Database;

/**
 * Variable pointer in queries
 * @author Joscha Henningsen
 */
public class Var implements QueryComponent{

    private String name;

    Var(String name){
        if(name.contains(";"))
            throw new QueryException("Illegal vaiable name");
        this.name=name;
    }

    String getName() {
        return name;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
