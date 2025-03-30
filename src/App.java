import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.BaseController;
import main.player.PlayerStorage;

public class App extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Application icon
        InputStream stream = getClass().getResourceAsStream("/main/resources/icon.png");
        if (stream != null) {
            Image icon = new Image(stream);
            primaryStage.getIcons().add(icon);
        } else {
            System.out.println("Resource not found!");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/Base.fxml"));
        Parent root = loader.load();
        root.requestFocus();

        BaseController baseController = loader.getController();
        //baseController.passValues();

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("TabletopToolkit - by Arkadya");
        primaryStage.show();
    }
}
