package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaitingRoomGui extends JFrame{
    static JLayeredPane lpane = new JLayeredPane();

    public WaitingRoomGui() {
        super("Master of Renaissance");
        prepareFirstGUI();
        showAction();
    }

    private void prepareFirstGUI() {

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
        top.setBounds(130, 160, 170, 60);
        top.setOpaque(false);
        //adding top to lpane,a.k.a the base
        lpane.add(top, 1, 0);
        top.setLayout(new GridLayout(3,1));

        JLabel Nickname = new JLabel("Nickname");
        Nickname.setForeground(Color.BLACK);
        Nickname.setFont(new Font("Arial Black", 0, 14));
        JTextField tfFirstName = new JTextField(12);
        Nickname.setLabelFor(tfFirstName);

        JLabel IP_Server = new JLabel("Server IP");
        IP_Server.setForeground(Color.BLACK);
        IP_Server.setFont(new Font("Arial Black", 0, 14));
        JTextField tfLastName = new JTextField(12);
        IP_Server.setLabelFor(tfLastName);

        JLabel Port = new JLabel("Port ");
        Port.setForeground(Color.BLACK);
        Port.setFont(new Font("Arial Black", 0, 14));
        JTextField PortName = new JTextField(12);
        Nickname.setLabelFor(PortName);


        top.add(Nickname);
        top.add(tfFirstName);
        top.add(IP_Server);
        top.add(tfLastName);
        top.add(Port);
        top.add(PortName);


        JPanel button = new JPanel();
        //setting the pos and dim of top
        button.setBounds(210, 480, 60, 60);
        button.setOpaque(false);
        //adding top to lpane,a.k.a the base
        lpane.add(button, 1, 1);
        //creations of bottons

        JButton StartButton = new JButton();

        //setting the buttons font and text
        StartButton.setFont(new Font("Arial Black", 0, 14));
        StartButton.setText("START");

        StartButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){

                Runnable init = new Runnable() {
                    public void run() {
                        new WaitingRoomGui();
                    }
                };
                SwingUtilities.invokeLater(init);
                dispose();

            }});

        StartButton.setActionCommand("start the game");


        //top.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        button.add(StartButton);
    }
}
