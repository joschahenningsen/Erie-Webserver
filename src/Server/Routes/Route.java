package Server.Routes;

import Server.Config;
import Server.HttpRequest;
import Server.HttpResponse;
import java.io.IOException;
import java.util.HashMap;

public abstract class Route {
    protected String name;
    protected HttpRequest requestData;
    protected HashMap<String, String> vars;
    public void setRequestData(HttpRequest requestData){
        this.requestData=requestData;
        Config config=new Config();
        this.vars=config.defaultReplacements;
    }

    public String getName(){
        return name;
    }

    public abstract HttpResponse getResponse() throws IOException;
}
