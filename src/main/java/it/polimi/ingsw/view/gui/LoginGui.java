package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//check if the nickaname is correct and choosing the mode of the game

public class LoginGui extends JFrame{

        private boolean SinglePlayer= false;
        static JLayeredPane lpane = new JLayeredPane();

        public LoginGui() {
            super("Master of Renaissance");
            prepareLoginGUI();

        }

        private void prepareLoginGUI() {

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

            showAction();
            System.out.println(Thread.currentThread().getName()); // Stampa "main"

            setLocationRelativeTo(null);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }

        private void showAction() {
            JPanel top = new JPanel();
            //setting the pos and dim of top
            top.setBounds(100, 40, 250, 300);
            top.setOpaque(false);
            //adding top to lpane,a.k.a the base
            lpane.add(top, 1, 0);

            GridBagLayout layout = new GridBagLayout();
            top.setLayout(layout);

            JLabel Nickname = new JLabel("Nickname");
            Nickname.setForeground(Color.BLACK);
            Nickname.setFont(new Font("Arial Black", 0, 14));
            JTextField Name = new JTextField(12);
            //Nickname.setLabelFor(Name);

            top.add(Nickname);
            top.add(Name);


            JLabel message = new JLabel(" ");
            message.setForeground(Color.BLACK);
            message.setFont(new Font("Arial Black", 2, 16));

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 0.5;
            gbc.gridx = 0;
            gbc.gridy = 0;
            top.add(Nickname, gbc); //gdc reset


            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.fill = GridBagConstraints.HORIZONTAL;
            gbc2.weightx = 0.5;
            gbc2.gridx = 1;
            gbc2.gridy = 0;
            top.add(Name, gbc2);


            GridBagConstraints gbc5 = new GridBagConstraints();
            gbc5.fill = GridBagConstraints.HORIZONTAL;
            gbc5.weightx = 1;
            gbc5.gridx = 0;
            gbc5.gridy = 2;
            gbc5.gridwidth = 2;

            top.add(message, gbc5);

            //adding top to lpane,a.k.a the base
            lpane.add(top, 1, 0);


            JPanel button = new JPanel();

            //setting the pos and dim of top
            button.setBounds(80, 470, 350, 200);
            button.setOpaque(false);
            //adding top to lpane,a.k.a the base
            lpane.add(button, 1, 1);
            //creations of bottons

            //adding top to lpane,a.k.a the base
            lpane.add(button, 1, 0);

            //creations of bottons
            JButton SinglePlayerButton = new JButton();
            JButton MultiPlayerButton = new JButton();

            //setting the buttons font and text
            SinglePlayerButton.setFont(new java.awt.Font("Arial Black", 0, 14));
            SinglePlayerButton.setText("Single Player");
            MultiPlayerButton.setFont(new java.awt.Font("Arial Black", 0, 14));
            MultiPlayerButton.setText("Multi Player");

            SinglePlayerButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    if((Name.getText().isEmpty() /*|| Name.getText().equals()*/))
                    dispose();

                }});



            MultiPlayerButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    new WaitingRoomGui();
                   dispose();

                }});
            SinglePlayerButton.setActionCommand("go");
            MultiPlayerButton.setActionCommand("exit");

            //adding both buttons on top panel
            button.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
            button.add(SinglePlayerButton);
            button.add(MultiPlayerButton);

/*
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

      */
        }
    }


