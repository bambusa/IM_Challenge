package Draw;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tristan on 25.09.2015.
 */
public class Build extends JPanel {

    Double wWidth;
    Double wHeight;

    public Build(Double wWidth, Double wHeight) {
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
        g2.clearRect(20, 20, wWidth.intValue(), wHeight.intValue());
        g2.setColor(Color.ORANGE);
        g2.fillRect(40, 40, 100, 60);
        g2.setColor(Color.GREEN);
        g2.fillRoundRect(170, 40, 100, 60, 45, 90);
        g2.setColor(Color.BLUE);
        g2.drawOval(40, 105, 100, 60);
        g2.setColor(Color.RED);
        g2.fillOval(170, 105, 100, 60);
    }
}