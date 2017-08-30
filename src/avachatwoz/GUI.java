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
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author samf
 */
public class GUI {
    JFrame frame;
    JButton[] buttons;

    public GUI(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        buttons = new JButton[8];
        for (int i = 0; i<8; ++i) {
            buttons[i] = new JButton("Option "+i);
        }
        addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setSize(800, 400);
        frame.setVisible(true);
        frame.repaint();

    }


    final void addComponentsToPane(Container pane) {
        
        GridLayout layout = new GridLayout(0,2);
        pane.setLayout(layout);
        for (int i=0;i<6;++i) {
            pane.add(buttons[i]);
        }

    }
    public static void main(String args[]) {
        GUI gui = new GUI("WOZ");
    }

}

