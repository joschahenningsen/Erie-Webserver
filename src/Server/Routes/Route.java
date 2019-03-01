package Server.Routes;

import Server.HttpResponse;
import Server.TemplateProcessor;

import java.io.IOException;

public abstract class Route {
    protected String name;

    public String getName(){
        return name;
    }

    public abstract HttpResponse getResponse() throws IOException;
}
