package sample.algorithms;

import sample.MazeController;
import sample.basics.c_vals;
import sample.basics.cell;
import sample.basics.cellState;

import java.util.LinkedList;

public class depthFirstSearch extends shortestPath {
    public static boolean COMPLETE_DEPTHFIRST = false;
    private LinkedList<cell> prevPath = null;

    public depthFirstSearch() {

    }

    private void DFS(cell curr) {

        // The current is not the SOURCE node
        if (curr.state != cellState.SHORTEST && curr.state != cellState.SOURCE && curr.state != cellState.TARGET) {
            MazeController.PaintBlock(curr.i, curr.j, c_vals.BORDER, c_vals.VIS);
            curr.state = cellState.VISITED;
        }

        for (int k = 0; k < c_vals.TRAV_LEN && runThread && curr.distance < shortestPath && !pathFound; k++) {
            if (inRange(curr.i + Y[k], curr.j + X[k])) {
                cell temp = MazeController.Grid[curr.i + Y[k]][curr.j + X[k]];

                if (curr.distance + 1 < temp.distance) {
                    try {
                        Thread.sleep(c_vals.SLEEP);
                    } catch (Exception ignored) {
                    }

                    temp.distance = curr.distance + 1;
                    temp.setParent(curr.i, curr.j);

                    if (temp.state != cellState.TARGET) {
                        DFS(temp);
                    } else { // IF THE TARGET IS REACHED
                        if (temp.distance < shortestPath) {
                            if (prevPath != null) {
                                colorPath(prevPath, c_vals.VIS, false);
                            }

                            shortestPath = temp.distance;
                            tracePath(temp);

                            if (!c_vals.DFX_EX) runThread = false;
                        }
                        return;
                    }
                }
            }
        }

        if (curr.state != cellState.SHORTEST && curr.state != cellState.SOURCE && curr.state != cellState.TARGET) {
            MazeController.PaintBlock(curr.i, curr.j, c_vals.BORDER, c_vals.UNVISITED);
            curr.state = cellState.UNVISITED;
        }
    }

    public void tracePath(cell temp) {

        LinkedList<cell> shortestPath = new LinkedList<>();
        while (temp.state != cellState.SOURCE) {

            shortestPath.addFirst(temp);
            temp = MazeController.Grid[temp.Pi][temp.Pj];
        }
        prevPath = shortestPath;
        colorPath(shortestPath, c_vals.ANS, true);
    }

    @Override
    public void run() {

        src.distance = 0;
        DFS(src);

        if (pathFound) MazeController.UpdateBorder(c_vals.TGT);
        c_vals.current = null;
        System.out.println("Return Depth-First Search Algorithm Thread");
    }
}
