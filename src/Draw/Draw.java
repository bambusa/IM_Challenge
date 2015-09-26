package Draw;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Tristan on 23.09.2015.
 */

public class Draw extends JFrame {

    Double wWidth;
    Double wHeight;
    //JPanel panel = new JPanel();
    //JButton start = new JButton("Start");

    /*
    public Draw(int width, int height) {
        super();
        wWidth = width/22.5+20;
        wHeight = height/22.5+20;
        setSize(1920, 1080);
        setTitle("IM Challenge");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(null);
        panel.setLayout(null);
        panel.add(start);
        add(panel);
        panel.setLocation(20,40+wHeight.intValue()+20);

//        start.setLayout(null);
//        start.setBounds(60, 400, 220, 30);
//        panel.setBounds(800, 800, 200, 100);
//        panel.add(start);
//        add(panel);
    }*/

    public Draw(int width, int height, ArrayList<ArrayList<Integer>> coords, ArrayList<ArrayList<Integer>> lines, ArrayList<String> names) {
        super();

        setSize(1920, 1080);
        setBackground(Color.WHITE);
        setLayout(null);

        wWidth = width/22.5+60;
        wHeight = height/22.5+60;

        Plan plan = new Plan(wWidth, wHeight, coords, lines);
        //plan.setLayout(new GridLayout(10, 10));
        //plan.setBounds(10, 10, wWidth.intValue(), wHeight.intValue());
        //plan.setBorder(BorderFactory.createTitledBorder("Panel1"));
        add(plan);

        Button button = new Button();
        button.setLocation(20, wHeight.intValue()+40);
        //button.setLayout(new GridLayout(10, 10));
        //button.setBounds(10, wHeight.intValue(), 100, 50);
        //button.setBorder(BorderFactory.createTitledBorder("Panel2"));
        add(button);

        Time time = new Time();

        Legend legend = new Legend(names);
        legend.setLocation(wWidth.intValue()+40, 20);
        add(legend);

        setVisible(true);
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