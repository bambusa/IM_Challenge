package Draw;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

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
    HashMap<String, ArrayList<Integer>> redraw = new HashMap<>();

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
            }
        });
        panel.add(start);

        /*
        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRepaint(false);
                repaint();
                timer.restart();
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
        */

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

        if(repaint) {
            /*
            Iterator it = redraw.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                Object redrawArray;
                redrawArray = pair.getValue();
                System.out.println(redrawArray);
                String string = redrawArray.toString();
                System.out.println(string);
                String[] stringZwei = string.split(", ");
                System.out.println(stringZwei[0] + stringZwei[1] + stringZwei[2] + stringZwei[3]);
                stringZwei[0] = stringZwei[0].replace("[", "");
                stringZwei[3] = stringZwei[3].replace("]", "");

                bestAX = Integer.parseInt(stringZwei[0]);
                bestAY = Integer.parseInt(stringZwei[1]);
                bestBX = Integer.parseInt(stringZwei[2]);
                bestBY = Integer.parseInt(stringZwei[3]);
                activeLine = bestLines.get(r).get(4);
                activeColor = color.get(activeLine - 1);
                colorSet = activeColor.split(",");
                activeColInt.add(0, Integer.parseInt(colorSet[0]));
                activeColInt.add(1, Integer.parseInt(colorSet[1]));
                activeColInt.add(2, Integer.parseInt(colorSet[2]));
                System.out.println(activeColInt.get(0) + "," + activeColInt.get(1) + "," + activeColInt.get(2));
                Color col = new Color(activeColInt.get(0), activeColInt.get(1), activeColInt.get(2));
                g2.setColor(col);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(bestAX, bestAY, bestBX, bestBY);

                it.remove(); // avoids a ConcurrentModificationException
            }
            */

            for(ArrayList<Integer> thisArray : redraw.values()) {
                bestAX = thisArray.get(0);
                bestAY = thisArray.get(1);
                bestBX = thisArray.get(2);
                bestBY = thisArray.get(3);
                activeLine = bestLines.get(r).get(4);
                activeColor = color.get(activeLine - 1);
                colorSet = activeColor.split(",");
                activeColInt.add(0, Integer.parseInt(colorSet[0]));
                activeColInt.add(1, Integer.parseInt(colorSet[1]));
                activeColInt.add(2, Integer.parseInt(colorSet[2]));
                System.out.println(activeColInt.get(0) + "," + activeColInt.get(1) + "," + activeColInt.get(2));
                Color col = new Color(activeColInt.get(0), activeColInt.get(1), activeColInt.get(2));
                g2.setColor(col);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(bestAX, bestAY, bestBX, bestBY);

            }

            bestAX = bestLines.get(r).get(0);
            bestAY = bestLines.get(r).get(1);
            bestBX = bestLines.get(r).get(2);
            bestBY = bestLines.get(r).get(3);
            activeLine = bestLines.get(r).get(4);
            activeColor = color.get(activeLine - 1);
            colorSet = activeColor.split(",");
            activeColInt.add(0, Integer.parseInt(colorSet[0]));
            activeColInt.add(1, Integer.parseInt(colorSet[1]));
            activeColInt.add(2, Integer.parseInt(colorSet[2]));
            System.out.println(activeColInt.get(0) + "," + activeColInt.get(1) + "," + activeColInt.get(2));
            Color col = new Color(activeColInt.get(0), activeColInt.get(1), activeColInt.get(2));
            g2.setColor(col);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(bestAX, bestAY, bestBX, bestBY);

            if(redraw.containsKey(bestLines.get(r).get(0) + bestLines.get(r).get(2) + bestLines.get(r).get(1) + bestLines.get(r).get(3))){
                g2.setColor(Color.BLACK);
                g2.drawLine(bestLines.get(r).get(0), bestLines.get(r).get(1), bestLines.get(r).get(2), bestLines.get(r).get(3));
                System.out.println(redraw.get(bestAX + bestBX + bestAY + bestBY).get(0));
            }

            if(!redraw.containsKey(bestLines.get(r).get(0) + bestLines.get(r).get(2) + bestLines.get(r).get(1) + bestLines.get(r).get(3))){
                ArrayList<Integer> redrawArray = new ArrayList<>();
                redrawArray.add(0, bestAX);
                redrawArray.add(1, bestAY);
                redrawArray.add(2, bestBX);
                redrawArray.add(3, bestBY);
                redraw.put("" + bestAX + bestBX + bestAY + bestBY, redrawArray);
            }
            r++;
        }
    }

    public void setRepaint(boolean b){
        repaint = b;
    }

    public boolean getRepaint() {
        return repaint;
    }
}