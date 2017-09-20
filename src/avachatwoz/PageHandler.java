/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avachatwoz;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 *
 * @author samf
 */
public class PageHandler implements HttpHandler {

    @Override
    /*public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } */
    public void handle(HttpExchange t) throws IOException {
        //System.out.println("MyHandler");
        URI uri = t.getRequestURI();
        //System.out.println(uri + " " + uri.getPath());
        if (uri.getPath().contains("favicon")) {
            System.out.println("Sending null for favicon");
            t.sendResponseHeaders(204, -1);
            return;
        }
        try {
            File file = new File("." + uri.getPath()).getCanonicalFile();

            // Object exists and is a file: accept with response code 200.
            if (file != null && file.exists()) {
                t.sendResponseHeaders(200, 0);
                System.out.println("Sending page as requested");

                OutputStream os = t.getResponseBody();
                FileInputStream fs = new FileInputStream(file);
                final byte[] buffer = new byte[0x10000];
                int count = 0;
                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
                fs.close();
                os.close();
            } else {
                t.sendResponseHeaders(204, -1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
