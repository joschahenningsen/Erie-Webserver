package Server.Routes;

import Server.HttpResponse;
import Server.HttpStatus;
import Server.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;

public class MainCss extends Route {
    public MainCss(){
        this.name="/css/main.css";
    }

    @Override
    public HttpResponse getResponse() throws IOException {
        TemplateProcessor templateProcessor=new TemplateProcessor("css/main.css");
        return new HttpResponse(HttpStatus.Ok, templateProcessor.replace(new HashMap<String, String>()));
    }
}
