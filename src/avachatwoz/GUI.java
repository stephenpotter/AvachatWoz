/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avachatwoz;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author samf
 */
public class GUI implements Receiver, ActionListener, Runnable {

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
        buttons[0].setText("Start system");
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
        System.out.println("Inside set options");
        for (int i = 0; i < options.size(); ++i) {
            String option = options.get(i);
            System.out.println("Setting option "+option);
            buttons[i].setText(option);
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
        System.out.println("Started manager");
        while (!m.finished()) {
           // System.out.println("In loop (not finished)");

            if (!m.blocked()) {
                System.out.println("Not blocked");

                String out = m.next();
                if (out != null) {
                    System.out.println("Sending message out");

                    client.send(out);
                    MySleeper.sleep(2000);
                } else {
                    System.out.println("Showing options");

                    List<String> options = m.getOptions();
                    setOptions(options);
                }
            }
            MySleeper.sleep(100);
        }
        System.out.println("finished");
    }

    @Override
    public void receiveInput(String input) {
        if (input.equals("restart")) {
            System.out.println("GUI received restart");
            //startManager();
            //new Thread(this).start();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("actionPerformed");
        String s = e.getActionCommand();
        if (s.equals("Start system")) {
            //startManager();
            new Thread(this).start();
            
        } else {
            m.chooseOption(s);
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        startManager();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
