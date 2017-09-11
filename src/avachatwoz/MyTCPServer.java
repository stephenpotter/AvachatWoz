/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avachatwoz;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author samf
 */


public class MyTCPServer extends Thread {
    
    ServerSocket welcomeSocket;
    DataOutputStream outToClient;
    Receiver r;
    
    public MyTCPServer(Receiver r) {
        this.r = r;
        try {
            welcomeSocket = new ServerSocket(6789);
            System.out.println("Opened server socket");
        } catch (IOException ex) {
            Logger.getLogger(AvachatWoz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send(String s) {
        try {
            outToClient.writeBytes(s+"\n");
        } catch (IOException ex) {
            Logger.getLogger(MyTCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run() {
        System.out.println("in run method");
        try {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            System.out.println("tcp run init");
            while (true) {
                String clientSentence = inFromClient.readLine();
                r.receiveInput(clientSentence);
                MySleeper.sleep(100);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
                //Logger.getLogger(AvachatWoz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
