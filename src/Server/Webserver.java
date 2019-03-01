package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.IntStream;

public class Webserver {

  public static void main(String[] args) throws IOException {

    Config config=new Config();
    @SuppressWarnings("resource")
    ServerSocket serverSocket = new ServerSocket(config.port);
    IntStream.iterate(0, i -> i + 1).forEach(i -> {
      Socket client;
      try {
        client = serverSocket.accept();
        System.out.println("*** Client connected!");
        WebserverThread wst = new WebserverThread(client);
        wst.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

}
