package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//ask th eplayer  the port and ip of the server and try the connection
public class ConnectionToServerGui extends JFrame{

    private boolean SinglePlayer= false;
    static JLayeredPane lpane = new JLayeredPane();

    public ConnectionToServerGui() {
        super("Master of Renaissance");
        prepareSecondGUI();
        showAction();
    }

    private void prepareSecondGUI() {

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
        top.setBounds(110, 160, 190, 40);
        top.setOpaque(false);
        //adding top to lpane,a.k.a the base
        lpane.add(top, 1, 0);
        top.setLayout(new GridLayout(2,2));



        JLabel IP_Server = new JLabel("Server IP");
        IP_Server.setForeground(Color.BLACK);
        IP_Server.setFont(new Font("Arial Black", 0, 14));
        JTextField tfLastName = new JTextField(12);
        IP_Server.setLabelFor(tfLastName);

        JLabel Port = new JLabel("Port ");
        Port.setForeground(Color.BLACK);
        Port.setFont(new Font("Arial Black", 0, 14));
        JTextField PortName = new JTextField(12);
        Port.setLabelFor(PortName);


        top.add(IP_Server);
        top.add(tfLastName);
        top.add(Port);
        top.add(PortName);


        JPanel button = new JPanel();
        //setting the pos and dim of top
        button.setBounds(160, 470, 150, 200);
        button.setOpaque(false);
        //adding top to lpane,a.k.a the base
        lpane.add(button, 1, 1);
        //creations of bottons

        //adding top to lpane,a.k.a the base
        lpane.add(button, 1, 0);

        //creations of botton
        JButton Connect = new JButton();


        //setting the buttons font and text
        Connect.setFont(new java.awt.Font("Arial Black", 0, 14));
        Connect.setText("Connect");


        Connect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){

                new GUI().displayLogin();
                dispose();

            }});




        Connect.setActionCommand("Connect");
           //adding both buttons on top panel
        button.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        button.add(Connect);


    }
}