package nrw.janikbau.sfm.controller;
// <- Import ->

// <- Static_Import ->

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SFM_Controller{
    // <- Public ->
    // <- Protected ->

    // <- Private->
    @FXML
    private Button buttonSearch;

    @FXML
    private Label labelLogLine;

    @FXML
    private Label labelPreviousYear;

    @FXML
    private Label labelCurrentYear;

    @FXML
    private Label labelNextYear;

    @FXML
    private TextField textFieldSearchBar;

    private String invoiceSaveLocation;

    // <- Static ->
    // <- Constructor ->
    // <- Abstract ->

    // <- Object ->
    public void log(final String s){
        labelLogLine.setText(s);
    }

    public void clearLog(){
        labelLogLine.setText("");
    }

    @FXML
    public void initialize(){
        labelPreviousYear.setText("2019");
        labelCurrentYear.setText("2020");
        labelNextYear.setText("2021");
    }

    @FXML
    void onButtonSearchPressed(final ActionEvent e){
        log("Searching...");

        // TODO: Search. (Jan - 2020.12.01)

        log("No Entries Found...");

        textFieldSearchBar.setText("");
    }

    public void afterStartup(){
        buttonSearch.requestFocus();
    }

    public void setInvoiceSaveLocation(final String invoiceSaveLocation){
        this.invoiceSaveLocation = invoiceSaveLocation;
    }

    // <- Getter & Setter ->
    // <- Static ->
}

