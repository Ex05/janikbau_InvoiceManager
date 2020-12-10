package nrw.janikbau.sfm.util;
// <- Import ->

// <- Static_Import ->

import java.util.Locale;

/**
 * @author Jan.Marcel.Janik [©2016]
 */
public final class Constants {
    // <- Public ->
    public static final String FXML_FILE_LOCATION;

    public static final String CSS_FILE_LOCATION;

    public static final String MAIN_VIEW_FXML_FILE_NAME;

    public static final String DIALOG_CHOOSE_INVOICE_SAVE_LOCATION_FXML_FILE_NAME;

    public static final String STAGE_ICON_LOCATION;

    public static final String LANGUAGE_FILE_LOCATION;

    public static final String PROPERTY_FILE_EXTENSION;

    public static final String SETTINGS_FILE_LOCATION;

    public static final String SETTINGS_FILE_NAME;

    public static final String SETTINGS_FILE_PATH;

    public static final String SETTING_INVOICE_SAVE_LOCATION_NAME;

    public static final String DIRECTORY_CHOOSER_INVOICE_LOCATION_TITLE;

    public static final String DIALOG_CHOOSE_INVOICE_LOCATION_TITLE;

    public static final Locale LANGUAGE_EN_US;
    public static final Locale LANGUAGE_DE_DE;

    // <- Protected ->
    // <- Private->

    // <- Static ->
    static {
        FXML_FILE_LOCATION = "/res/fxml/";

        MAIN_VIEW_FXML_FILE_NAME = "sfm.fxml";

        DIALOG_CHOOSE_INVOICE_SAVE_LOCATION_FXML_FILE_NAME = "chooseInvoiceSaveLocationDialog.fxml";

        CSS_FILE_LOCATION = "/res/css/sfm.css";

        STAGE_ICON_LOCATION = "./res/img/application_icon.png";

        LANGUAGE_FILE_LOCATION = "res/lang/";

        PROPERTY_FILE_EXTENSION = ".properties";

        SETTINGS_FILE_LOCATION = "/res/";

        SETTINGS_FILE_NAME = "settings";

        SETTINGS_FILE_PATH = "." + SETTINGS_FILE_LOCATION + SETTINGS_FILE_NAME + PROPERTY_FILE_EXTENSION;

        SETTING_INVOICE_SAVE_LOCATION_NAME = "invoiceSaveLocation";

        DIRECTORY_CHOOSER_INVOICE_LOCATION_TITLE = "directoryChooserInvoiceLocationTitle";

        DIALOG_CHOOSE_INVOICE_LOCATION_TITLE = "dialogChooseInvoiceSaveLocationTitle";

        LANGUAGE_EN_US = new Locale("en", "US");
        LANGUAGE_DE_DE = new Locale("de", "DE");
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
