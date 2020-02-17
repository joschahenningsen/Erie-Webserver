package Server.Routes;

import Server.*;
import Server.Database.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * abstract class for your Routes to extend from.
 *
 * @author Joscha Henningsen
 */
public abstract class Route {
    protected String name;
    protected HttpRequest requestData;
    protected HashMap<String, String> vars;
    protected ArrayList<Database> databases;
    private HttpStatus status;
    private String templateFile;
    private String body;
    private String url;
    private boolean acceptsSubPages = false;
    private String contentType;


    /**
     * Sets the route to be a page that gets request for sub urls
     *
     * @param acceptsSubPages true->enable false->disable
     */
    public void acceptSubPages(boolean acceptsSubPages) {
        this.acceptsSubPages = acceptsSubPages;
    }

    /**
     * returns whether page is a root page or not
     *
     * @return acceptsSubPages
     */
    public boolean acceptsSubPages() {
        return acceptsSubPages;
    }

    /**
     * Sets the (html) body for your response.
     * Notice, that setting a body disables the possibility to use the template processor.
     *
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Sets the name of the TemplateFile for the TemplateProcessor
     *
     * @param fileName
     */
    public void setTemplateFile(String fileName) {
        this.templateFile = fileName;
    }

    /**
     * Sets the URL of the route. This method HAS to be called in your routes constructor if
     * you want your Route to be publicly accessible.
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Sets the Http status code for your page. If you don't call this method the code defaults to 200 OK
     *
     * @param status
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    /**
     * Not to be called by you. This method sets extras like headers send by the browser for you to access later.
     *
     * @param requestData
     */
    public void setRequestData(HttpRequest requestData) {
        this.requestData = requestData;
        Config config = new Config();
        this.vars = config.defaultReplacements;
    }

    /**
     * Sets databases specified in WebserverThread
     *
     * @param databases databases
     */
    public void setDatabases(ArrayList<Database> databases) {
        this.databases = databases;
    }

    /**
     * Returns url for the route
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method is the main method you can implement in order to setup your route.
     */
    public abstract void setupPage();

    /**
     * Sets the content type of the page. Defaults to "text/html" if not called.
     *
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Builds your response based on your setupPage implementation.
     *
     * @return response
     * @throws IOException
     */
    public final HttpResponse getResponse() throws IOException {
        setupPage();
        if (templateFile == null && body == null)
            return new HttpResponse(HttpStatus.InternalServerError, "<h1>500</h1>Internal Server Error");
        if (status == null)
            status = HttpStatus.Ok;
        if (body != null)
            return new HttpResponse(status, body);
        TemplateProcessor templateProcessor = new TemplateProcessor(templateFile);
        HttpResponse response = new HttpResponse(status, templateProcessor.replace(vars));
        if (contentType == null || contentType == "")
            contentType = "text/html";
        response.addHeader("Content-Type: " + contentType);
        return response;
    }
}
