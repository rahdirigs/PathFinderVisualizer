package sample.basics;

import sample.algorithms.shortestPath;

public class c_vals {
    public static int ROW = 40, COL = 80;
    public static final int WALL_WT = -1, UNVIS_WT = Integer.MAX_VALUE;

    public static final String BORDER = "black";
    public static final String WALL = "black";
    public static final String UNVISITED = "white";
    public static final String SRC = "orange";
    public static final String TGT = "#ff0000";     // red
    public static final String ANS = "yellow";

    public static final String NXT = "#8BE42D";     //cyan
    public static final String VIS = "blue";

    public static final String NXT2 = "#8BE42D";    // light green
    public static final String VIS2 = "#3F7B00";    // dark green

    public static final String INTR = "#f000ff";    // pink

    public static final String MAZE = "template/";

    public static final int SLEEP = 40;
    public static final int SHT = 30;
    public static final int MZ = 1;

    public static final int MV_ST = 10;
    public static final int MV_DG = 14;

    public static final int NON_DG = 4;
    public static final int DG = 8;

    public static int TRAV_LEN;

    public static boolean DFX_EX, UPD_BOR;

    public static shortestPath current;

    public static final String LOAD = "maze1.txt";
    public static final String SAVE = "untitled.txt";
    public static final String GITHUB = "github.com/rahdirigs";

    private c_vals() {
    }
}
