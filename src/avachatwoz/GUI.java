/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avachatwoz;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author samf
 */
public class GUI implements Receiver, ActionListener, Runnable {

    JFrame frame;
    JButton[] buttons;
    JTextField textField;
    TCPClient client;
    ContentManager m;
    final static int NUM_BUTTONS = 8;

    public GUI(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        buttons = new JButton[8];

        //       buttons[0].setText("Start system");
        addComponentsToPane(frame.getContentPane());
        setStartOptions();
       
        //Display the window.
        frame.pack();
        frame.setSize(800, 400);
        frame.setVisible(true);
        frame.repaint();
        m = new ContentManager();
        client = new TCPClient(this);

    }

    final void addComponentsToPane(Container pane) {
        JPanel optionPanel = new JPanel();
        JPanel textPanel = new JPanel();
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(600, 50));
        textField.setMaximumSize( textField.getPreferredSize() );
        JButton submitButton = new JButton("Submit");
        
        submitButton.addActionListener(this);

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.LINE_AXIS));
        textPanel.add(textField);
        textPanel.add(submitButton);
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        
        GridLayout layout = new GridLayout(0, 2);
        optionPanel.setLayout(layout);
        for (int i = 0; i < NUM_BUTTONS; ++i) {
            buttons[i] = new JButton();
            buttons[i].addActionListener(this);

            optionPanel.add(buttons[i]);
        }
        pane.add(optionPanel);
        pane.add(textPanel);
        
    }

    final void setStartOptions() {
        for (int i = 0; i < NUM_BUTTONS; ++i) {
            String option = "";
            if (i == 0) {
                option = "Start system";
            }
            buttons[i].setText(option);
        }
    }
    
    final void setFreeOptions() {
        for (int i = 0; i < NUM_BUTTONS; ++i) {
            String option = "";
            if (i == 0) {
                option = "Return to dialogue";
            }
            buttons[i].setText(option);
        }
    }

    void setOptions(List<String> options) {
        for (int i = 0; i < NUM_BUTTONS; ++i) {
            String option = "";
            if (i < options.size()) {
                option = options.get(i);
            }
            buttons[i].setText(option);
        }
    }

    void start() {
        m = new ContentManager();
        m.read();
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

            String out = "";
            while (!m.blocked() && !m.finished()) {
                System.out.println("Not blocked");

                String in = m.next();
                if (in != null) {
                    System.out.println("Sending message out");
                    out += " "+in;
                    //client.send(out);
 //                   MySleeper.sleep(2000);
                } else {
                    System.out.println("Showing options");

                    List<String> options = m.getOptions();
                    setOptions(options);
                }
            }
            if (!out.equals("")) {
                client.send(out);
            }
            MySleeper.sleep(100);
        }
        //buttons[0].setText("Start system");
        setStartOptions();
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

        } else if (s.equals("Submit")) {
            String text = textField.getText();
            textField.setText("");
            this.setFreeOptions();
            System.out.println("got text "+text);
            if (!text.equals("")) {
                client.send(text);
            }
        } else if (s.equals("Return to dialogue")) {
            m.goToDefaultState();
        }
        else {
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
