/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avachatwoz;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samf
 */
public class InfoHandler implements HttpHandler {

        int num = 0;
        long lastUpdate;
        boolean init = false;
        String message = null;
        MyTCPServer tcp;
        public InfoHandler(MyTCPServer tcp) {
            this.tcp = tcp;
        }
        void send(HttpExchange t, String response) throws IOException {
           // String response = "Response " + num;
            t.sendResponseHeaders(200, response.length());
            System.out.println("Sending message from GUI");
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
        public void handle(HttpExchange t)  {
            //System.out.println("TestHandler");
            InputStream is = t.getRequestBody();
            String info = convertStreamToString(is);
            //System.out.println(info);

            if (info.equals("true")) {
                tcp.send("restart");
            }

            boolean sent = false;
            if (message!=null) {
                try {
                    send(t, message);
                    message = null;
                    sent = true;
                } catch (IOException ex) {
                    Logger.getLogger(AvachatWoz.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (!sent) {
                try {
                    t.sendResponseHeaders(204, -1);
                    //System.out.println("Sending null, no message to send");
                } catch (IOException ex) {
                    Logger.getLogger(AvachatWoz.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

