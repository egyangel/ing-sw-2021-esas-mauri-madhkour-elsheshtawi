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
    static JTextField serverIp;
    static JTextField port;
    static JButton submit;

    public ConnectionToServerGui() {
        super("Master of Renaissance");
        prepareSecondGUI();
        showAction();
    }

    private void prepareSecondGUI() {
        setDefaultLookAndFeelDecorated(true);
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
        top.setBounds(100, 130, 250, 100);
        top.setOpaque(false);
        //adding top to lpane,a.k.a the base
        lpane.add(top, 1, 0);
        top.setLayout(new GridLayout(2,2));



        JLabel IP_Server = new JLabel("Server IP");
        IP_Server.setForeground(Color.BLACK);
        IP_Server.setFont(new Font("Arial Black", 0, 14));
        JTextField ServerName = new JTextField(12);
        //IP_Server.setLabelFor(ServerName);

        JLabel Port = new JLabel("Port ");
        Port.setForeground(Color.BLACK);
        Port.setFont(new Font("Arial Black", 0, 14));
        JTextField PortName = new JTextField(12);

        //Port.setLabelFor(PortName);


        JLabel message = new JLabel(" ");
        message.setForeground(Color.BLACK);
        message.setFont(new Font("Arial Black", 2, 16));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        top.add(IP_Server, gbc); //gdc reset


        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 0.5;
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        top.add(ServerName, gbc2);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.weightx = 0.5;
        gbc3.gridx = 0;
        gbc3.gridy = 1;
        top.add(Port, gbc3);

        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.fill = GridBagConstraints.HORIZONTAL;
        gbc4.weightx = 0.5;
        gbc4.gridx = 1;
        gbc4.gridy = 1;
        top.add(PortName, gbc4);

        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.fill = GridBagConstraints.HORIZONTAL;
        gbc5.weightx = 1;
        gbc5.gridx = 0;
        gbc5.gridy = 2;
        gbc5.gridwidth = 2; //the name is very long, the element are spaced

        top.add(message, gbc5);
        //adding top to lpane,a.k.a the base
        lpane.add(top, 1, 0);


        JPanel buttonPanel = new JPanel();
        //setting the pos and dim of top
        buttonPanel.setBounds(160, 470, 150, 200);
        buttonPanel.setOpaque(false);
        //adding top to lpane,a.k.a the base
        lpane.add(buttonPanel, 1, 1);
        //creations of bottons

        //adding top to lpane,a.k.a the base
        lpane.add(buttonPanel, 1, 0);

        //creations of botton
        JButton Connect = new JButton();


        //setting the buttons font and text
        Connect.setFont(new java.awt.Font("Arial Black", 0, 14));
        Connect.setText("Connect");


        Connect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){

                if((ServerName.getText().isEmpty() || !ServerName.getText().equals("localhost")) && (PortName.getText().isEmpty() || !PortName.getText().equals("3000")))
                    message.setText("Server IP or Port wrong");

                else {
                    new GUI().displayLogin();
                    dispose();
                }


            }});




        Connect.setActionCommand("Connect");
           //adding both buttons on top panel
        button.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        button.add(Connect);


    }
}
