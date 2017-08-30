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
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AvachatWoz implements Receiver {
    MyTCPServer tcp;
    InfoHandler ih;
    AvachatWoz() {
       tcp = new MyTCPServer(this);
       ih = new InfoHandler(tcp);
     
    }
    public static void main(String[] args)  {
        AvachatWoz w = new AvachatWoz();
        w.startHttpServer();
        w.startTcpServer();
        //AvachatWoz w = new AvachatWoz();
        //w.start();
    }
    
    void startTcpServer() {
        System.out.println("Starting tcp server");
       tcp.start();
    }
    
    @Override
    public void receiveInput(String input) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ih.message = input;
    }

    private void startHttpServer() {
      try {
            //tcp = new MyTCPServer(this);
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new PageHandler());
            server.createContext("/test", ih);
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(AvachatWoz.class.getName()).log(Level.SEVERE, null, ex);
        }
}

    

    
}
