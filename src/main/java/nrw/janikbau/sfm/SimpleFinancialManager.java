package nrw.janikbau.sfm;
// <- Import ->

// <- Static_Import ->

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nrw.janikbau.sfm.controller.ChooseInvoiceSaveLocationDialogController;
import nrw.janikbau.sfm.controller.SFM_Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import static javafx.stage.StageStyle.UTILITY;
import static nrw.janikbau.sfm.util.Constants.*;
import static nrw.janikbau.sfm.util.Resources.IMAGE_STAGE_ICON;
import static nrw.janikbau.sfm.util.Resources.LANGUAGE_FILE;

public class SimpleFinancialManager extends Application{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final Properties settings;

	private final SimpleStringProperty invoiceSaveLocation;

	// <- Static ->

	// <- Constructor ->

	public SimpleFinancialManager(){
		settings = new Properties();

		invoiceSaveLocation = new SimpleStringProperty();

		try{
			final File settingsFile = new File(SETTINGS_FILE_PATH);

			if (!settingsFile.exists()){
				if (!settingsFile.createNewFile()){
					new IOException("Failed to create settings file: '" + SETTINGS_FILE_PATH + "'.").printStackTrace();
				}
			}

			settings.load(new FileInputStream(settingsFile));

			final String invoiceSaveLocation = settings.getProperty(SETTING_INVOICE_SAVE_LOCATION_NAME);

			if (invoiceSaveLocation == null){
				settings.setProperty(SETTING_INVOICE_SAVE_LOCATION_NAME, "");
			}else if (!invoiceSaveLocation.equals("")){
				this.invoiceSaveLocation.set(invoiceSaveLocation);
			}
		} catch (final IOException e){
			e.printStackTrace();
		}
	}

	// <- Abstract ->

	// <- Object ->
	@Override
	public void init() throws Exception{
		LANGUAGE_FILE = new PropertyResourceBundle(new FileInputStream(LANGUAGE_FILE_LOCATION + LANGUAGE_EN_US.toString() + PROPERTY_FILE_EXTENSION));
	}

	@Override
	public void start(final Stage primaryStage) throws Exception{
		checkAndSetInvoiceLocation();

		final FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(FXML_FILE_LOCATION + MAIN_VIEW_FXML_FILE_NAME));
		loader.setResources(LANGUAGE_FILE);

		final Parent root = loader.load();
		root.getStylesheets().add(CSS_FILE_LOCATION);

		final Scene scene = new Scene(root, 1920, 1080);
		scene.getStylesheets().add(CSS_FILE_LOCATION);

		primaryStage.setTitle("Simple Financial Manager [v0.1.0-indev]");
		primaryStage.setScene(scene);

		IMAGE_STAGE_ICON = new Image(new FileInputStream(new File(STAGE_ICON_LOCATION)));

		primaryStage.getIcons().add(IMAGE_STAGE_ICON);

		primaryStage.show();

		final SFM_Controller controller = loader.getController();
		controller.setInvoiceSaveLocation(invoiceSaveLocation.get());
		controller.afterStartup();
	}

	private void checkAndSetInvoiceLocation(){
		if (invoiceSaveLocation.isEmpty().get()){
			try{
				final FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource(FXML_FILE_LOCATION + DIALOG_CHOOSE_INVOICE_SAVE_LOCATION_FXML_FILE_NAME));
				loader.setResources(LANGUAGE_FILE);

				final Parent root = loader.load();
				root.getStylesheets().add(CSS_FILE_LOCATION);

				final Scene scene = new Scene(root, 480, 240);
				scene.getStylesheets().add(CSS_FILE_LOCATION);

				final Stage stage = new Stage(UTILITY);
				stage.setTitle(LANGUAGE_FILE.getString(DIALOG_CHOOSE_INVOICE_LOCATION_TITLE));

				stage.setScene(scene);

				final ChooseInvoiceSaveLocationDialogController controller = loader.getController();
				controller.setStage(stage);

				stage.showAndWait();

				final String invoiceSaveLocation = controller.getInvoiceLocation();

				if (invoiceSaveLocation == null){
					System.exit(0);
				}

				this.invoiceSaveLocation.set(invoiceSaveLocation);

				settings.setProperty(SETTING_INVOICE_SAVE_LOCATION_NAME, invoiceSaveLocation);

				settings.store(new FileOutputStream(SETTINGS_FILE_PATH), null);

			} catch (final IOException e){
				e.printStackTrace();
			}
		}
	}

	// <- Getter & Setter ->
	public String getInvoiceSaveLocation(){
		return invoiceSaveLocation.get();
	}

	public SimpleStringProperty invoiceSaveLocationProperty(){
		return invoiceSaveLocation;
	}

	public void checkAndSetInvoiceLocation(final String invoiceLocation){
		this.invoiceSaveLocation.set(invoiceLocation);
	}

	// <- Static ->
}
	/*
    root
	    - Foo_Bar
			- 2020_12_01
				- hash
				- invoice.xml
			- 2020_12_02
				- hash
				- invoice.xml
		- Bar_Bar
		    - 2019_06_28
		        - hash
		        - invoice.xml
	 */