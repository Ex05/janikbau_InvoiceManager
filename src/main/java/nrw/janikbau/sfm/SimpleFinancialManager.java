package nrw.janikbau.sfm;
// <- Import ->

// <- Static_Import ->

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.PropertyResourceBundle;

import static nrw.janikbau.sfm.util.Constants.*;
import static nrw.janikbau.sfm.util.Resources.IMAGE_STAGE_ICON;

public class SimpleFinancialManager extends Application{
    // <- Public ->
    // <- Protected ->
    // <- Private->
    // <- Static ->
    // <- Constructor ->
    // <- Abstract ->

    // <- Object ->
    @Override
    public void start(final Stage primaryStage) throws Exception{
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FXML_FILE_LOCATION));
        loader.setResources(new PropertyResourceBundle(new FileInputStream("res/lang/en_US.properties")));

        final Parent root = loader.load();

        final Scene scene = new Scene(root, 1920, 1080);
        scene.getStylesheets().add(CSS_FILE_LOCATION);

        primaryStage.setTitle("Simple Financial Manager [v0.1.0-indev]");
        primaryStage.setScene(scene);

        IMAGE_STAGE_ICON = new Image(new FileInputStream(new File(STAGE_ICON_LOCATION)));

        primaryStage.getIcons().add(IMAGE_STAGE_ICON);

        primaryStage.show();
    }

    // <- Getter & Setter ->
    // <- Static ->
}
