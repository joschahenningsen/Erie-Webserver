package Server;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HttpRequest {
  private HttpMethod method;
  
  public HttpMethod getMethod() {
    return method;
  }
  
  private String path;
  
  public String getPath() {
    return path;
  }

  private HashMap<String, String> cookies;

  public boolean hasCookie(String key){
    return cookies.containsKey(key);
  }

  public String getCookie(String key){
    return cookies.get(key);
  }

  
  public HttpRequest(String requestLine, String others) {
    String[] requestLineParts = requestLine.split(" ");
    if(requestLineParts.length < 2)
      throw new InvalidRequestException();
    String method = requestLineParts[0];
    switch(method) {
      case "GET":
        this.method = HttpMethod.GET;
        break;
      case "POST":
        this.method = HttpMethod.POST;
        break;
      default:
        throw new InvalidRequestException();
    }


    System.out.println(others);
    String additionalParams[] = others.split("\n");
    cookies=new HashMap<>();
    for (int i = 0; i < additionalParams.length; i++) {//resolve cookies
      if (additionalParams[i].startsWith("Cookie: ")){
        if (additionalParams[i].contains(";")){
          String[]cookies=additionalParams[i].split(": ")[1].split("; ");
          for (String cookie : cookies) {
            this.cookies.put(cookie.split("=")[0],cookie.split("=")[1]);
          }
        }else{
          this.cookies.put(additionalParams[i].split(": ")[1].split("=")[0], additionalParams[i].split(": ")[1].split("=")[1]);
        }
      }
    }
    
    String resource = requestLineParts[1];
    String[] pathParams = resource.split("\\?");
    this.path = pathParams[0];
    if(pathParams.length == 1)
      return;
    if(pathParams.length > 2)
      throw new InvalidRequestException();
    String paramsStr = pathParams[1];
    String[] params = paramsStr.split("&");
    for (int i = 0; i < params.length; i++) {
      String[] paramValue = params[i].split("=");
      if(paramValue.length != 2)
        throw new InvalidRequestException();
    }
  }
}