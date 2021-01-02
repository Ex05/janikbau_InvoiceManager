package nrw.janikbau.sfm.controller;
// <- Import ->

// <- Static_Import ->

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nrw.janikbau.sfm.Client;
import nrw.janikbau.sfm.model.SFM_Model;

public class AddClientDialogController{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	@FXML
	private Button buttonExit;

	@FXML
	private Button buttonOK;

	@FXML
	private TextField textFieldClientname;

	private Stage stage;

	private SFM_Model model;

	// <- Static ->
	// <- Constructor ->
	// <- Abstract ->

	// <- Object ->
	@FXML
	public void initialize(){
		textFieldClientname.setText("");

		buttonOK.setDisable(true);

		textFieldClientname.textProperty().addListener((object, oldValue, newValue) -> {
			this.textFieldClientname.setStyle("-fx-text-inner-color: #00cc00;");

			buttonOK.setDisable(false);

			for(final Client client : model.getClients()){
				if(client.getName().equals(newValue)){
					this.textFieldClientname.setStyle("-fx-text-inner-color: #cc0000;");

					buttonOK.setDisable(true);
				}
			}
		});

		buttonOK.requestFocus();
	}

	@FXML
	public void onButtonExitPressed(final ActionEvent e){
		textFieldClientname.setText("");

		stage.hide();
	}

	@FXML
	public void buttonOKPressed(final ActionEvent e){
		stage.hide();
	}

	// <- Getter & Setter ->
	public String getClientName(){
		final String clientName = textFieldClientname.getText();

		return !clientName.equals("") ? clientName : null;
	}

	public void setStage(final Stage stage){
		this.stage = stage;
	}

	public void setModel(final SFM_Model model){
		this.model = model;
	}
	// <- Static ->
}
