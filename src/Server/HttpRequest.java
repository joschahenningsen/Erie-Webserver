package Server;

import Server.Exceptions.InvalidRequestException;
import java.util.HashMap;

/**
 * Represents the request send to the server
 * @author Joscha Henningsen
 */
public class HttpRequest {
  private HttpMethod method;
  private int length;
  private HashMap<String, String> POST;
  private HashMap<String, String> GET;

  /**
   * gets the Method for the request
   * @return HttpMethod.Post/Get
   */
  HttpMethod getMethod() {
    return method;
  }
  
  private String path;

  /**
   * returns the path the user tries to access
   * @return path
   */
  String getPath() {
    return path;
  }

  private HashMap<String, String> cookies;

  /**
   * checks if cookie exists by key
   * @param key
   * @return boolean hasCookie
   */
  public boolean hasCookie(String key){
    return cookies.containsKey(key);
  }

  /**
   * returns a cookie by key
   * @param key
   * @return cookie value
   */
  public String getCookie(String key){
    return cookies.get(key);
  }


  /**
   * Sets up request by the Header send from browser
   * @param requestLine
   * @param others
   */
  HttpRequest(String requestLine, String others) {
    String[] requestLineParts = requestLine.split(" ");
    if(requestLineParts.length < 2)
      throw new InvalidRequestException();
    String method = requestLineParts[0];
    GET = new HashMap<>();
    switch(method) {
      case "GET":
        this.method = HttpMethod.GET;
        break;
      case "POST":
        this.method = HttpMethod.POST;
        POST = new HashMap<>();
        break;
      default:
        throw new InvalidRequestException();
    }


    String[] additionalParams = others.split("\n");
    cookies=new HashMap<>();
    for (String additionalParam : additionalParams) {//resolve cookies
      if (additionalParam.startsWith("Cookie: ")) {
        if (additionalParam.contains(";")) {
          String[] cookies = additionalParam.split(": ")[1].split("; ");
          for (String cookie : cookies) {
            this.cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
          }
        } else {
          this.cookies.put(additionalParam.split(": ")[1].split("=")[0], additionalParam.split(": ")[1].split("=")[1]);
        }
      } else if (additionalParam.startsWith("Content-Length: ")) {
        this.length = Integer.parseInt(additionalParam.split(": ")[1]);
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
    for (String param : params) {
      String[] paramValue = param.split("=");
      if (paramValue.length != 2)
        throw new InvalidRequestException();
    }
  }

    int getLength() {
        return length;
    }

  void setBody(String body) {
    String[] params = body.split("&");
    for (String param : params) {
      String[] paramValue = param.split("=");
      if (paramValue.length != 2)
        throw new InvalidRequestException();
      POST.put(paramValue[0], paramValue[1]);
    }
  }

  /**
   * Returns a variable passed in post request
   * @param key name of post param
   * @return value of post param
   */
  public String POST(String key){
    if (POST == null)
      return null;
    return POST.get(key);
  }

  void setGetParams(String getParams) {
    String[] params = getParams.split("&");
    for (String param : params) {
      String[] paramValue = param.split("=");
      if (paramValue.length == 2)
        GET.put(paramValue[0], paramValue[1]);
        //throw new InvalidRequestException();
    }
  }

  /**
   * Returns a variable passed in get request
   * @param key name of get param
   * @return value of get param
   */
  public String GET(String key){
    if (GET == null)
      return null;
    return GET.get(key);
  }

}