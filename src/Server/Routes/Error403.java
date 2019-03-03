package Server.Routes;

import Server.HttpStatus;

public class Error403 extends Route {
    @Override
    public void setupPage() {
        setStatus(HttpStatus.Forbidden);
        setBody("<h1>403</h1>Forbidden.");
    }
}
