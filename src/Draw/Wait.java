package Draw;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tristan on 29.09.2015.
 */

public class Wait {

    JFrame waitFrame;

    public Wait(){
        waitFrame = new JFrame("IM Challenge");
        waitFrame.setLocation(300, 150);
        waitFrame.setLayout(new FlowLayout());
        waitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea waitText = new JTextArea("\n\nBitte warten.\nBerechnung wird durchgef\u00FChrt.", 5, 10);
        waitText.setPreferredSize(new Dimension(200, 100));
        waitText.setLineWrap(true);
        waitText.setEditable(false);
        waitFrame.add(waitText);
        waitFrame.pack();
        waitFrame.setVisible(true);
    }

    public void close(){
        waitFrame.setVisible(false);
    }
}
