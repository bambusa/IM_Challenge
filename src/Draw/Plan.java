package Draw;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Tristan on 25.09.2015.
 */
public class Plan extends JPanel {

    Double wWidth;
    Double wHeight;
    ArrayList<ArrayList<Integer>> coords;
    ArrayList<ArrayList<Integer>> lines;

    public Plan(Double wWidth, Double wHeight, ArrayList<ArrayList<Integer>> coords, ArrayList<ArrayList<Integer>> lines) {
        this.wWidth = wWidth;
        this.wHeight = wHeight;
        this.coords = coords;
        this.lines = lines;
        setSize(wWidth.intValue()+20, wHeight.intValue()+20);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(Color.WHITE);
        g2.setColor(Color.BLACK);
        g2.drawRect(20, 20, wWidth.intValue(), wHeight.intValue());

        for(int i = 0; i < coords.size(); i++) {
            String number = String.valueOf(i + 1);
            g2.setColor(Color.RED);
            g2.drawString(number, coords.get(i).get(0), coords.get(i).get(1));
            g2.setColor(Color.BLACK);
            g2.fillOval(coords.get(i).get(0), coords.get(i).get(1), 5, 5);
        }

        for(int i = 0; i < lines.size(); i++) {
            g2.drawLine(lines.get(i).get(0), lines.get(i).get(1), lines.get(i).get(2), lines.get(i).get(3));
        }
    }
}