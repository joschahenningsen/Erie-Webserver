package Server.Routes;

import Server.HttpRequest;
import Server.HttpResponse;
import java.io.IOException;

public abstract class Route {
    protected String name;
    protected HttpRequest requestData;

    public void setRequestData(HttpRequest requestData){
        this.requestData=requestData;
    }

    public String getName(){
        return name;
    }

    public abstract HttpResponse getResponse() throws IOException;
}
