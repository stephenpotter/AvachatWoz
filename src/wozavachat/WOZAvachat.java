/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wozavachat;

/**
 *
 * @author samf
 */
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

public class WOZAvachat {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.createContext("/test", new TestHandler());
        
        server.start();
    }

    static class MyHandler implements HttpHandler {

        @Override
        /*public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } */
        public void handle(HttpExchange t) throws IOException {
            System.out.println("MyHandler");
            URI uri = t.getRequestURI();
            //System.out.println(uri+" "+uri.getPath());
            
            try {
                File file = new File("."+uri.getPath()).getCanonicalFile();
            
            
            // Object exists and is a file: accept with response code 200.
            t.sendResponseHeaders(200, 0);
            OutputStream os = t.getResponseBody();
            FileInputStream fs = new FileInputStream(file);
            final byte[] buffer = new byte[0x10000];
            int count = 0;
            while ((count = fs.read(buffer)) >= 0) {
                os.write(buffer, 0, count);
            }
            fs.close();
            os.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    static class TestHandler implements HttpHandler {
         
        @Override
        public void handle(HttpExchange t) throws IOException {
            System.out.println("TestHandler");

            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } 
    }

}
