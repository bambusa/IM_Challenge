package Draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tristan on 25.09.2015.
 */
public class Button extends JPanel {

    private Timer timer = null;

    public Button(JPanel plan) {

        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                plan.repaint();
            }
        });

        setSize(70, 40);
        /*
        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plan.repaint();
            }
        });
        */
        add(start);
    }
}