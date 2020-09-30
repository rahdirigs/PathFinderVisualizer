package sample.algorithms;

import sample.MazeController;
import sample.basics.c_vals;
import sample.basics.cell;
import sample.basics.cellState;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class greedyBestFirst extends shortestPath {
    private LinkedList<cell> prevPath = null;

    public greedyBestFirst() {
    }

    @Override
    public void run() {

        PriorityQueue<cell> pq = new PriorityQueue<cell>(new CompareGreedy(des));
        cell curr, temp;

        src.distance = 0;
        pq.add(src);

        try {
            while (!pq.isEmpty() && !pathFound && runThread) {

                curr = pq.poll();

                // The distance of the last element in the array should be
                // used to check that array's validity
                if (curr.distance + 1 < shortestPath) // if the current front node can be better than the previous one
                {
                    if (!samePoint(src, curr)) // Ignore the source node
                        MazeController.PaintBlock(curr.i, curr.j, c_vals.BORDER, c_vals.VIS);

                    for (int k = 0; k < c_vals.TRAV_LEN; k++) {
                        if (inRange(curr.i + Y[k], curr.j + X[k])) {

                            temp = MazeController.Grid[curr.i + Y[k]][curr.j + X[k]];

                            if (curr.distance + 1 < temp.distance) {
                                temp.distance = curr.distance + 1;
                                temp.setParent(curr.i, curr.j);

                                if (!samePoint(temp, des)) { // next possible visit for BSF
                                    MazeController.PaintBlock(temp.i, temp.j, c_vals.BORDER, c_vals.NXT);
                                    pq.add(temp);
                                } else if (curr.distance < shortestPath) { // IF THE TARGET IS REACHED
                                    if (prevPath != null) {
                                        colorPath(prevPath, c_vals.VIS, false);
                                    }
                                    shortestPath = temp.distance;
                                    tracePath(temp);

                                    pathFound = true;
                                    break;
                                }
                            }
                        }
                    }
                    Thread.sleep(c_vals.SLEEP);
                }
            }
        } catch (Exception ignored) {
        } finally {
            while (!pq.isEmpty()) pq.poll();
        }

        if (pathFound) MazeController.UpdateBorder(c_vals.TGT);
        c_vals.current = null;
        System.out.println("Return Greedy Best-First Thread");
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
}

class CompareGreedy implements Comparator<cell> {

    public static cell dest;

    public CompareGreedy(cell dest) {
        CompareGreedy.dest = dest;
    }

    @Override
    public int compare(cell cell1, cell cell2) {

        long dest1 = calculateHValue(cell1); // only h(cost) is considered
        long dest2 = calculateHValue(cell2); // only h(cost) is considered

        if (dest1 == dest2) return 0;
        else if (dest1 < dest2)
            return -1;
        return 1;
    }

    // No need to do the square root as both are getting square root
    // if A > B => sqrt(A) > sqrt(B)
    public long calculateHValue(cell curr) {
        return (long) (Math.pow(curr.i - dest.i, 2) + Math.pow(curr.j - dest.j, 2));
    }
}
