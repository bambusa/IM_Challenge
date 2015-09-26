package Draw;

import Models.Edge;

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
    ArrayList<ArrayList<Integer>> bestRoute;
    boolean update = false;

    public Plan(Double wWidth, Double wHeight, ArrayList<ArrayList<Integer>> coords, ArrayList<ArrayList<Integer>> lines, ArrayList<ArrayList<Integer>> bestRoute) {
        this.wWidth = wWidth;
        this.wHeight = wHeight;
        this.coords = coords;
        this.lines = lines;
        this.bestRoute = bestRoute;
        setSize(wWidth.intValue() + 20, wHeight.intValue() + 20);
    }

    @Override
    public void update(Graphics g) {
        this.update = true;
        paint(g);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(Color.WHITE);
        g2.setColor(Color.BLACK);
        g2.drawRect(20, 20, wWidth.intValue()-10, wHeight.intValue()-10);

        if(!update) {
            for (int i = 0; i < coords.size(); i++) {
                String number = String.valueOf(i + 1);
                g2.setColor(Color.RED);
                g2.drawString(number, coords.get(i).get(0) - 3, coords.get(i).get(1) - 3);
                g2.setColor(Color.BLACK);
                g2.fillOval(coords.get(i).get(0) - 3, coords.get(i).get(1) - 3, 6, 6);
            }

            for (int i = 0; i < lines.size(); i++) {
                g2.drawLine(lines.get(i).get(0), lines.get(i).get(1), lines.get(i).get(2), lines.get(i).get(3));
            }
        }
        if(update){
            int xA = bestRoute.get(0).get(0);
            int yA = bestRoute.get(0).get(1);
            int xB = bestRoute.get(0).get(2);
            int yB = bestRoute.get(0).get(3);
            g2.drawLine(xA, yA, xB, yB);
        }

    }

    /*
    @Override
    public void repaint() {
        super.repaint();
    }
    */
}