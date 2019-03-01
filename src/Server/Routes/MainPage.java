package Server.Routes;

import Server.HttpResponse;
import Server.HttpStatus;
import Server.TemplateProcessor;
import java.io.IOException;

public class MainPage extends Route{
    public MainPage(){
        this.name="/";
    }

    @Override
    public HttpResponse getResponse() throws IOException {
        TemplateProcessor templateProcessor=new TemplateProcessor("html/index.html");
        vars.put("%title", "It Works!");
        vars.put("%page0", "It Works!");
        vars.put("%page0link", "/");
        vars.put("%page1link", "get-started");
        vars.put("%page1", "Get started");
        vars.put("%page2link", "https://github.com/joschahenningsen/Java-Webserver");
        vars.put("%page2", "About");
        vars.put("%page0active", "active");
        vars.put("%content", "<a href=\"get-started\" class=\"btn btn-lg btn-secondary\">get started!</a>");

        HttpResponse response=new HttpResponse(HttpStatus.Ok, templateProcessor.replace(vars));
        return response;
    }
}
