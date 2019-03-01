package Server;

import java.util.HashMap;

public class HttpResponse {
  private HttpStatus status;
  
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
    if (cookies!=null) {
      responseBuilder.append("\r\n");
      cookies.forEach((o1, o2)->responseBuilder.append("Set-Cookie: "+o1+"="+o2));
    }
    responseBuilder.append("\r\n\r\n");
    
    responseBuilder.append(body);
    
    return responseBuilder.toString();
  }

    public void setCookie(String key, String value) {
      if (cookies==null)
        cookies=new HashMap<>();
      cookies.put(key, value);
    }
}
