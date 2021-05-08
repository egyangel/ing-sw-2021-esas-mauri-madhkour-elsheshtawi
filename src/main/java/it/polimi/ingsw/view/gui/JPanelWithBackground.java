package it.polimi.ingsw.view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class JPanelBackground extends JPanel {

    private Image backgroundImage;

    // use the constructor to load the image.
    public JPanelBackground(String fileName) throws IOException {
        backgroundImage = ImageIO.read(new File(fileName));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image.
        g.drawImage(backgroundImage, 0, 0, this);
    }
}

