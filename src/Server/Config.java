package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration for the server
 *
 * @author Joscha Henningsen
 */
public class Config {
    int port;
    public Map<String, String> defaultReplacements = new HashMap<>();
    String logfile;
    public List<String> defaultHeaders = new ArrayList<>();

    /**
     * Set configs here, not in the classes definition.
     */
    public Config() {
        //port your server runs on
        port = 8080;

        //default value pairs that get replaced by the TemplateProcessor on each site you use it on
        defaultReplacements.put("%defaultheader", "<link rel=\"stylesheet\" " +
                "href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\"></script>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script>\n" +
                "    <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <link rel=\"stylesheet\" href=\"css/main.css\">");

        //the headers you want to send with each http response:
        defaultHeaders.add("Content-Language: en");

        //file for error and access logs
        logfile = "logs.txt";
    }
}
