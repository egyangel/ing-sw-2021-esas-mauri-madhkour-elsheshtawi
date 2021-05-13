package it.polimi.ingsw.view.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// till now is useless,because i implemented the action inside the buttons
class CustomActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e)
    {

        System.out.println(e.getActionCommand());
        if(e.getActionCommand().equals("EXIT"))
            System.exit(1);
        else {

            //System.exit(1);
        }
    }
}