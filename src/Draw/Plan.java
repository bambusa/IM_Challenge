package Draw;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tristan on 25.09.2015.
 */

public class Plan extends JPanel {

    Double wWidth;
    Double wHeight;

    public Plan(Double wWidth, Double wHeight) {
        this.wWidth = wWidth;
        this.wHeight = wHeight;
        setSize(wWidth.intValue(), wHeight.intValue());
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
        g2.fillOval(1, 1, 5, 5);
    }
}