package Server.Routes;

import Server.HttpResponse;
import Server.HttpStatus;
import Server.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;

public class GetStartedPage extends Route {

    public GetStartedPage(){
        this.name="/get-started";
    }

    @Override
    public HttpResponse getResponse() throws IOException {
        vars.put("%title", "Getting started");
        vars.put("%page0", "It Works!");
        vars.put("%page0link", "/");
        vars.put("%page1link", "get-started");
        vars.put("%page1", "Get started");
        vars.put("%page2link", "https://github.com/joschahenningsen/Java-Webserver");
        vars.put("%page2", "About");
        vars.put("%page0active", "inactive");
        vars.put("%page1active", "active");
        StringBuilder content=new StringBuilder();
        content.append("<h2>Creating Routes</h2>");
        content.append("Creating routes to your pages is as easy as it sonds: You'll have to create a new Class extending the Route class which implements a default Constructor that sets the attribute name to your pages url. Additionaly you are going to need to implement the function getResponse that returns a HttpResponse object. You can easily create your webpage by using a template file and replace contents with the TemplateProcessor. Finally you have to add the new class to the WebserverThreads constructor. Have fun and explore the Code!");
        content.append("</br></br></br><h2>Accessing Request Data</h2>");
        content.append("Accessing Request-data is not hard as well. Every Route implementing the Routes class has the attribute requestData which offers quite a few helpfull methods like hasCookie(String key) and getCookie(key). More is comming soon!");
        vars.put("%content", content.toString());
        System.out.println(vars);
        return new HttpResponse(HttpStatus.Ok, new TemplateProcessor("html/index.html").replace(vars));
    }
}
