package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//greet the player and allow him to go in the connction frame
public class FirstGui extends JFrame {
    static JFrame window;
    static JLayeredPane lpane = new JLayeredPane();

    public FirstGui() {
        super("Master of Renaissance");
        prepareFirstGUI();


    }

    private void prepareFirstGUI() {


        setDefaultLookAndFeelDecorated(true);

        //creation of the 3 panels,one ober the other,lpane is the base,
        // then p1 is the panel that contain the image and the last one contains te button

        ImagePanel p1 = new ImagePanel ("src/main/resources/Background.jpg");

        // dimension of the frame and setting layout
        setSize(489, 666);
        setLayout(new BorderLayout());
        add(lpane, BorderLayout.CENTER);


        //setting the pos and dim of p1
        p1.setBounds(0, 0, 489, 666);
        p1.setOpaque(true);


        //adding p1 to lpane,a.k.a the base
        lpane.add(p1, 0, 0);
        showAction();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void showAction() {
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

        PlayButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                new GUI().displaySetup();
                 dispose();

            }});



        ExitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){

                dispose();

            }});
        PlayButton.setActionCommand("go");
        ExitButton.setActionCommand("exit");

        //adding both buttons on top panel
        top.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        top.add(PlayButton);
        top.add(ExitButton);



    }
}
