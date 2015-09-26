package Draw;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Tristan on 23.09.2015.
 */

public class Draw extends JFrame {

    Double wWidth;
    Double wHeight;

    public Draw(int width, int height, ArrayList<ArrayList<Integer>> coords, ArrayList<ArrayList<Integer>> lines, ArrayList<String> names) {
        super();

        setSize(1920, 1080);
        setBackground(Color.WHITE);
        setLayout(null);

        wWidth = width/22.5+60;
        wHeight = height/22.5+60;

        Plan plan = new Plan(wWidth, wHeight, coords, lines);
        add(plan);

        Button button = new Button();
        button.setLocation(20, wHeight.intValue()+40);
        add(button);

        Time time = new Time();
        time.setLocation(100, wHeight.intValue()+40);
        add(time);

        Legend legend = new Legend(names);
        legend.setLocation(wWidth.intValue()+40, 20);
        add(legend);

        setVisible(true);
        setBackground(Color.WHITE);
        //pack();
        //addKeyListener(this);
        //repaint();
    }
/*
    @Override
    public void repaint() {
        super.repaint();
    }*/
}