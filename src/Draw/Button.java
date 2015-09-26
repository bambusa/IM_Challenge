package Draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tristan on 25.09.2015.
 */
public class Button extends JPanel {

    Graphics g;

    public Button(JPanel plan) {
        setSize(70, 40);
        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plan.update(g);
            }
        });
        add(start);
    }
}