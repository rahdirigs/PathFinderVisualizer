package sample.algorithms;

import sample.MazeController;
import sample.basics.c_vals;
import sample.basics.cell;
import sample.basics.cellState;

import java.util.LinkedList;
import java.util.ListIterator;

public class shortestPath extends Thread {
    public static int[] Y = {-1, 0, 1, 0, -1, -1, 1, 1};
    public static int[] X = {0, 1, 0, -1, -1, 1, 1, -1};

    protected cell src;
    protected cell des;

    protected boolean runThread;
    protected boolean pathFound;
    protected int shortestPath;

    public void algorithm(cell src, cell des) {
        this.src = src;
        this.des = des;

        shortestPath = Integer.MAX_VALUE;
        pathFound = false;
        runThread = true;
    }

    public boolean inRange(int r, int c) {
        return (r >= 0 && r < c_vals.ROW) && (c >= 0 && c < c_vals.COL);
    }

    public boolean samePoint(cell x, cell y) {
        return x.i == y.i && x.j == y.j;
    }

    public void colorPath(LinkedList<cell> shortestPath, String Color, boolean shortPath) {
        cell prev = null, curr = null;

        ListIterator<cell> iterator = shortestPath.listIterator();
        while (iterator.hasNext() && runThread) {
            curr = iterator.next();

            if (!shortPath) { // Change its state to DEFAULT_VISITED state
                MazeController.PaintBlock(curr.i, curr.j, c_vals.BORDER, Color);
                MazeController.Grid[curr.i][curr.j].state = cellState.VISITED;

                prev = curr;
            } else {
                if (prev != null) {
                    MazeController.PaintBlock(prev.i, prev.j, c_vals.BORDER, Color);
                }
                MazeController.PaintBlock(curr.i, curr.j, c_vals.BORDER, c_vals.TGT);
                curr.state = cellState.SHORTEST;

                prev = curr;
                try {
                    MazeController.Grid[curr.i][curr.j].state = cellState.SHORTEST;
                    Thread.sleep(c_vals.SHT);
                } catch (Exception ignored) {
                }
            }
        }

        if (prev != null) MazeController.PaintBlock(prev.i, prev.j, c_vals.BORDER, c_vals.TGT);
    }

    protected void tracePath(cell temp) {

        LinkedList<cell> shortestPath = new LinkedList<>();
        while (temp.state != cellState.SOURCE) {

            shortestPath.addFirst(temp);
            temp = MazeController.Grid[temp.Pi][temp.Pj];
        }
        colorPath(shortestPath, c_vals.ANS, true);
    }

    public void killThread() {
        System.out.println("Kill thread");

        runThread = false;
        pathFound = true;
        this.interrupt();
    }
}