package Server;

import Server.Exceptions.InvalidNameException;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * HttpResponse handles the translation from the websites body, status and additional header
 * lines to a valid http response String.
 *
 * @author Joscha Henningsen
 */
public class HttpResponse {
    private String[] illegalCookieChars = new String[]{"(", ")", "<", ">", "@", ",", ";", ":", "\\", "\"", "/", "[", "]", "?", "=", "{", "}"};
    private HttpStatus status;
    private ArrayList<String> headers = new ArrayList<>();

    public HttpStatus getStatus() {
        return status;
    }

    private String body;

    public String getBody() {
        return body;
    }

    private HashMap<String, String> cookies;

    /**
     * Initialised a simple response with just a status code in it's header and a html body.
     * Good for just sending an http error.
     *
     * @param status
     * @param body
     */
    public HttpResponse(HttpStatus status, String body) {
        this.status = status;
        this.body = body;
    }

    /**
     * Best practice of initializing a response. The status code is set,
     * a body can be added later using the TemplateProcessor
     *
     * @param status
     */
    public HttpResponse(HttpStatus status) {
        this(status, "");
    }

    /**
     * Creates a String that gets send to the browser including all http headers and the Sites body.
     *
     * @return responseString
     */
    @Override
    public String toString() {
        StringBuilder responseBuilder = new StringBuilder();

        responseBuilder.append("HTTP/1.1 ");
        responseBuilder.append(status.getCode());
        responseBuilder.append(" ");
        responseBuilder.append(status.getText());
        headers.forEach(s -> responseBuilder.append("\r\n").append(s));
        if (cookies != null) {
            responseBuilder.append("\r\n");
            cookies.forEach((o1, o2) -> responseBuilder.append("Set-Cookie: ").append(o1).append("=").append(o2));
        }
        responseBuilder.append("\r\n\r\n"); //end of http header

        responseBuilder.append(body);

        return responseBuilder.toString();
    }

    /**
     * Sets a cookie that is stored in the users Browser.
     *
     * @param key    the cookies name. Make sure it doesn't include illegal characters
     * @param value  the cookies value. Make sure it doesn't include illegal characters
     * @param extras http cookie extras, such as a path or time
     */
    public void setCookie(String key, String value, String extras) {
        for (int i = illegalCookieChars.length - 1; i >= 0; i--) {
            if (key.contains(illegalCookieChars[i]) || value.contains(illegalCookieChars[i]))
                throw new InvalidNameException();
        }
        if (cookies == null)
            cookies = new HashMap<>();
        if (!extras.equals(""))
            value += "; " + extras;
        cookies.put(key, value);
    }

    /**
     * Use this function to add extra headers to the response such as "Location: http://...".
     *
     * @param header
     */
    public void addHeader(String header) {
        headers.add(header);
    }

    /**
     * Sets a cookie that is stored in the users Browser without extras.
     *
     * @param key   the cookies name. Make sure it doesn't include illegal characters
     * @param value the cookies value. Make sure it doesn't include illegal characters
     */
    public void setCookies(String key, String value) {
        setCookie(key, value, "");
    }
}
