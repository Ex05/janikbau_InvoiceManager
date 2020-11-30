package nrw.janikbau.sfm.util;
// <- Import ->

// <- Static_Import ->

/**
 * @author Jan.Marcel.Janik [©2016]
 */
public final class Constants {
    // <- Public ->
    public static final String FXML_FILE_LOCATION;
    public static final String CSS_FILE_LOCATION;
    public static final String STAGE_ICON_LOCATION;
    // <- Protected ->
    // <- Private->

    // <- Static ->
    static {
        FXML_FILE_LOCATION = "/res/fxml/sfm.fxml";
        CSS_FILE_LOCATION = "/res/css/sfm.css";
        STAGE_ICON_LOCATION = "./res/img/application_icon.png";
    }

    // <- Constructor ->
    private Constants(){
        throw new IllegalStateException("Do not instantiate !~!");
    }

    // <- Abstract ->
    // <- Object ->
    // <- Getter & Setter ->
    // <- Static ->
}
