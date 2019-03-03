package Server;

import Server.Routes.Error403;
import Server.Routes.Error404;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileRequest {
    private String fileName;
    private FileInputStream fileInputStream;
    private boolean exists;
    private PrintWriter out;
    private OutputStream outputStream;
    private boolean forbidden;

    public FileRequest(String fileName, PrintWriter out, OutputStream outputStream){
        this.fileName=fileName.substring(1);
        this.out=out;
        this.outputStream=outputStream;
        exists();
        isForbidden();

        respond();
        try {
            fileInputStream.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private void exists() {
        try {
            fileInputStream=new FileInputStream(this.fileName);
            exists=true;
        } catch (FileNotFoundException e) {
            exists=false;
        }
    }

    private void isForbidden() {
        try {
            String tempName = ".forbidden";
            if (this.fileName.indexOf("/")!=-1)
                tempName = fileName.substring(0, fileName.lastIndexOf("/"))+"/"+tempName;
            System.out.println(tempName);
            fileInputStream=new FileInputStream(tempName);
            forbidden=true;
        } catch (FileNotFoundException e) {
            forbidden=false;
        }
    }

    private void respond(){
        if (forbidden){
            respond403();
            return;
        }else if (!exists){
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


    private void respond403() {
        try {
            out.print(new Error403().getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void respond404(){
        try {
            out.print(new Error404().getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
