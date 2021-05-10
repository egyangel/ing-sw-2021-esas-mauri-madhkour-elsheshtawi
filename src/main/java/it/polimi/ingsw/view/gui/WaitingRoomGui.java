package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaitingRoomGui extends JFrame{
    static JLayeredPane lpane = new JLayeredPane();

    public WaitingRoomGui() {
        super("Master of Renaissance");
        prepareWaitingRoomGUI();
        showAction();
    }

    private void prepareWaitingRoomGUI() {

        //creation of the 3 panels,one ober the other,lpane is the base,
        // then p1 is the panel that contain the image and the last one contains te button

        ImagePanel p1 = new ImagePanel("src/main/resources/Background.jpg");

        // dimension of the frame and setting layout
        setSize(489, 666);
        setLayout(new BorderLayout());
        add(lpane, BorderLayout.CENTER);


        //upload image on the p1 panel

        //setting the pos and dim of p1
        p1.setBounds(0, 0, 489, 666);
        p1.setOpaque(true);


        //adding p1 to lpane,a.k.a the base
        lpane.add(p1, 0, 0);


        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void showAction() {
        JPanel top = new JPanel();
        //setting the pos and dim of top
        top.setBounds(100, 160, 300, 60);
        top.setOpaque(false);
        //adding top to lpane,a.k.a the base
        lpane.add(top, 1, 0);
        top.setLayout(new GridLayout(3,1));

        JLabel Nickname = new JLabel("Waiting Other Player....");
        Nickname.setForeground(Color.BLACK);
        Nickname.setFont(new Font("Arial Black", 0, 20));


        top.add(Nickname);


    }
}
