/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lcss.gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author nikolas
 */
public class GoogleMaps extends VBox {

    private static Stage stage = null;
    private Scene scene = null;
    private MyBrowser myBrowser = null;

    /**
     * Initialize the VBox for Google Maps
     */
    public GoogleMaps() {
        if (stage == null) {
            stage = new Stage();
            myBrowser = new MyBrowser();
            scene = new Scene(myBrowser, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Google Maps");
            stage.setResizable(false);
            stage.getIcons()
                    .add(new Image(
                                    GoogleMaps.class.getResourceAsStream("/resources/icons/logo.jpg")));
        }
    }

    /**
     * Shows the JavaFX frame and loads the new Content.
     */
    public void show() {
        myBrowser.loadURL();
        stage.show();
    }

    /**
     * Setter and Getters 
     */
    
    
    public MyBrowser getMyBrowser() {
        return myBrowser;
    }

    public void setMyBrowser(MyBrowser myBrowser) {
        this.myBrowser = myBrowser;
    }

}
