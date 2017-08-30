/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avachatwoz;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author samf
 */
public class GUI implements Receiver, ActionListener {

    JFrame frame;
    JButton[] buttons;
    TCPClient client;
    ContentManager m;

    public GUI(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        buttons = new JButton[8];
        for (int i = 0; i < 8; ++i) {
            buttons[i] = new JButton();
            buttons[i].addActionListener(this);
        }
        addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setSize(800, 400);
        frame.setVisible(true);
        frame.repaint();
        m = new ContentManager();
        client = new TCPClient(this);

    }

    final void addComponentsToPane(Container pane) {

        GridLayout layout = new GridLayout(0, 2);
        pane.setLayout(layout);
        for (int i = 0; i < 8; ++i) {
            pane.add(buttons[i]);
        }

    }

    void setOptions(List<String> options) {
        for (int i = 0; i < options.size(); ++i) {
            buttons[i].setText(options.get(i));
        }
    }

    void start() {
        m = new ContentManager();
        m.read("content.txt");
        client.start();
       
    }

    public static void main(String args[]) {
        GUI gui = new GUI("WOZ");
        gui.start();

    }

    void startManager() {
        m.start();
        while (!m.finished()) {
            if (!m.blocked()) {
                String out = m.next();
                if (out != null) {
                    client.send(out);
                    MySleeper.sleep(2000);
                } else {
                    List<String> options = m.getOptions();
                    setOptions(options);
                }
                
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ContentManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("finished");
    }

    @Override
    public void receiveInput(String input) {
        if (input.equals("restart")) {
            System.out.println("GUI received restart");
            startManager();

        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        m.chooseOption(s);
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
