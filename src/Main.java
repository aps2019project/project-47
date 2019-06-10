import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("layouts/mainPageLayout.fxml"));
        root.getStylesheets().add("resources/stylesheets/styles.css");
        root.getStyleClass().add("mainPage");
        Image img = new Image(getClass().getResourceAsStream("resources/cursor.png"));
        ImageCursor cursor = new ImageCursor(img, 30, 30);
        root.setCursor(cursor);
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("DUELYST");
        primaryStage.setScene(new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
