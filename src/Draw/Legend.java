package Draw;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Tristan on 25.09.2015.
 */
public class Legend extends JPanel {

    ArrayList<String> names;

    public Legend(ArrayList<String> names){
        this.names = names;
        setSize(1000, 1000);
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

        for(int i = 0; i < names.size(); i++) {
            String numberAndName = String.valueOf(i + 1) + " : " + names.get(i);
            g2.drawString(numberAndName, 10, i*10+10);
        }
    }
}
