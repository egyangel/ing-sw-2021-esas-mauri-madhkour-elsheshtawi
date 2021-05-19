package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//lobby for the palyer that choose multiplayer
public class WaitingRoomGui extends JFrame{

    static JLayeredPane lpane = new JLayeredPane();
    private String PlayerMode;

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
        top.setBounds(100, 160, 300, 80);
        top.setOpaque(false);
        //adding top to lpane,a.k.a the base
        lpane.add(top, 1, 0);
        top.setLayout(new GridLayout(3,1));

        JLabel Nickname = new JLabel("Waiting Other Player....");
        Nickname.setForeground(Color.BLACK);
        Nickname.setFont(new Font("Arial Black", 0, 20));


        top.add(Nickname);
        //lobby player

        GridBagLayout layout = new GridBagLayout();
        top.setLayout(layout);

        JLabel Player1= new JLabel();
        Player1.setForeground(Color.BLACK);
        Player1.setFont(new Font("Arial Black", 0, 14));
        JLabel Player2= new JLabel();
        Player2.setForeground(Color.BLACK);
        Player2.setFont(new Font("Arial Black", 0, 14));
        JLabel Player3= new JLabel();
        Player3.setForeground(Color.BLACK);
        Player3.setFont(new Font("Arial Black", 0, 14));
        JLabel Player4= new JLabel();
        Player4.setForeground(Color.BLACK);
        Player4.setFont(new Font("Arial Black", 0, 14));





        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        top.add(Player1, gbc);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 0.5;
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        top.add(Player2, gbc2);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.weightx = 0.5;
        gbc3.gridx = 0;
        gbc3.gridy = 1;
        top.add(Player3, gbc3);

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.fill = GridBagConstraints.HORIZONTAL;
        gbc4.weightx = 0.5;
        gbc4.gridx = 1;
        gbc4.gridy = 1;
        top.add(Player4, gbc4);


    }
}
