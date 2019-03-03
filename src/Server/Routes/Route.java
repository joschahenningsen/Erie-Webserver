package Server.Routes;

import Server.*;

import java.io.IOException;
import java.util.HashMap;

public abstract class Route {
    protected String name;
    protected HttpRequest requestData;
    protected HashMap<String, String> vars;
    private HttpStatus status;
    private String templateFile;
    private String body;
    private String url;

    public void setBody(String body){
        this.body=body;
    }

    public void setTemplateFile(String fileName){
        this.templateFile=fileName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatus(HttpStatus status){
        this.status=status;
    }

    public void setRequestData(HttpRequest requestData){
        this.requestData=requestData;
        Config config=new Config();
        this.vars=config.defaultReplacements;
    }

    public String getUrl(){
        return url;
    }

    public abstract void setupPage();

    public final HttpResponse getResponse() throws IOException{
        setupPage();
        if (templateFile==null&&body==null)
            return new HttpResponse(HttpStatus.InternalServerError, "<h1>500</h1>Internal Server Error");
        if (status==null)
            status=HttpStatus.Ok;
        if (body!=null)
            return new HttpResponse(status, body);
        TemplateProcessor templateProcessor=new TemplateProcessor(templateFile);
        return new HttpResponse(status, templateProcessor.replace(vars));
    }
}
