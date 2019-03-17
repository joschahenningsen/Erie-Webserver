package Server.Database;

import java.io.ByteArrayInputStream;

/**
 * Condition for db queries
 * @author Joscha Henningsen
 */
public class Cond implements QueryComponent{
    Cond lCond;
    Cond rCond;
    private CondOp condOp;

    Expr lExpr;
    Expr rExpr;
    private CompOp compOp;

    private String[] tokens;

    private boolean isLeaf;

    Cond(String conditions, int start, int end){
        Lexer l = new Lexer(new ByteArrayInputStream(conditions.getBytes()));
        int s;
        String tokenStr="";
        while ( (s = l.nextSymbol()) != Lexer.EOF)
            if(s != Lexer.EOL){
                if (l.toString().equals("'='") && (tokenStr.endsWith("'<'")||tokenStr.endsWith("'>'")||tokenStr.endsWith("'!'"))) {
                    tokenStr = tokenStr.substring(0, tokenStr.length()-1) + l.toString().substring(1);
                }else{
                    tokenStr += ";" + l.toString();
                }
            }
        this.tokens = tokenStr.substring(1).split(";");
        if (end == 0)
            end = tokens.length;

        for (int i = start; i < end; i++) {
            switch (this.tokens[i]){
                case "'('":
                    int len = pLen(i);
                    if (len-i!=3){
                        if (lCond==null){
                            lCond=new Cond(conditions, i+1, len-1);
                        }else {
                            rCond=new Cond(conditions, i+1, len-1);
                        }
                    }

                    i = len;
                    continue;
                case "')'":

                    continue;
                case "And":
                    this.condOp = CondOp.And;
                    continue;
                case "Or":
                    this.condOp = CondOp.Or;
                    continue;
                default:
                    switch (tokens[i + 1]) {
                        case "'<'":
                            this.compOp = CompOp.Less;
                            break;
                        case "'>'":
                            this.compOp = CompOp.Greater;
                            break;
                        case "'='":
                            this.compOp = CompOp.Equals;
                            break;
                        case "'<='":
                            this.compOp = CompOp.LessEquals;
                            break;
                        case "'>='":
                            this.compOp = CompOp.GreaterEquals;
                            break;
                        case "'!='":
                            this.compOp = CompOp.NotEquals;
                            break;
                    }
                    if (tokens[i].startsWith("'")){
                        lExpr=new Expr(new Val(tokens[i].replaceAll("'", "")));
                    }else{
                        lExpr=new Expr(new Var(tokens[i]));
                    }
                    if (tokens[i+2].startsWith("'")){
                        rExpr=new Expr(new Val(tokens[i+2].replaceAll("'", "")));
                    }else{
                        rExpr=new Expr(new Var(tokens[i+2]));
                    }
                    i+=2;
            }
        }
    }

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

    CondOp getCondOp() {
        return condOp;
    }

    public Expr getlExpr() {
        return lExpr;
    }

    public Expr getrExpr() {
        return rExpr;
    }

    CompOp getCompOp() {
        return compOp;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    private int pLen(int openingIndex){
        int brackets=0;
        for (int i = openingIndex; i < tokens.length; i++) {
            if (tokens[i].equals("'('")){
                brackets++;
            }else if (tokens[i].equals("')'")){
                brackets--;
                if (brackets==0)
                    return i;
            }
        }
        return -1;
    }
}
