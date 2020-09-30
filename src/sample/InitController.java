package sample;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.basics.c_vals;
import javafx.scene.image.Image;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class InitController implements Initializable {
    @FXML
    private JFXButton previousButton, nextButton, skipButton;
    @FXML private AnchorPane page0, page1, page2, page3, page4, page5, page6, page7;
    @FXML private Label github;

    private int prevPage = -1, currPage = 0;
    private AnchorPane anchorPage[];

    private static final int PAGE_WIDTH = 400;
    private static final int TOTAL_PAGE = 8;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        anchorPage = new AnchorPane[]{page0, page1, page2, page3, page4, page5, page6, page7};

        // Set the default possible of the pages
        page0.setLayoutX(0);
        for(int i = 1; i < TOTAL_PAGE; i++) anchorPage[i].setLayoutX(-PAGE_WIDTH);

        nextButton.setOnAction(event -> {
            if(currPage < TOTAL_PAGE -1){
                prevPage = currPage;
                currPage++;

                updateNextButton();
                updatePage();
            }
            else {
                OpenApplication();
            }
        });

        previousButton.setOnAction(event -> {
            if(currPage > 0) {
                prevPage = currPage;
                currPage--;

                updateNextButton();
                updatePage();
            }
        });

        skipButton.setOnAction(event -> OpenApplication());

        github.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI(c_vals.GITHUB));
            } catch (Exception ignored) {}
        });
    }

    private void updatePage()
    {
        anchorPage[prevPage].setLayoutX(-PAGE_WIDTH);
        anchorPage[currPage].setLayoutX(0);
    }

    private void updateNextButton()
    {
        if(currPage == TOTAL_PAGE-1){
            nextButton.setText("Finish");
        }
        else {
            nextButton.setText("Next");
        }
    }

    private void OpenApplication()
    {
        Stage stage = (Stage) skipButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Stage primaryStage = new Stage();

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("PathFinder Analyzer");
            primaryStage.getIcons().add(new Image("/images/logo.png"));
            primaryStage.show();

        }
        catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
