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

    public Draw(int width, int height, ArrayList<ArrayList<Integer>> coords, ArrayList<ArrayList<Integer>> lines, ArrayList<String> names, ArrayList<ArrayList<Integer>> bestLines, ArrayList<String> color) {
        super();

        setSize(1700, 1000);
        setBackground(Color.WHITE);
        setLayout(null);

        wWidth = width/22.5+60;
        wHeight = height/22.5+60;

        Plan plan = new Plan(wWidth, wHeight, coords, lines, bestLines, color);
        add(plan);

        Legend legend = new Legend(names);
        legend.setLocation(wWidth.intValue() + 40, 20);
        add(legend);

        setVisible(true);
        setBackground(Color.WHITE);
    }
}