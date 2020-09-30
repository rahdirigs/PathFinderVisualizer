package sample.algorithms;

import sample.MazeController;
import sample.basics.c_vals;
import sample.basics.cell;
import sample.basics.cellState;

import java.util.LinkedList;
import java.util.Queue;

public class grassFire extends shortestPath {
    public grassFire() {
    }

    @Override
    public void run() {

        Queue<cell> queue = new LinkedList<>();
        cell curr, temp;

        src.distance = 0;
        queue.add(src);

        try {
            while (!queue.isEmpty() && !pathFound && runThread) {

                curr = queue.poll();

                if (curr.state != cellState.SOURCE)
                    MazeController.PaintBlock(curr.i, curr.j, c_vals.BORDER, c_vals.VIS);

                for (int k = 0; k < c_vals.TRAV_LEN && !pathFound; k++) {

                    if (inRange(curr.i + Y[k], curr.j + X[k])) {

                        temp = MazeController.Grid[curr.i + Y[k]][curr.j + X[k]];

                        if (temp.state == cellState.UNVISITED || temp.state == cellState.TARGET) {
                            temp.distance = curr.distance + 1;
                            temp.setParent(curr.i, curr.j);

                            if (temp.state != cellState.TARGET) {
                                MazeController.PaintBlock(temp.i, temp.j, c_vals.BORDER, c_vals.NXT);
                            } else { // destination reached
                                tracePath(temp); // TracePath of the Destination Node and print its path, Target node is not saved

                                shortestPath = temp.distance;
                                pathFound = true;
                                break;
                            }

                            MazeController.Grid[temp.i][temp.j].state = cellState.VISITED;
                            queue.add(temp);
                        }
                    }
                }
                Thread.sleep(c_vals.SLEEP);
            }
        } catch (Exception ignored) {
        } finally {
            while (!queue.isEmpty()) queue.poll();
        }

        if (pathFound) MazeController.UpdateBorder(c_vals.TGT);
        c_vals.current = null;
        System.out.println("Return Breadth-First Search Algorithm Thread");
    }
}
