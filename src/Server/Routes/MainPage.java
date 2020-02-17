package Server.Routes;

/**
 * Sample Route, inspire yourself
 *
 * @author Joscha Henningsen
 */
public class MainPage extends Route {

    /**
     * This is important! If you don't call setUrl in the constructor of your Routes, the page can't be accessed directly.
     */
    public MainPage() {
        setUrl("/");
    }


    /**
     * sets everything your page does
     */
    @Override
    public void setupPage() {
        setTemplateFile("html/index.html");

        vars.put("%title", "It Works!");
        vars.put("%page0", "It Works!");
        vars.put("%page0link", "/");
        vars.put("%page1link", "get-started");
        vars.put("%page1", "Get started");
        vars.put("%page2link", "https://github.com/joschahenningsen/Erie-Webserver");
        vars.put("%page2", "About");
        vars.put("%page0active", "active");
        vars.put("%content", "<a href=\"get-started\" class=\"btn btn-lg btn-secondary\">get started!</a>");
    }
}
