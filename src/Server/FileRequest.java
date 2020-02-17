package Server;

import Server.Routes.Error403;
import Server.Routes.Error404;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class handles request to your File System
 *
 * @author Joscha Henningsen
 */
public class FileRequest {
    private String fileName;
    private FileInputStream fileInputStream;
    private boolean exists;
    private PrintWriter out;
    private OutputStream outputStream;
    private boolean forbidden;

    /**
     * Initializes the fileRequest and handles errors if file doesn't exist or cannot be acceded due to forbidden directory settings
     *
     * @param fileName
     * @param out
     * @param outputStream
     */
    public FileRequest(String fileName, PrintWriter out, OutputStream outputStream) {
        this.fileName = fileName.substring(1);
        this.out = out;
        this.outputStream = outputStream;
        exists();
        isForbidden();

        respond();
        try {
            fileInputStream.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * sets exists to false if file doesn't exist. True otherwise
     */
    private void exists() {
        try {
            fileInputStream = new FileInputStream(this.fileName);
            exists = true;
        } catch (FileNotFoundException e) {
            exists = false;
        }
    }

    /**
     * sets forbidden to true if the File .forbidden exists in the directory the user tries to access
     */
    private void isForbidden() {
        try {
            String tempName = ".forbidden";
            if (this.fileName.contains("/"))
                tempName = fileName.substring(0, fileName.lastIndexOf("/")) + "/" + tempName;
            ;
            fileInputStream = new FileInputStream(tempName);
            forbidden = true;
        } catch (FileNotFoundException e) {
            forbidden = false;
        }
    }


    /**
     * returns either the file if it exists and can be accessed or throws an 403/404 error otherwise
     */
    private void respond() {
        if (forbidden) {
            respond403();
            return;
        } else if (!exists) {
            respond404();
            return;
        }
        try {
            out.println("HTTP/2.0 " + HttpStatus.Ok.getCode() + " " + HttpStatus.Ok.getText());
            out.println("Content-Type: " + Files.probeContentType(Paths.get(this.fileName)));
            out.println("Content-Length: " + fileInputStream.available());
            out.println("");
            out.flush();
            fileInputStream.transferTo(outputStream);
            out.print("\r\n\r\n"); //end of http response
        } catch (IOException e) {
            respond404();
        }
    }


    /**
     * sends a simple 403 error page to the browser
     */
    private void respond403() {
        try {
            out.print(new Error403().getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a simple 404 error page to the browser
     */
    private void respond404() {
        try {
            out.print(new Error404().getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
