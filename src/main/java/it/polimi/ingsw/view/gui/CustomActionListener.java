package it.polimi.ingsw.view.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CustomActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e)
    {
        SecondGui SecGui;
        System.out.println(e.getActionCommand());
        if(e.getActionCommand().equals("EXIT"))
            System.exit(1);
        else
          new  Thread((Runnable) (SecGui = new SecondGui())).run();
            System.exit(1);

    }
}