package Server.Database;

public class Var implements QueryComponent{

    private String name;

    public Var(String name){
        if(name.indexOf(";")!=-1)
            throw new QueryException("Illegal vaiable name");
        this.name=name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
