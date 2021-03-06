import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    private static int PAGE_NUM = 1;
    private Crawler crawler;
    private Scene scene1, scene2;
    private String urls;
    private Text urlsText;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.crawler = new Crawler();
        primaryStage.setTitle("UofT Course Search");

        //Layout 1

        // User's search specifications
        TextField keywordInput = new TextField();
        Label keywordLabel = new Label("Search for keyword: ");
        keywordLabel.setLabelFor(keywordInput);

        TextField breadthInput = new TextField();
        Label breadthLabel = new Label("Search for breadth classes: ");
        breadthLabel.setLabelFor(breadthInput);

        TextField levelInput = new TextField();
        Label levelLabel = new Label("Search for level classes: ");
        levelLabel.setLabelFor(levelInput);

        TextField lowInput = new TextField();
        Label lowLabel = new Label("Starting page number: ");
        lowInput.setText("1");
        lowLabel.setLabelFor(lowInput);

        TextField highInput = new TextField();
        Label highLabel = new Label("Ending page number: ");
        highInput.setText("100");
        highLabel.setLabelFor(highInput);

        Button searchButton = new Button();
        searchButton.setText("Search!");
        searchButton.setOnAction(e -> {
            // when the search button is clicked, the crawler calls the findCourses method
            String URL = this.getStartingURL(lowInput.getText());
            if (this.checkURL(URL)) {
                this.crawler.findCourses(URL, keywordInput.getText(), breadthInput.getText(), levelInput.getText(), Integer.parseInt(lowInput.getText()) ,Integer.parseInt(highInput.getText()), true);
                this.urls = this.crawler.getUrls();
                primaryStage.setScene(scene2);
                this.urlsText.setText(this.urls);
            }
            else {
                Alert.display("Error!", "Entered URL format is incorrect");
            }
        });

        FlowPane container = new FlowPane();
        container.getChildren().addAll(breadthInput, keywordInput, searchButton);
        VBox layout1 = new VBox(10);
        layout1.setPadding(new Insets(20, 20, 20, 20));
        layout1.getChildren().addAll(keywordLabel, keywordInput, breadthLabel, breadthInput, levelLabel, levelInput, lowLabel, lowInput, highLabel, highInput, searchButton);
        scene1 = new Scene(layout1, 500, 500);

        // Layout 2

        this.urlsText = new Text(10, 50, this.urls);
        this.urlsText.setFont(new Font(20));
        Label urlsLabel = new Label("URLs found: ");
        urlsLabel.setLabelFor(urlsText);
        Button backButton = new Button();
        backButton.setText("Back");
        backButton.setOnAction(e -> primaryStage.setScene(scene1));
        Button serializeButton = new Button();
        serializeButton.setText("Save as .txt File");
        serializeButton.setOnAction(e -> {
                crawler.save("page" + PAGE_NUM);
                Alert.display("Success!", "Urls Saved Successfully!");
        });

        VBox layout2 = new VBox(10);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(urlsText);
        layout2.setPadding(new Insets(20, 20, 20, 20));
        layout2.getChildren().addAll(urlsLabel, urlsText, scrollPane, backButton, serializeButton);
        scene2 = new Scene(layout2, 1000, 500);

        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    private boolean checkURL(String input) {
        Pattern pattern = Pattern.compile("((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private String getStartingURL(String start) {
        if (Integer.parseInt(start) == 1) {
            return "https://fas.calendar.utoronto.ca/search-courses";
        }
        else {
            int pageNum = Integer.parseInt(start) - 1;
            return "https://fas.calendar.utoronto.ca/search-courses" + "?page=" + pageNum;
        }
    }
}
