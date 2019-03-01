package Server;

import java.util.HashMap;

public class Config {
    public int port;
    public HashMap<String, String> defaultReplacements=new HashMap<>();

    public Config(){
        port=8000;
        defaultReplacements.put("%defaultheader", "<link rel=\"stylesheet\" " +
                "href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\"></script>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script>\n" +
                "    <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>%title</title>\n" +
                "    <link rel=\"stylesheet\" href=\"css/main.css\">");

    }
}
