package Server.Routes;

import Server.HttpResponse;
import Server.HttpStatus;

import java.io.IOException;

public class Error403 extends Route {
    @Override
    public HttpResponse getResponse() throws IOException {
        return new HttpResponse(HttpStatus.Forbidden, "<h1>403</h1> <h2>forbidden</h2>");
    }
}
