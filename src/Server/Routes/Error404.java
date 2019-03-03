package Server.Routes;

import Server.HttpResponse;
import Server.HttpStatus;

import java.io.IOException;

public class Error404 extends Route {
    @Override
    public void setupPage() {
        setStatus(HttpStatus.NotFound);
        setBody("<h1>404</h1>Not found.");
    }
}
