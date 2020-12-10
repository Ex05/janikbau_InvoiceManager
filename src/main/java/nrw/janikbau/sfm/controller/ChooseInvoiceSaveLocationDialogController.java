package nrw.janikbau.sfm.controller;
// <- Import ->

// <- Static_Import ->

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

import static nrw.janikbau.sfm.util.Constants.DIRECTORY_CHOOSER_INVOICE_LOCATION_TITLE;
import static nrw.janikbau.sfm.util.Resources.LANGUAGE_FILE;

public class ChooseInvoiceSaveLocationDialogController{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final SimpleStringProperty invoiceLocation;

	@FXML
	private Button buttonExit;

	@FXML
	private Button buttonOK;

	@FXML
	private Button buttonOpenDirectoryChooser;

	@FXML
	private TextField textFieldInvoiceLocation;

	private Stage stage;

	// <- Static ->

	// <- Constructor ->

	public ChooseInvoiceSaveLocationDialogController(){
		invoiceLocation = new SimpleStringProperty(null);
	}

	// <- Abstract ->

	// <- Object ->
	@FXML
	public void initialize(){
		buttonOK.setDisable(true);

		invoiceLocation.bind(textFieldInvoiceLocation.textProperty());

		textFieldInvoiceLocation.setDisable(true);
	}

	@FXML
	public void onButtonExitPressed(final ActionEvent e){
		stage.hide();
	}

	@FXML
	public void buttonOKPressed(final ActionEvent e){
		stage.hide();
	}

	@FXML
	public void onButtonOpenDirectoryChooserPressed(final ActionEvent e){
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		directoryChooser.setTitle(LANGUAGE_FILE.getString(DIRECTORY_CHOOSER_INVOICE_LOCATION_TITLE));

		final File chosenDir = directoryChooser.showDialog(null);

		if (chosenDir != null){
			textFieldInvoiceLocation.setText(chosenDir.getAbsolutePath());

			buttonOK.setDisable(false);
		}
	}

	// <- Getter & Setter ->
	public void setStage(final Stage stage){
		this.stage = stage;
	}

	public String getInvoiceLocation(){
		return !invoiceLocation.get().equals("") ? invoiceLocation.get() : null;
	}

	public SimpleStringProperty invoiceLocationProperty(){
		return invoiceLocation;
	}

	// <- Static ->
}
