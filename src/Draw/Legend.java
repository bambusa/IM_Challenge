package Draw;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Tristan on 25.09.2015.
 */
public class Legend extends JPanel {

    ArrayList<String> names;

    public Legend(ArrayList<String> names){
        this.names = names;
        setSize(300, 950);
        JTextArea nameList = new JTextArea();
        nameList.setText("Legende");
        for(int i = 0; i < names.size(); i++) {
            String numberAndName = String.valueOf(i + 1) + " : " + names.get(i);
            nameList.append("\n" + numberAndName);
        }
        nameList.setEditable(false);
        nameList.setRows(56);
        JScrollPane nameScroll = new JScrollPane(nameList);
        add(nameScroll);
    }
}
