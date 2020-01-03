package com.nicolas_abroad.epub_scraper_desktop.gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import com.nicolas_abroad.epub_scraper_desktop.ebook.Story;
import com.nicolas_abroad.epub_scraper_desktop.ebook.Volume;
import com.nicolas_abroad.epub_scraper_desktop.format.EbookFormat;
import com.nicolas_abroad.epub_scraper_desktop.format.EpubFormat;
import com.nicolas_abroad.epub_scraper_desktop.input.InputChecking;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.EbookScraper;
import com.nicolas_abroad.epub_scraper_desktop.scrape.sources.SyosetsuScraper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private static final String INCORRECT_INPUT_MSG = "Please input a correct url.";

    private static final String SCRAPING_MSG = "Please wait a moment while the app is scraping.";

    private static final String FINISHED_MSG = "The app has finished scraping.";

    private static final int WINDOW_WIDTH = 500;

    private static final int WINDOW_HEIGHT = 200;

    private static final int HORIZONTAL_GAP = 10;

    private static final int VERTICAL_GAP = 10;

    private static final int TOP_OFFSET = 25;

    private static final int RIGHT_OFFSET = 25;

    private static final int BOTTOM_OFFSET = 25;

    private static final int LEFT_OFFSET = 25;

    private static final int TEXT_WIDTH = 30;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(STAGE_TITLE);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(HORIZONTAL_GAP);
        grid.setVgap(VERTICAL_GAP);
        grid.setPadding(new Insets(TOP_OFFSET, RIGHT_OFFSET, BOTTOM_OFFSET, LEFT_OFFSET));

        generateTitle(grid);
        generateUrlLabel(grid);
        TextField inputTextField = generateUrlTextBox(grid);
        generateScrapeButton(grid, inputTextField);
        generateTextMessage(grid);

        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * Launch program.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Generate title element to be added to the window grid.
     * @param grid
     * @return title
     */
    public static Text generateTitle(GridPane grid) {
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
    public static Label generateUrlLabel(GridPane grid) {
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
    public static TextField generateUrlTextBox(GridPane grid) {
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
    public static Button generateScrapeButton(GridPane grid, TextField textField) {
        Button btn = new Button(SCRAPE_BUTTON_LABEL);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 2);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (InputChecking.isIncorrectUrl(textField.getText())) {
                        // inputted index url is incorrect
                        modifyTextMessage(grid, INCORRECT_INPUT_MSG);
                        return;
                    }
                    modifyTextMessage(grid, SCRAPING_MSG);
                    executeScraping(textField);
                    modifyTextMessage(grid, FINISHED_MSG);
                } catch (IOException e) {
                    try {
                        PrintWriter pw = new PrintWriter(LocalDateTime.now().toString() + ".txt");
                        e.printStackTrace(pw);
                        pw.close();
                    } catch (Exception e1) {
                    }
                }
            }
        });

        return btn;
    }

    /**
     * Generate text message panel to be added to the window grid.
     * @param grid
     */
    public static void generateTextMessage(GridPane grid) {
        Text text = new Text();
        grid.add(text, 1, 3);
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

    private static Node getNodeFromGridPane(GridPane grid, int col, int row) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Execute scraping related functions.
     * @param textField
     * @throws IOException
     */
    public static void executeScraping(TextField textField) throws IOException {
        EbookScraper scraper = new SyosetsuScraper();
        String url = textField.getText();

        // Scrape all data from url
        Story story = new Story(scraper, url);
        story.generate();

        // Generate volumes from scrapped data
        EbookFormat ebookFormat = new EpubFormat();
        for (Volume volume : story.getVolumes()) {
            ebookFormat.generate(volume);
        }
    }

}
