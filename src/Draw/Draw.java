package Draw;

import javax.swing.*;
import java.awt.*;

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
/*
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setBackground(Color.WHITE);
        g2.clearRect(20, 40, wWidth.intValue(), wHeight.intValue());
        g2.setColor(Color.ORANGE);
        g2.fillRect(40, 40, 100, 60);
        g2.setColor(Color.GREEN);
        g2.fillRoundRect(170, 40, 100, 60, 45, 90);
        g2.setColor(Color.BLUE);
        g2.drawOval(40, 105, 100, 60);
        g2.setColor(Color.RED);
        g2.fillOval(170, 105, 100, 60);
    }
*/

    public Draw(int width, int height) {
        super();

        setSize(1920, 1080);
        setBackground(Color.WHITE);
        setVisible(true);

        wWidth = width/22.5+20;
        wHeight = height/22.5+20;

        Plan plan = new Plan(wWidth, wHeight);
        add(plan);
        Button button = new Button();
        add(button);
        Time time = new Time();
        add(time);
        Legend legend = new Legend();
        add(legend);
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



/*
import java.applet.Applet;
import java.awt.*;

public class Draw<T> extends Applet{

    int width;
    int height;

    public Draw() {}

    public void startPainting(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void paint(Graphics graph) {

        graph.drawRect(50, 50, width, height);

    }

}
*/