package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BSD on 10.07.2015.
 */
public class Transfer {

    private String id; // VHSBNR + NHSBNR
    private String vhsbnr;
    private String nhsbnr;
    private int time;

    public Transfer (String id, String vhsbnr, String nhsbnr, int time) {
        this.id = id;
        this.vhsbnr = vhsbnr;
        this.nhsbnr = nhsbnr;
        this.time = time;
    }

    public String getId() {
        return id;
    }
    public int getTime() {
        return time;
    }

    public String getVhsbnr() {
        return vhsbnr;
    }

    public String getNhsbnr() {
        return nhsbnr;
    }
}
