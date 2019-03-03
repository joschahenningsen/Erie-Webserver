package Server.Routes;

import Server.HttpResponse;
import Server.HttpStatus;

import java.io.IOException;

public class Error404 extends Route {
    @Override
    public HttpResponse getResponse() throws IOException {
        return new HttpResponse(HttpStatus.NotFound, "<h1>404</h1> <h2>not found</h2>");
    }
}
