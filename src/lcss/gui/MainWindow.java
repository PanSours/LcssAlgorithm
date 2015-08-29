/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcss.gui;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author bob
 */
public class MainWindow extends Application {

    
    /**
     * Loads the fxml for the application and shows the javaFX frame 
     * @param stage
     * @throws IOException 
     */
    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource(
                "/resources/fxml/MainWindow.fxml"));
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Lcss Algorithm");
        stage.setResizable(false);
        stage.setScene(scene);
        stage
                .getIcons()
                .add(new Image(
                                MainWindow.class
                                .getResourceAsStream("/resources/icons/logo.jpg")));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
