package sample.files;

import sample.MazeController;
import sample.basics.c_vals;
import sample.basics.cell;
import sample.basics.cellState;
import sample.basics.stateNum;
import sample.maze.mazegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class readWrite {
    private readWrite() {}

    public static void save(String path) {
        try {
            FileWriter fw = new FileWriter(path);

            StringBuilder sb = new StringBuilder();
            boolean possibleSave = false;

            for(int i = 0; i < c_vals.ROW; i++) {
                for(int j = 0; j < c_vals.COL; j++) {
                    if(MazeController.Grid[i][j].state == cellState.WALL)
                        possibleSave = true;

                    sb.append(stateNum.getChar(MazeController.Grid[i][j].state));
                }
                if(i != c_vals.ROW-1) sb.append("\n");
            }

            if(!possibleSave) { // the maze is not saved, delete the file, and close the FileWriter
                fw.close(); File file = new File(path);
                file.delete(); // delete the file if the file is empty

                return;
            }

            fw.write(sb.toString());
            fw.close();
        }
        catch (Exception ignored) { }
    }

    public static ArrayList<cell> read(String path)
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            ArrayList<cell> wallAr = new ArrayList<>();
            cell curr;

            for(int i = 0; i < c_vals.ROW; i++) {
                String str = br.readLine();
                for(int j = 0; j < c_vals.COL; j++) {

                    curr = MazeController.Grid[i][j];

                    if(curr.state != cellState.SOURCE && curr.state != cellState.TARGET) {
                        MazeController.PaintBlock(i, j, c_vals.BORDER, c_vals.UNVISITED);

                        curr.state = stateNum.getNum(str.charAt(j));
                    }
                    if(curr.state == cellState.WALL) {
                        wallAr.add(new cell(i, j));
                    }
                }
            }
            return wallAr;
        }
        catch (Exception ignored) { }
        return null;
    }

    public static ArrayList<cell> loadRandomMaze()
    {
        String maze = mazegen.buildMaze();

        ArrayList<cell> wallAr = new ArrayList<>();
        cell curr;

        for(int i = 0; i < c_vals.ROW; i++) {
            for(int j = 0; j < c_vals.COL; j++) {

                curr = MazeController.Grid[i][j];

                if(curr.state != cellState.SOURCE && curr.state != cellState.TARGET) {
                    MazeController.PaintBlock(i, j, c_vals.BORDER, c_vals.UNVISITED);

                    curr.state = stateNum.getNum(maze.charAt(i*c_vals.ROW + j));
                }
                if(curr.state == cellState.WALL) {
                    wallAr.add(new cell(i, j));
                }
            }
        }
        return wallAr;
    }
}
