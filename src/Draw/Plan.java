package Draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    boolean repaint = false;
    Timer timer = null;
    int bestAX;
    int bestAY;
    int bestBX;
    int bestBY;

    public Plan(Double wWidth, Double wHeight, ArrayList<ArrayList<Integer>> coords, ArrayList<ArrayList<Integer>> lines, ArrayList<ArrayList<Integer>> bestRoute) {
        this.wWidth = wWidth;
        this.wHeight = wHeight;
        this.coords = coords;
        this.lines = lines;
        this.bestRoute = bestRoute;

        timer = new Timer(30, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < bestRoute.size(); i++) {
                    bestAX = bestRoute.get(i).get(0);
                    bestAY = bestRoute.get(i).get(1);
                    bestBX = bestRoute.get(i).get(2);
                    bestBY = bestRoute.get(i).get(3);
                    repaint();
                }
            }
        });

        setSize(wWidth.intValue() + 20, wHeight.intValue() + 100);

        JPanel panel = new JPanel();

        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRepaint(true);
                timer.start();
                //repaint();
            }
        });
        panel.add(start);

        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRepaint(false);
                repaint();
            }
        });
        panel.add(reset);

        JTextArea clock = new JTextArea();
        clock.setText("Startzeit: ");
        clock.append("\nDauer: ");
        clock.append("\nAktuelle Zeit: ");
        clock.setEditable(false);
        clock.setRows(3);
        panel.add(clock);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.PAGE_END);
    }

    @Override
    public void update(Graphics g) { paint(g); }

    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setBackground(Color.WHITE);
        g2.setColor(Color.BLACK);
        g2.drawRect(20, 20, wWidth.intValue() - 10, wHeight.intValue() - 10);

        for (int i = 0; i < coords.size(); i++) {
            String number = String.valueOf(i + 1);
            g2.setColor(Color.RED);
            g2.drawString(number, coords.get(i).get(0) - 3, coords.get(i).get(1) - 3);
            g2.setColor(Color.BLACK);
            g2.fillOval(coords.get(i).get(0) - 3, coords.get(i).get(1) - 3, 6, 6);
        }

        if(!repaint) {
            for (int i = 0; i < lines.size(); i++) {
                g2.drawLine(lines.get(i).get(0), lines.get(i).get(1), lines.get(i).get(2), lines.get(i).get(3));
            }
        }

        if(repaint) {
            //for (int i = 0; i < bestRoute.size(); i++) {
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(bestAX, bestAY, bestBX, bestBY);
            //}
        }
    }

    public void setRepaint(boolean b){
        repaint = b;
    }

    public boolean getRepaint() {
        return repaint;
    }
}