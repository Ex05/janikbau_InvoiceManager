package nrw.janikbau.sfm.controller;
// <- Import ->

// <- Static_Import ->

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nrw.janikbau.sfm.Client;
import nrw.janikbau.sfm.JobSite;

public class AddJobSiteDialogController{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	@FXML
	private Button buttonExit;

	@FXML
	private Button buttonOK;

	@FXML
	private TextField textFieldJobSiteDescription;

	private Stage stage;

	private Client client;

	// <- Static ->
	// <- Constructor ->
	// <- Abstract ->

	// <- Object ->
	@FXML
	public void initialize(){
		textFieldJobSiteDescription.setText("");

		buttonOK.setDisable(true);

		textFieldJobSiteDescription.textProperty().addListener((object, oldValue, newValue) -> {
			this.textFieldJobSiteDescription.setStyle("-fx-text-inner-color: #00cc00;");

			buttonOK.setDisable(false);

			for(final JobSite jobSite : client.getJobSites()){
				if(jobSite.getDescription().equals(newValue)){
					this.textFieldJobSiteDescription.setStyle("-fx-text-inner-color: #cc0000;");

					buttonOK.setDisable(true);
				}
			}
		});

		buttonOK.requestFocus();
	}

	@FXML
	public void onEnterPressed(final ActionEvent e){
		stage.hide();
	}

	@FXML
	public void onButtonExitPressed(final ActionEvent e){
		textFieldJobSiteDescription.setText("");

		stage.hide();
	}

	@FXML
	public void buttonOKPressed(final ActionEvent e){
		stage.hide();
	}

	// <- Getter & Setter ->
	public String getJobSiteDescription(){
		final String description = textFieldJobSiteDescription.getText();

		return !description.equals("") ? description : null;
	}

	public void setStage(final Stage stage){
		this.stage = stage;
	}

	public void setClient(final Client client){
		this.client = client;
	}

	// <- Static ->
}
