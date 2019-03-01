package Server.Routes;
import Server.HttpResponse;
import Server.HttpStatus;
import Server.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;

public class MainPage extends Route{
    public MainPage(){
        this.name="/";
    }

    @Override
    public HttpResponse getResponse() throws IOException {
        TemplateProcessor templateProcessor=new TemplateProcessor("html/index.html");
        HashMap<String, String> vars=new HashMap();
        vars.put("%title", "Startseite");
        vars.put("%value", "Überschrift");
        vars.put("%results", "unterschrift");
        return new HttpResponse(HttpStatus.Ok, templateProcessor.replace(vars));
    }
}
