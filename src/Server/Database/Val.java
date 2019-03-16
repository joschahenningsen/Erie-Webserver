package Server.Database;

public class Val implements QueryComponent{
    public String content;

    public Val(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
