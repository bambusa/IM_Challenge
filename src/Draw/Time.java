package Draw;

import javax.swing.*;

/**
 * Created by Tristan on 25.09.2015.
 */
public class Time extends JPanel {

    public Time(){
        setSize(300, 950);
        JTextArea clock = new JTextArea();
        clock.setText("Startzeit: ");
        clock.append("\nDauer: ");
        clock.append("\nAktuelle Zeit: ");
        clock.setEditable(false);
        clock.setRows(3);
        add(clock);
    }
}
