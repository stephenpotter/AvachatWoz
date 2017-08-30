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
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient extends Thread {
    DataOutputStream outToServer;
    BufferedReader inFromServer;
    Receiver r;
    TCPClient(Receiver r) {
        try {
            this.r = r;
            System.out.println("Starting client");
            Socket clientSocket = new Socket("localhost", 6789);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void send(String s) {
        try {    
            outToServer.writeBytes(s+"\n");
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void start() {
        super.start();
        
    }
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("in tcp loop");
                String sentence = inFromServer.readLine();
                System.out.println("Received sentence "+sentence);
                r.receiveInput(sentence);
                MySleeper.sleep(100);
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
