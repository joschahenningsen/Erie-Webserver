package Server;

import Server.Exceptions.InvalidNameException;

import java.util.ArrayList;
import java.util.HashMap;

public class HttpResponse {
  private String[] illegalCookieChars=new String[]{"(", ")", "<", ">", "@", ",", ";",":", "\\", "\"","/", "[", "]", "?", "=", "{", "}"};
  private HttpStatus status;
  private ArrayList<String> headers=new ArrayList<>();

  public HttpStatus getStatus() {
    return status;
  }
  
  private String body;
  
  public String getBody() {
    return body;
  }

  private HashMap<String, String> cookies;

  public HttpResponse(HttpStatus status, String body) {
    this.status = status;
    this.body = body;
  }
  
  public HttpResponse(HttpStatus status) {
    this(status, "");
  }
  
  @Override
  public String toString() {
    StringBuilder responseBuilder = new StringBuilder();
    
    responseBuilder.append("HTTP/2.0 ");
    responseBuilder.append(status.getCode());
    responseBuilder.append(" ");
    responseBuilder.append(status.getText());
    headers.forEach(s -> responseBuilder.append("\r\n"+s));
    if (cookies!=null) {
      responseBuilder.append("\r\n");
      cookies.forEach((o1, o2)->responseBuilder.append("Set-Cookie: "+o1+"="+o2));
    }
    responseBuilder.append("\r\n\r\n");
    
    responseBuilder.append(body);
    
    return responseBuilder.toString();
  }

    public void setCookie(String key, String value, String extras) {
      for (int i = illegalCookieChars.length - 1; i >= 0; i--) {
        if (key.contains(illegalCookieChars[i])||value.contains(illegalCookieChars[i]))
          throw new InvalidNameException();
      }
      if (cookies==null)
        cookies=new HashMap<>();
      if (!extras.equals(""))
        value+="; "+extras;
      cookies.put(key, value);
    }

    public void addHeader(String header){
      headers.add(header);
    }

    public void setCookies(String key, String value){
      setCookie(key, value, "");
    }
}
