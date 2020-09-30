package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import sample.algorithms.*;
import sample.basics.c_vals;
import sample.basics.cell;
import sample.basics.cellState;
import javafx.event.ActionEvent;
import sample.files.readWrite;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class MazeController implements Initializable {
    @FXML
    private GridPane platform;

    @FXML
    private ComboBox<String> algoOptions;
    @FXML
    private ToggleSwitch updateBorder;

    @FXML
    private JFXButton clearButton, intermediateButton, pathButton, mazeButton, sourceButton,
            targetButton, drawButton, saveButton, loadButton, cancelButton;

    @FXML
    private JFXButton visualButton;
    @FXML
    private JFXCheckBox diagonalCheckBox;
    @FXML
    private JFXCheckBox dfsCheckBox;

    public static GridPane GridPanel;

    private cellState curState;
    private int algoIndex;
    private boolean applyColor;

    private int[][] currSD = new int[3][2];

    public static BorderPane[][] borderGrid = new BorderPane[c_vals.ROW][c_vals.COL];
    public static cell[][] Grid = new cell[c_vals.ROW][c_vals.COL];


    void constructcell(int i, int j) {

        BorderPane pane = new BorderPane();

        pane.setStyle("-fx-border-color: " + c_vals.BORDER + "; -fx-background-color: " + c_vals.UNVISITED + ";");
        platform.add(pane, j, i);
        borderGrid[i][j] = pane;

        pane.setOnMouseEntered(event -> {
            if (curState == cellState.WALL) {
                // NEED TO WORK ON THIS FUNCTION
                if (Grid[i][j].state != cellState.SOURCE && Grid[i][j].state != cellState.TARGET && Grid[i][j].state != cellState.INTERMEDIATE) {
                    if (applyColor) {
                        pane.setStyle("-fx-border-color: " + c_vals.BORDER + "; -fx-background-color: " + c_vals.WALL + ";");
                        Grid[i][j].state = cellState.WALL;
                    }
                }
            } else if (curState == cellState.UNVISITED) {
                if (Grid[i][j].state != cellState.SOURCE && Grid[i][j].state != cellState.TARGET && Grid[i][j].state != cellState.INTERMEDIATE) {
                    if (applyColor) {
                        pane.setStyle("-fx-border-color: " + c_vals.BORDER + "; -fx-background-color: " + c_vals.UNVISITED + ";");
                        Grid[i][j].state = cellState.UNVISITED;
                    }
                }
            }
        });

        pane.setOnMouseClicked(event -> {

            // 0 -> source
            // 1 -> target
            // 2 -> intermediate

            if (curState == cellState.SOURCE) {
                if (Grid[i][j].state == cellState.SOURCE) { // Remove it from the board
                    Grid[i][j].state = cellState.UNVISITED;
                    unVisitPreviousBlock(0);
                } else if (Grid[i][j].state == cellState.UNVISITED) {
                    unVisitPreviousBlock(0);
                    PaintBlock(i, j, c_vals.BORDER, c_vals.SRC);
                    currSD[0][0] = i;
                    currSD[0][1] = j;

                    Grid[i][j].state = cellState.SOURCE;
                }
            } else if (curState == cellState.TARGET) {
                if (Grid[i][j].state == cellState.TARGET) { // Remove it from the board
                    Grid[i][j].state = cellState.UNVISITED;
                    unVisitPreviousBlock(1);
                } else if (Grid[i][j].state == cellState.UNVISITED) {
                    unVisitPreviousBlock(1);
                    PaintBlock(i, j, c_vals.BORDER, c_vals.TGT);
                    currSD[1][0] = i;
                    currSD[1][1] = j;

                    Grid[i][j].state = cellState.TARGET;
                }
            } else if (curState == cellState.INTERMEDIATE) {

                if (Grid[i][j].state == cellState.INTERMEDIATE) {
                    unVisitPreviousBlock(2);
                    currSD[2][0] = -1;
                    currSD[2][1] = -1;

                    Grid[i][j].state = cellState.UNVISITED;
                } else if (Grid[i][j].state == cellState.UNVISITED) {
                    unVisitPreviousBlock(2);

                    PaintBlock(i, j, c_vals.BORDER, c_vals.INTR);
                    Grid[i][j].state = cellState.INTERMEDIATE;
                    currSD[2][0] = i;
                    currSD[2][1] = j;
                }
            }

            if (curState == cellState.WALL || curState == cellState.UNVISITED) {
                applyColor = !(applyColor);

                if (applyColor && Grid[i][j].state != cellState.SOURCE && Grid[i][j].state != cellState.TARGET) {
                    // Apply Wall on the current Grid box
                    if (curState == cellState.WALL) {
                        PaintBlock(i, j, c_vals.BORDER, c_vals.WALL);
                        Grid[i][j].state = cellState.WALL;
                    }
                    // Apply Unvisited on the current Grid box
                    else {
                        PaintBlock(i, j, c_vals.BORDER, c_vals.UNVISITED);
                        Grid[i][j].state = cellState.UNVISITED;
                    }
                }
            }
        });

        Grid[i][j].state = cellState.UNVISITED; // is it default
    }

    private void unVisitPreviousBlock(int row) {
        if (currSD[row][0] != -1) {
            PaintBlock(currSD[row][0], currSD[row][1], c_vals.BORDER, c_vals.UNVISITED);
            Grid[currSD[row][0]][currSD[row][1]].state = cellState.UNVISITED;
        }
    }

    public synchronized static void PaintBlock(int i, int j, String border, String fill) {
        borderGrid[i][j].setStyle("-fx-border-color: " + border + "; -fx-background-color: " + fill + ";");
    }

    private void buttonStateChange(boolean disable) {
        pathButton.setDisable(disable);
        mazeButton.setDisable(disable);
        intermediateButton.setDisable(disable);
        clearButton.setDisable(disable);
    }

    public static void UpdateBorder(String color) {
        if (c_vals.UPD_BOR) {
            GridPanel.setStyle("-fx-border-color: " + color);
        }
    }

    private void clearBoardPath(boolean keepPath) {
        for (int i = 0; i < c_vals.ROW; i++) {
            for (int j = 0; j < c_vals.COL; j++) {

                if (keepPath && Grid[i][j].state == cellState.WALL) continue;

                PaintBlock(i, j, c_vals.BORDER, c_vals.UNVISITED);
                Grid[i][j].state = cellState.UNVISITED;
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        platform.setStyle("-fx-border-color: " + c_vals.WALL);
        GridPanel = platform;

        for (int i = 0; i < c_vals.ROW; i++) {
            for (int j = 0; j < c_vals.COL; j++) {
                Grid[i][j] = new cell(i, j);
                constructcell(i, j);
            }
        }
        for (int i = 0; i < 3; i++) currSD[i][0] = -1;

        algoOptions.getItems().addAll("Breadth First Search", "Depth First Search",
                "A* Algorithm", "Greedy Best-First");

        algoOptions.setOnAction(event -> algoIndex = algoOptions.getSelectionModel().getSelectedIndex());

        applyColor = false;
        curState = null;
        algoIndex = -1;
    }

    private void updateGrid() {
        for (int i = 0; i < c_vals.ROW; i++) {
            for (int j = 0; j < c_vals.COL; j++) {

                Grid[i][j].setParent(-1, -1); // Set parent to null

                if (algoIndex == 2) Grid[i][j].AStarInit();

                // Remove Everything except the walls
                if (Grid[i][j].state != cellState.WALL) {
                    PaintBlock(i, j, c_vals.BORDER, c_vals.UNVISITED);
                    Grid[i][j].state = cellState.UNVISITED;

                    Grid[i][j].distance = c_vals.UNVIS_WT;
                } else {
                    Grid[i][j].distance = c_vals.WALL_WT;
                }
            }
        }

        // Regain the Source and Target point
        PaintBlock(currSD[0][0], currSD[0][1], c_vals.BORDER, c_vals.SRC);
        PaintBlock(currSD[1][0], currSD[1][1], c_vals.BORDER, c_vals.TGT);

        if (currSD[2][0] != -1) {
            PaintBlock(currSD[2][0], currSD[2][1], c_vals.BORDER, c_vals.INTR);
            Grid[currSD[2][0]][currSD[2][1]].state = cellState.INTERMEDIATE;
        }

        Grid[currSD[0][0]][currSD[0][1]].state = cellState.SOURCE;
        Grid[currSD[1][0]][currSD[1][1]].state = cellState.TARGET;
    }

    @FXML
    void visualActionEvent(ActionEvent event) {
        // If both the source and destination points are given
        applyColor = false;
        if (c_vals.current == null) {
            if (currSD[0][0] != -1 && currSD[1][0] != -1 && algoIndex != -1) {

                shortestPath algo = null;
                updateGrid();

                c_vals.UPD_BOR = updateBorder.isSelected();
                // UpdateBorder(c_vals.WALL);

                if (diagonalCheckBox.isSelected()) c_vals.TRAV_LEN = c_vals.DG;
                else c_vals.TRAV_LEN = c_vals.NON_DG;

                c_vals.DFX_EX = dfsCheckBox.isSelected();

                switch (algoIndex) {
                    case 0:
                        algo = new grassFire();
                        break;
                    case 1:
                        algo = new depthFirstSearch();
                        break;
                    case 2:
                        algo = new aStar();
                        break;
                    case 3:
                        algo = new greedyBestFirst();
                        break;
                }
                if (algo != null) {
                    c_vals.current = algo;

                    algo.algorithm(Grid[currSD[0][0]][currSD[0][1]], Grid[currSD[1][0]][currSD[1][1]]);
                    algo.start();
                }

                saveButton.setDisable(true);
                drawButton.setDisable(true);
                loadButton.setDisable(true);
                sourceButton.setDisable(true);
                targetButton.setDisable(true);
                buttonStateChange(true);

                cancelButton.setDisable(false);
            }
        }
    }

    @FXML
    void clearBoardActionEvent(ActionEvent event) {
        System.out.println("CLEAR GRID");
        clearBoardPath(false);

        if (currSD[0][0] != -1) PaintBlock(currSD[0][0], currSD[0][1], c_vals.BORDER, c_vals.SRC);
        if (currSD[1][0] != -1) PaintBlock(currSD[1][0], currSD[1][1], c_vals.BORDER, c_vals.TGT);

        currSD[2][0] = -1; // remove the intermediate
    }

    @FXML
    void clearPathActionEvent(ActionEvent event) {
        System.out.println("CLEAR PATH ONLY");
        clearBoardPath(true);

        if (currSD[0][0] != -1) PaintBlock(currSD[0][0], currSD[0][1], c_vals.BORDER, c_vals.SRC);
        if (currSD[1][0] != -1) PaintBlock(currSD[1][0], currSD[1][1], c_vals.BORDER, c_vals.TGT);
        if (currSD[2][0] != -1) PaintBlock(currSD[2][0], currSD[2][1], c_vals.BORDER, c_vals.INTR);
    }

    @FXML
    void mazeActionEvent(ActionEvent event) {
        System.out.println("LOAD RANDOM MAZE");

        ArrayList<cell> wallAr = readWrite.loadRandomMaze();
        Collections.shuffle(wallAr);

        buttonStateChange(true);
        cancelButton.setDisable(true);
        clearButton.setDisable(true);

        Thread thread = new Thread(() -> {
            int wallPainted = 0;
            for (cell wall : wallAr) {
                try {
                    PaintBlock(wall.i, wall.j, c_vals.BORDER, c_vals.WALL);
                    Thread.sleep(c_vals.MZ);
                    wallPainted++;
                } catch (Exception ignored) {
                }
            }

            if (wallPainted < wallAr.size()) { // If any wall is left
                for (cell x : wallAr) {
                    PaintBlock(x.i, x.j, c_vals.BORDER, c_vals.WALL);
                }
            }
        });
        thread.start();

        buttonStateChange(false);
        cancelButton.setDisable(false);
        clearButton.setDisable(false);
    }

    @FXML
    void drawMazeActionEvent(ActionEvent event) {
        System.out.println("DRAW MAZE");
        buttonStateChange(true); // enable the other buttons
        applyColor = false;
        clearBoardPath(false);

        cancelButton.setDisable(false);
        saveButton.setDisable(false);
        drawButton.setDisable(true);
    }

    @FXML
    void saveMazeActionEvent(ActionEvent event) {
        String filePath = c_vals.MAZE + c_vals.SAVE;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Save Maze");

        Stage fileStage = new Stage();
        File file = fileChooser.showSaveDialog(fileStage);

        if (file != null) filePath = file.getPath();
        else {
            File f = new File(filePath);
            if (!f.exists()) return;
        }

        readWrite.save(filePath);
        cancelButton.setDisable(true);
        saveButton.setDisable(true);
        drawButton.setDisable(false);
    }

    @FXML
    void cancelMazeActionEvent(ActionEvent event) {
        if (c_vals.current != null) c_vals.current.killThread();
        c_vals.current = null;

        UpdateBorder(c_vals.WALL);
        applyColor = false;

        saveButton.setDisable(false);
        loadButton.setDisable(false);
        drawButton.setDisable(false);
        sourceButton.setDisable(false);
        targetButton.setDisable(false);
        buttonStateChange(false);

        cancelButton.setDisable(true);
    }

    @FXML
    void loadMazeActionEvent(ActionEvent event) {
        String filePath = c_vals.MAZE + c_vals.LOAD;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Load Maze");

        Stage fileStage = new Stage();
        File file = fileChooser.showOpenDialog(fileStage);

        if (file != null) filePath = file.getPath();
        else {
            File f = new File(filePath);
            if (!f.exists()) return;
        }

        ArrayList<cell> wallAr = readWrite.read(filePath);

        if (wallAr != null) { // There are wall
            Collections.shuffle(wallAr);

            Thread thread = new Thread(() -> {
                int wallPainted = 0;
                for (cell wall : wallAr) {
                    try {
                        PaintBlock(wall.i, wall.j, c_vals.BORDER, c_vals.WALL);
                        Thread.sleep(c_vals.MZ);
                        wallPainted++;
                    } catch (Exception ignored) {
                    }
                }

                if (wallPainted < wallAr.size()) { // If any wall is left
                    for (cell x : wallAr) {
                        PaintBlock(x.i, x.j, c_vals.BORDER, c_vals.WALL);
                    }
                }
            });
            thread.start();
        }
        saveButton.setDisable(false);
    }

    @FXML
    void wallActionEvent(ActionEvent event) {
        curState = cellState.WALL;
        applyColor = false;
    }

    @FXML
    void unvisitedActionEvent(ActionEvent event) {
        curState = cellState.UNVISITED;
        applyColor = false;
    }

    @FXML
    void intermediateActionEvent(ActionEvent event) {
        curState = cellState.INTERMEDIATE;
    }

    @FXML
    void sourceActionEvent(ActionEvent event) {
        curState = cellState.SOURCE;
    }

    @FXML
    void targetActionEvent(ActionEvent event) {
        curState = cellState.TARGET;
    }
}
