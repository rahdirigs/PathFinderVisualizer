package sample.algorithms;

import sample.MazeController;
import sample.basics.c_vals;
import sample.basics.cell;
import sample.basics.cellState;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class aStar extends shortestPath {
    private LinkedList<cell> prevPath = null;
    public int shortestPath;

    public aStar() {

    }

    @Override
    public void run() {

        PriorityQueue<cell> pq = new PriorityQueue<cell>(new ComparePoint(des));
        cell curr, temp;
        long gNew, hNew, fNew;

        src.f = src.g = src.h = 0;

        src.inPQ = true;
        pq.add(src);

        try {
            while (!pq.isEmpty() && !pathFound && runThread) {
                curr = pq.poll();
//                System.out.println("Current cell : " + curr);

                if (!samePoint(src, curr)) {
                    MazeController.PaintBlock(curr.i, curr.j, c_vals.BORDER, c_vals.VIS);
                    curr.state = cellState.VISITED;
                }

                for (int k = 0; k < c_vals.TRAV_LEN; k++) {
                    if (inRange(curr.i + Y[k], curr.j + X[k])) {
                        temp = MazeController.Grid[curr.i + Y[k]][curr.j + X[k]];

                        if (samePoint(temp, des)) { // TARGET IS REACHED
                            temp.setParent(curr.i, curr.j);
                            if (prevPath != null) {
                                colorPath(prevPath, c_vals.VIS, false);
                            }
                            tracePath(temp);

                            shortestPath = (int) temp.g;
                            pathFound = true;
                            break;
                        } else {
                            // Neither VISITED, WALL OR SOURCE
                            if (temp.state != cellState.VISITED && temp.state != cellState.WALL &&
                                    temp.state != cellState.SOURCE) {
                                if (k < 4) gNew = curr.g + c_vals.MV_ST; // E-W-N-S     + 10
                                else gNew = curr.g + c_vals.MV_DG;      // NW-NW-SW-SE + 14

                                hNew = calculateDistance(temp);
                                fNew = gNew + hNew;

                                if (temp.f == Float.MAX_VALUE || temp.f > fNew) {
                                    temp.f = fNew;
                                    temp.g = gNew;
                                    temp.h = hNew;
                                    temp.setParent(curr.i, curr.j);

                                    MazeController.PaintBlock(temp.i, temp.j, c_vals.BORDER, c_vals.NXT);

                                    if (!temp.inPQ) {
//                                        System.out.println("Into PQ : " + temp);
                                        temp.inPQ = true;
                                        pq.add(temp);
                                    } //else {
//                                        System.out.println("Updated : "  + temp);
//                                  }
                                }
                            }
                        }
                    }
                }
                Thread.sleep(c_vals.SLEEP);

                if (!pq.isEmpty()) pq.add(pq.poll());
            }
        } catch (Exception ignored) {
        } finally {
            while (!pq.isEmpty()) pq.poll();
        }

        if (pathFound) MazeController.UpdateBorder(c_vals.TGT);
        c_vals.current = null;
        System.out.println("Return A* Search Algorithm Thread");
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

    private int calculateDistance(cell curr) {
        return Math.min(diagonal(curr, Math.abs(des.i - curr.i)), diagonal(curr, Math.abs(des.j - curr.j)));
    }

    private int diagonal(cell curr, int diagonal) {
        int fi = 1, fj = 1;

        if (curr.j < des.j) fj = -1;
        if (curr.i < des.i) fi = -1;

        int newI = des.i + fi * diagonal;
        int newJ = des.j + fj * diagonal;

        int straight = Math.abs(curr.j - newJ) + Math.abs(curr.i - newI);

        return diagonal * c_vals.MV_DG + straight * c_vals.MV_ST;
    }
}

class ComparePoint implements Comparator<cell> {
    public static cell dest;

    public ComparePoint(cell dest) {
        ComparePoint.dest = dest;
    }

    @Override
    public int compare(cell cell1, cell cell2) {
        if (cell1.f == cell2.f) {// same f
            if (cell1.h == cell2.h) { // same h
                if (calculateHValue(cell1) < calculateHValue(cell2)) {
                    return -1;
                }
                return 1;
            } else if (cell1.h < cell2.h) {
                return -1;
            }
            return 0;
        }
        if (cell1.f < cell2.f) return -1;
        return 1;
    }

    // No need to do the square root as both are getting square root
    // if A > B => sqrt(A) > sqrt(B)
    public static long calculateHValue(cell curr) {
        return (long) (Math.pow(curr.i - dest.i, 2) + Math.pow(curr.j - dest.j, 2));
    }
}
