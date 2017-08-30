/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avachatwoz;

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
import java.io.InputStream;
import java.net.URI;

public class AvachatWoz {

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
            //System.out.println("MyHandler");
            URI uri = t.getRequestURI();
            //System.out.println(uri + " " + uri.getPath());
            if (uri.getPath().contains("favicon")) {
                t.sendResponseHeaders(204, -1);
                return;
            }
            try {
                File file = new File("." + uri.getPath()).getCanonicalFile();

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class TestHandler implements HttpHandler {

        int num = 0;
        long lastUpdate;
        boolean init = false;

        void send(HttpExchange t) throws IOException {
            String response = "Response " + num;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            num++;
        }

        static String convertStreamToString(java.io.InputStream is) {
            if (is == null) {
                return "";
            }
            java.util.Scanner s = new java.util.Scanner(is);
            s.useDelimiter("\\A");
            String streamString = s.hasNext() ? s.next() : "";
            s.close();
            return streamString;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            //System.out.println("TestHandler");
            InputStream is = t.getRequestBody();
            String info = convertStreamToString(is);
            //System.out.println(info);

            boolean sent = false;
            if (!init) {
                init = true;
                lastUpdate = System.currentTimeMillis();
                send(t);
                sent = true;
            }
            long now = System.currentTimeMillis();
            if (now - lastUpdate > 3000) {
                send(t);
                lastUpdate = now;
                sent = true;
            }
            if (!sent) {
                t.sendResponseHeaders(204, -1);
            }
        }
    }

}
