package Server;

import Server.Routes.NotFoundError;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileRequest {
    private String fileName;
    private FileInputStream fileInputStream;
    private boolean exists;
    private PrintWriter out;
    private OutputStream outputStream;

    public FileRequest(String fileName, PrintWriter out, OutputStream outputStream){
        this.fileName=fileName.substring(1);
        try {
            fileInputStream=new FileInputStream(this.fileName);
            exists=true;
        } catch (FileNotFoundException e) {
            exists=false;
            //e.printStackTrace();
        }
        this.out=out;
        this.outputStream=outputStream;
        respond();
        try {
            fileInputStream.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void respond404(){
        try {
            out.print(new NotFoundError().getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void respond(){
        if (!exists){
            respond404();
            return;
        }
        try {
            out.println("HTTP/2.0 "+HttpStatus.Ok.getCode()+" "+HttpStatus.Ok.getText());
            out.println("Content-Type: "+ Files.probeContentType(Paths.get(this.fileName)));
            out.println("Content-Length: "+fileInputStream.available());
            out.println("");
            out.flush();
            fileInputStream.transferTo(outputStream);
            out.print("\r\n\r\n");
        } catch (IOException e) {
            respond404();
        }
    }
}
