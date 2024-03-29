package com.nicolas_abroad.epub_scraper_desktop.user_interface;

import com.nicolas_abroad.epub_scraper_desktop.input.InputParser;
import com.nicolas_abroad.epub_scraper_desktop.scrape.ScrapeExecutor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI for ebook scrapping app.
 * @author Nicolas
 */
public class MainWindow extends Application {

    private static final String STAGE_TITLE = "Ebook scraper";

    private static final String SCRAPE_BUTTON_LABEL = "Scrape";

    private static final class MSG {
        private static final String INCORRECT_INPUT = "Please input a correct url.";
        private static final String SCRAPING = "Please wait a moment while the app is scraping.";
        private static final String ERROR = "An error occured while scraping.";
        private static final String FINISHED = "The app has finished scraping.";
    }

    private static final class WINDOW_SIZE {
        private static final int WIDTH = 500;
        private static final int HEIGHT = 200;
    }

    private static final class GAP {
        private static final int HORIZONTAL = 10;
        private static final int VERTICAL = 10;
    }

    private static final class OFFSET {
        private static final int TOP = 25;
        private static final int RIGHT = 25;
        private static final int BOTTOM = 25;
        private static final int LEFT = 25;
    }

    private static final int TEXT_WIDTH = 30;

    /**
     * Launch program.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    // -------------------------------
    // Update GUI
    // -------------------------------

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(STAGE_TITLE);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(GAP.HORIZONTAL);
        grid.setVgap(GAP.VERTICAL);
        grid.setPadding(new Insets(OFFSET.TOP, OFFSET.RIGHT, OFFSET.BOTTOM, OFFSET.LEFT));

        generateTitle(grid);
        generateUrlLabel(grid);
        TextField inputTextField = generateUrlTextBox(grid);
        generateScrapeButton(grid, inputTextField);
        generateTextMessage(grid);

        Scene scene = new Scene(grid, WINDOW_SIZE.WIDTH, WINDOW_SIZE.HEIGHT);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * Generate title element to be added to the window grid.
     * @param grid
     * @return title
     */
    private static Text generateTitle(GridPane grid) {
        String TITLE = STAGE_TITLE;
        Text scenetitle = new Text(TITLE);
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        return scenetitle;
    }

    /**
     * Generate url label text element to be added to the window grid.
     * @param grid
     * @return url label text
     */
    private static Label generateUrlLabel(GridPane grid) {
        String TEXT_BOX_LABEL = "URL:";
        Label userName = new Label(TEXT_BOX_LABEL);
        grid.add(userName, 0, 1);
        return userName;
    }

    /**
     * Generate url input text box to be added to the window grid.
     * @param grid
     * @return url input text box
     */
    private static TextField generateUrlTextBox(GridPane grid) {
        TextField userTextField = new TextField();
        userTextField.setPrefColumnCount(TEXT_WIDTH);
        grid.add(userTextField, 1, 1);
        return userTextField;
    }

    /**
     * Generate scraping button to be added to the window grid.
     * @param grid
     * @param textField
     * @return scraping button
     */
    private static Button generateScrapeButton(GridPane grid, TextField textField) {
        Button btn = new Button(SCRAPE_BUTTON_LABEL);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 2);
        btn.setOnAction(event -> setScrapeButtonAction(grid, textField));
        return btn;
    }

    /**
     * Generate text message panel to be added to the window grid.
     * @param grid
     */
    private static void generateTextMessage(GridPane grid) {
        Text text = new Text();
        grid.add(text, 1, 3);
    }

    // -------------------------------
    // Button actions
    // -------------------------------

    /**
     * Set scrape button action.
     * @param grid
     * @param textField
     */
    private static void setScrapeButtonAction(GridPane grid, TextField textField) {
        String url = textField.getText();
        InputParser inputParser = InputParser.getInputParser();
        inputParser.processUrl(url);

        // Url check
        if (!inputParser.isValidUrl()) {
            // inputted index url is incorrect
            updateMessage(grid, MSG.INCORRECT_INPUT);
            return;
        }

        updateMessage(grid, MSG.SCRAPING);

        Thread thread = new Thread(() -> {
            boolean executedCorrectly = ScrapeExecutor.executeScraping(textField.getText());
            if (!executedCorrectly) {
                modifyTextMessage(grid, MSG.ERROR);
                return;
            }

            updateMessage(grid, MSG.FINISHED);
        });
        thread.setDaemon(true);
        thread.start();
    }

    // -------------------------------
    // Scrape button methods
    // -------------------------------

    private static void updateMessage(GridPane grid, String textMessage) {
        Thread thread = new Thread(() -> {
            Runnable updater = () -> modifyTextMessage(grid, textMessage);
            Platform.runLater(updater);
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Modify text message panel in the window grid.
     * @param grid
     * @param textMessage
     */
    public static void modifyTextMessage(GridPane grid, String textMessage) {
        Text text = (Text) getNodeFromGridPane(grid, 1, 3);
        text.setText(textMessage);
    }

    /**
     * Get node from grid pane.
     * @param grid
     * @param col
     * @param row
     * @return
     */
    private static Node getNodeFromGridPane(GridPane grid, int col, int row) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

}
