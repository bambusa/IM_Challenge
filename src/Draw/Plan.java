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
    ArrayList<ArrayList<Integer>> bestLines;
    ArrayList<String> color;
    boolean repaint = false;
    Timer timer = null;
    int bestAX;
    int bestAY;
    int bestBX;
    int bestBY;
    int activeLine;
    int r = 0;
    String activeColor = new String();
    String[] colorSet;
    ArrayList<Integer> activeColInt = new ArrayList<>();

    public Plan(Double wWidth, Double wHeight, ArrayList<ArrayList<Integer>> coords, ArrayList<ArrayList<Integer>> lines, ArrayList<ArrayList<Integer>> bestLines, ArrayList<String> color) {
        this.wWidth = wWidth;
        this.wHeight = wHeight;
        this.coords = coords;
        this.lines = lines;
        this.bestLines = bestLines;
        this.color = color;

        timer = new Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < bestLines.size(); i++) {
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

        for (int i = 0; i < lines.size(); i++) {
            g2.drawLine(lines.get(i).get(0), lines.get(i).get(1), lines.get(i).get(2), lines.get(i).get(3));
        }

        /*
        if(!repaint) {
            for (int i = 0; i < lines.size(); i++) {
                g2.drawLine(lines.get(i).get(0), lines.get(i).get(1), lines.get(i).get(2), lines.get(i).get(3));
            }
        }
        */

        if(repaint) {
            //for (int i = 0; i < bestRoute.size(); i++) {
            bestAX = bestLines.get(r).get(0);
            bestAY = bestLines.get(r).get(1);
            bestBX = bestLines.get(r).get(2);
            bestBY = bestLines.get(r).get(3);
            activeLine = bestLines.get(r).get(4);
            switch (activeLine) {
                case 1:
                    activeColor = color.get(0);
                    break;
                case 2:
                    activeColor = color.get(1);
                    break;
                case 3:
                    activeColor = color.get(2);
                    break;
                case 4:
                    activeColor = color.get(3);
                    break;
                case 5:
                    activeColor = color.get(4);
                    break;
                case 6:
                    activeColor = color.get(5);
                    break;
                case 7:
                    activeColor = color.get(6);
                    break;
                case 8:
                    activeColor = color.get(7);
                    break;
                case 9:
                    activeColor = color.get(8);
                    break;
                case 10:
                    activeColor = color.get(9);
                    break;
                case 11:
                    activeColor = color.get(10);
                    break;
                case 12:
                    activeColor = color.get(11);
                    break;
                case 13:
                    activeColor = color.get(12);
                    break;
            }
            colorSet = activeColor.split(",");
            activeColInt.add(0, Integer.parseInt(colorSet[0]));
            activeColInt.add(1, Integer.parseInt(colorSet[1]));
            activeColInt.add(2, Integer.parseInt(colorSet[2]));
            Color col = new Color(activeColInt.get(0), activeColInt.get(1), activeColInt.get(2));
            g2.setColor(col);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(bestAX, bestAY, bestBX, bestBY);
            r++;
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