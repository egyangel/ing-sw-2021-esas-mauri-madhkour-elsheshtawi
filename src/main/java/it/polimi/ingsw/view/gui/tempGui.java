package it.polimi.ingsw.view.gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class tempGui {
    static JFrame window = new JFrame();
    static JPanelWithBackground p1 = null;
    static JTextField TextField = new JTextField();;
    static JButton PlayButton=new JButton();
    static JButton ExitButton=new JButton();


        public static void main(String args[]) throws IllegalStateException, NullPointerException, IOException {
            window.setDefaultLookAndFeelDecorated(true);
            // decido le dimensioni della finestra
            window.setSize(600, 400);
             // rendo visibile la finestra
             window.setVisible(true);
            // scelgo che cosa succedera quando si chiudera la finestra
             window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             // centro la finestra nello schermo
            window.setLocationRelativeTo(null);



            TextField.setFont(new java.awt.Font("Arial Black", 0, 24));
            TextField.setForeground(new java.awt.Color(100, 0, 100));
            TextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            TextField.setText("MASTER OF RESAINENCE");
            TextField.setOpaque(false);
            TextField.setBorder(null);
            PlayButton.setFont(new java.awt.Font("Arial Black", 0, 14));
            PlayButton.setText("PLAY");


            ExitButton.setFont(new java.awt.Font("Arial Black", 0, 14));
            ExitButton.setText("EXIT");
            GroupLayout p1Layout = new javax.swing.GroupLayout(p1);
            p1.setLayout(p1Layout);
            p1Layout.setHorizontalGroup(
                    p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p1Layout.createSequentialGroup()
                                    .addGroup(p1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(ExitButton, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(PlayButton, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
                                    .addGap(350, 350, 350))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p1Layout.createSequentialGroup()
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(TextField, GroupLayout.PREFERRED_SIZE, 450,GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            p1Layout.setVerticalGroup(
                    p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(p1Layout.createSequentialGroup()
                                    .addGap(32, 32, 32)
                                    .addComponent(TextField, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                                    .addComponent(PlayButton, GroupLayout.PREFERRED_SIZE, 53,GroupLayout.PREFERRED_SIZE)
                                    .addGap(55, 55, 55)
                                    .addComponent(ExitButton, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                                    .addGap(58, 58, 58))
            );

            GroupLayout layout = new GroupLayout(window.getContentPane());

            window.getContentPane().add(new JPanelWithBackground("Background.jpeg"));
            window.getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(p1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(p1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );


            p1.add(TextField);
            p1.add(PlayButton);
            p1.add(ExitButton);



            // add panel
            window.add(p1);

            // set the size of frame
            window.setSize(800, 600);

            //window.show();




        }
}

class JPanelWithBackground extends JPanel {

    private Image backgroundImage;

    // Some code to initialize the background image.
    // Here, we use the constructor to load the image. This
    // can vary depending on the use case of the panel.
    public JPanelWithBackground(String fileName) throws IOException {
        backgroundImage = ImageIO.read(new File(fileName));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(backgroundImage, 0, 0, this);
    }
}