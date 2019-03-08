package Server.Routes;

public class GetStartedPage extends Route {

    public GetStartedPage(){
        setUrl("/get-started");
        acceptSubpages(true);
    }

    @Override
    public void setupPage() {
        setTemplateFile("html/index.html");

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
        content.append("Creating routes to your pages is as easy as it sonds: You'll have to create a new Class extending the Route class which implements a default Constructor that sets the attribute name to your pages url via setUrl(url). Additionally you are going to need to implement the function setupPage in which you can create your page by using a template file (setTemplateFile(filename)) and replace contents with the vars Map using vars.put(key, replacement), or use the setBody method offered by the Route. Finally you have to add the new class to the WebserverThreads constructor. Have fun and explore the Code!");
        content.append("</br></br></br><h2>Accessing Request Data</h2>");
        content.append("Accessing Request-data is not hard as well. Every Route implementing the Routes class has the attribute requestData which offers quite a few helpful methods like hasCookie(String key) and getCookie(key). More is coming soon!");
        content.append("<br>To set user side data, such as cookies you can use one of the methods implemented in HttpResponse. For example setCookie(String key, String value).");
        content.append("</br></br></br><h2>Limit File Access</h2>");
        content.append("Disallowing access to files works by placing a file named \".forbidden\" into a restricted directory.");
        vars.put("%content", content.toString());
    }
}
