package it.polimi.ingsw.view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FirstGui {
    static JFrame window;
    static JLayeredPane lpane = new JLayeredPane();

    public FirstGui() {
        prepareFirstGUI();


    }


    public static void main(String args[]) throws IllegalStateException, NullPointerException, IOException {
        FirstGui gui = new FirstGui();
        gui.showActionListenerDemo();

    }

    private void prepareFirstGUI() {
        window = new JFrame("Master of Renaissance");

        window.setDefaultLookAndFeelDecorated(true);

        //creation of the 3 panels,one ober the other,lpane is the base,
        // then p1 is the panel that contain the image and the last one contains te button

        JPanelBackground p1 = null;

        // dimension of the frame and setting layout
        window.setSize(489, 666);
        window.setLayout(new BorderLayout());
        window.add(lpane, BorderLayout.CENTER);


        //upload image on the p1 panel
        try {
            p1 = new JPanelBackground("src/main/resources/Background.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setting the pos and dim of p1
        p1.setBounds(0, 0, 489, 666);
        p1.setOpaque(true);


        //adding p1 to lpane,a.k.a the base
        lpane.add(p1, 0, 0);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void showActionListenerDemo() {
        JPanel top = new JPanel();
        //setting the pos and dim of top
        top.setBounds(130, 470, 200, 200);
        top.setOpaque(false);
        //adding top to lpane,a.k.a the base
        lpane.add(top, 1, 0);

        //creations of bottons
        JButton PlayButton = new JButton();
        JButton ExitButton = new JButton();

        //setting the buttons font and text
        PlayButton.setFont(new java.awt.Font("Arial Black", 0, 14));
        PlayButton.setText("PLAY");
        ExitButton.setFont(new java.awt.Font("Arial Black", 0, 14));
        ExitButton.setText("EXIT");

        PlayButton.addActionListener(new CustomActionListener());
        ExitButton.addActionListener(new CustomActionListener());
        PlayButton.setActionCommand("go");
        ExitButton.setActionCommand("exit");

        //adding both buttons on top panel
        top.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        top.add(PlayButton);
        top.add(ExitButton);


    }
}


