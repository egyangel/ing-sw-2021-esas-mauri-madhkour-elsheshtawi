package it.polimi.ingsw.view.gui;

import javax.swing.*;
import java.awt.*;

/**
 * shows a window with some Swing elements
 */
class TestCOde
{
    private JFrame mainFrame;


    public static void main(String[] args)
    {
        TestCOde elements  = new TestCOde();
    }

    /**
     * constructor, creates the window
     */
    public TestCOde()
    {
        prepareGUI();
    }

    //method that creates the window
    public void prepareGUI()
    {
        mainFrame = new JFrame("Java SWING Elements Examples");

        mainFrame.setLayout(new FlowLayout());

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //simple button
        JButton jb1 = new JButton("Button 1");
        mainFrame.add(jb1);


        //button with image

        //load and resize image
        ImageIcon leftButtonIcon = new ImageIcon("./resources/home.png");
        Image img = leftButtonIcon.getImage() ;
        Image newImg = img .getScaledInstance( 20, 20,  java.awt.Image.SCALE_SMOOTH ) ;
        ImageIcon leftButtonIconSmaller = new ImageIcon( newImg );

        JButton jb2 = new JButton("Button2", leftButtonIconSmaller);
        jb2.setVerticalTextPosition(AbstractButton.CENTER); //center text
        jb2.setHorizontalTextPosition(AbstractButton.LEADING); //place image vs text,  aka LEFT, for left-to-right locales

        mainFrame.add(jb2);

        //check boxes
        JCheckBox cb1 = new JCheckBox("cb1");
        cb1.setSelected(true);

        JCheckBox cb2 = new JCheckBox("cb2");
        cb2.setSelected(true);

        JCheckBox cb3 = new JCheckBox("cb3");
        cb3.setSelected(false);

        JCheckBox cb4 = new JCheckBox("cb4");
        cb4.setSelected(true);

        //create a pannel and add the check boxes to keep them aligned
        JPanel panelCB = new JPanel();
        panelCB.setLayout(new FlowLayout());
        panelCB.add(cb1);
        panelCB.add(cb2);
        panelCB.add(cb3);
        panelCB.add(cb4);

        mainFrame.add(panelCB);


        //radio button
        JRadioButton rb1 = new JRadioButton("radio 1");
        rb1.setSelected(true);

        JRadioButton rb2 = new JRadioButton("radio 2");

        JRadioButton rb3 = new JRadioButton("radio 3");

        JRadioButton rb4 = new JRadioButton("radio 4");

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(rb1);
        group.add(rb2);
        group.add(rb3);
        group.add(rb4);

        JPanel panelRB = new JPanel();
        panelRB.setLayout( new FlowLayout());

        panelRB.add(rb1);
        panelRB.add(rb2);
        panelRB.add(rb3);
        panelRB.add(rb4);

        mainFrame.add(panelRB);


        mainFrame.pack();
        mainFrame.setVisible(true);

    }

}
