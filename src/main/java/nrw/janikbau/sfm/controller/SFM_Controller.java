package nrw.janikbau.sfm.controller;
// <- Import ->

// <- Static_Import ->

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import nrw.janikbau.sfm.Client;
import nrw.janikbau.sfm.Invoice;
import nrw.janikbau.sfm.JobSite;
import nrw.janikbau.sfm.model.SFM_Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.min;
import static java.nio.file.Files.createDirectory;
import static java.util.Arrays.sort;
import static java.util.Objects.requireNonNull;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.input.TransferMode.COPY_OR_MOVE;
import static javafx.scene.paint.Color.DARKCYAN;
import static javafx.scene.paint.Color.DARKRED;
import static javafx.stage.StageStyle.UTILITY;
import static nrw.janikbau.sfm.util.Constants.*;
import static nrw.janikbau.sfm.util.Resources.LANGUAGE_FILE;
import static nrw.janikbau.sfm.util.Util.*;

public class SFM_Controller{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final SFM_Model model;

	private final List<Client> currentlyDisplayedClients;

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

	@FXML
	private VBox vBoxClientList;

	@FXML
	private Label labelAddClient;

	@FXML
	private Label labelClearSearch;

	@FXML
	private HBox hBoxSearchClients;

	@FXML
	private HBox hBoxAddClient;

	@FXML
	private VBox vBoxInvoices;

	@FXML
	private Pane paneDropTarget;

	private HBox hBoxSelectedClient;

	@FXML
	private HBox hBoxDropTarget;

	private Label labelRemoveClient;
	private Label labelAddJobSite;

	private String invoiceSaveLocation;

	private VBox vBox;

	// <- Static ->

	// <- Constructor ->
	public SFM_Controller(){
		model = new SFM_Model();

		currentlyDisplayedClients = new LinkedList<>();

		hBoxSelectedClient = new HBox();

		labelRemoveClient = new Label("[]");

		labelAddJobSite = new Label("+");
		labelAddJobSite.setFont(Font.font(labelAddJobSite.getFont().getSize() * 2));
		labelAddJobSite.setOnMouseClicked(this::onLabelAddJobSiteClicked);
	}

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
		final int currentYear = LocalDate.now().getYear();

		labelPreviousYear.setText((currentYear - 1) + "");
		labelCurrentYear.setText(currentYear + "");
		labelNextYear.setText((currentYear + 1) + "");

		labelClearSearch = new Label("x");
		labelClearSearch.setOnMouseClicked(event -> {
			textFieldSearchBar.setText("");
		});

		textFieldSearchBar.textProperty().addListener((object, oldValue, newValue) -> {
			if(newValue.equals("")){
				hBoxSearchClients.getChildren().remove(labelClearSearch);

				model.getClients().forEach(this::addClientToUI);

				return;
			}

			if(!hBoxSearchClients.getChildren().contains(labelClearSearch)){
				hBoxSearchClients.getChildren().add(labelClearSearch);
			}

			clearClientListUI();

			for(final Client client : model.getClients()){
				if(client.getName().toLowerCase().contains(newValue.toLowerCase())){
					addClientToUI(client);
				}
			}
		});

		hBoxDropTarget.setOnDragOver(dragEvent -> {
			if(dragEvent.getGestureSource() != hBoxDropTarget && dragEvent.getDragboard().hasFiles()){
				dragEvent.acceptTransferModes(COPY_OR_MOVE);
			}

			dragEvent.consume();
		});

		hBoxDropTarget.setOnDragDropped(dragEvent -> {
			final Dragboard dragboard = dragEvent.getDragboard();

			if(dragboard.hasFiles()){
				final JobSite jobSite = model.getSelectedJobSite();

				final List<File> files = dragboard.getFiles();

				if(files.size() != 1){
					dragEvent.setDropCompleted(false);
				}else{
					try{
						final File invoiceFile = files.get(0);

						final BufferedReader br = new BufferedReader(new FileReader(invoiceFile));

						final byte[] hash = HexStringToByteArray(br.readLine());

						br.close();

						final Invoice invoice = new Invoice(invoiceFile.getAbsolutePath(), LocalDateTime.now(), hash, jobSite.getInvoice());

						jobSite.setInvoice(invoice);
					}catch(final IOException e){
						e.printStackTrace();
					}

					dragEvent.setDropCompleted(true);
				}
			}else{
				dragEvent.setDropCompleted(false);
			}

			dragEvent.consume();
		});
	}

	private void clearClientListUI(){
		vBoxClientList.getChildren().clear();

		currentlyDisplayedClients.clear();

		vBoxClientList.getChildren().add(hBoxAddClient);
	}

	private void checkInvoiceIntegrity(){
		Invoice invoice = model.getSelectedJobSite().getInvoice();

		while(invoice != null){
			try{
				final Invoice previous = invoice.getPrevious();

				final byte[] previousHash = previous == null ? new byte[]{} : previous.getHash();

				final byte[] data = Files.readAllBytes(Path.of(invoice.getFileLocation()));
				final byte[] hashData = new byte[data.length + previousHash.length];

				System.arraycopy(previousHash, 0, hashData, 0, previousHash.length);
				System.arraycopy(data, 0, hashData, previousHash.length, data.length);

				final byte[] hash = Hash(hashData);

				invoice.setValid(Arrays.equals(hash, invoice.getHash()));

				System.out.println(HashToString(hash));
				System.out.println(HashToString(invoice.getHash()));
				System.out.println();

			}catch(final NoSuchAlgorithmException | IOException e){
				e.printStackTrace();
			}

			invoice = invoice.getPrevious();
		}
	}

	void onLabelAddJobSiteClicked(final MouseEvent event){
		try{
			final FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(FXML_FILE_LOCATION + DIALOG_ADD_JOB_SITE_FXML_FILE_NAME));
			loader.setResources(LANGUAGE_FILE);

			final Parent root = loader.load();
			root.getStylesheets().add(CSS_FILE_LOCATION);

			final Scene scene = new Scene(root, 480, 240);
			scene.getStylesheets().add(CSS_FILE_LOCATION);

			final Stage stage = new Stage(UTILITY);
			stage.setTitle(LANGUAGE_FILE.getString(DIALOG_ADD_JOB_SITE_TITLE));

			stage.setScene(scene);

			final AddJobSiteDialogController controller = loader.getController();
			controller.setClient(model.getSelectedClient());
			controller.setStage(stage);

			stage.showAndWait();

			final String jobSiteDescription = controller.getJobSiteDescription();

			if(jobSiteDescription != null){
				final JobSite jobSite = new JobSite(jobSiteDescription);

				addJobSiteToUI(jobSite);
				writeJobSiteToDisk(model.getSelectedClient(), jobSite);

				model.getSelectedClient().addJobSite(jobSite);
			}
		}catch(final Exception exception){
			exception.printStackTrace();
		}
	}

	void addJobSiteToUI(final JobSite jobSite){
		final Label labelJobSite = new Label(jobSite.getDescription());
		labelJobSite.setTextFill(DARKCYAN);
		labelJobSite.setFont(Font.font(labelJobSite.getFont().getFamily(), labelJobSite.getFont().getSize() * 1.25));

		if(jobSite.getInvoice() != null){
			labelJobSite.setTextFill(DARKCYAN);
		}else{
			labelJobSite.setTextFill(DARKRED);
		}

		vBox.getChildren().add(labelJobSite);
	}

	@FXML
	void onLabelAddClientClicked(final MouseEvent event){
		try{
			final FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(FXML_FILE_LOCATION + DIALOG_ADD_CLIENT_FXML_FILE_NAME));
			loader.setResources(LANGUAGE_FILE);

			final Parent root = loader.load();
			root.getStylesheets().add(CSS_FILE_LOCATION);

			final Scene scene = new Scene(root, 480, 240);
			scene.getStylesheets().add(CSS_FILE_LOCATION);

			final Stage stage = new Stage(UTILITY);
			stage.setTitle(LANGUAGE_FILE.getString(DIALOG_ADD_CLIENT_TITLE));

			stage.setScene(scene);

			final AddClientDialogController controller = loader.getController();
			controller.setStage(stage);
			controller.setModel(model);

			stage.showAndWait();

			final String clientName = controller.getClientName();

			if(clientName != null){
				final Client client = new Client(clientName);

				addClientToUI(client);
				writeClientToDisk(client);
			}
		}catch(final Exception exception){
			exception.printStackTrace();
		}
	}

	private void writeClientToDisk(final Client client) throws IOException{
		final Path path = Paths.get(invoiceSaveLocation, client.getName());

		createDirectory(path);
	}

	private void writeJobSiteToDisk(final Client client, final JobSite jobSite) throws IOException{
		final Path path = Paths.get(invoiceSaveLocation, client.getName(), jobSite.getDescription());

		createDirectory(path);
	}

	private void addClientToUI(final Client client){
		if(!currentlyDisplayedClients.contains(client)){
			final Label clientLabel = new Label(client.getName());

			currentlyDisplayedClients.add(client);

			if(client.equals(model.getSelectedClient())){
				clientLabel.setTextFill(Color.web("#0076a3"));
				clientLabel.setFont(Font.font(clientLabel.getFont().getFamily(), clientLabel.getFont().getSize() * 2));

				vBox = new VBox();
				vBox.setAlignment(CENTER);

				hBoxSelectedClient.getChildren().clear();

				hBoxSelectedClient.getChildren().add(clientLabel);
				hBoxSelectedClient.getChildren().add(labelAddJobSite);

				vBox.getChildren().add(hBoxSelectedClient);

				client.getJobSites().forEach(jobSite -> {
					final Label labelJobSite = new Label(jobSite.getDescription());
					if(jobSite.getInvoice() != null){
						labelJobSite.setTextFill(DARKCYAN);
					}else{
						labelJobSite.setTextFill(DARKRED);
					}

					labelJobSite.setFont(Font.font(labelJobSite.getFont().getFamily(), labelJobSite.getFont().getSize() * 1.25));
					labelJobSite.setOnMouseClicked(event -> {
						model.setSelectedJobSite(jobSite);

						repaintInvoiceUI();
					});

					vBox.getChildren().add(labelJobSite);
				});

				vBoxClientList.getChildren().add(vBox);
			}else{
				clientLabel.setOnMouseClicked(e -> selectClient(client));

				vBoxClientList.getChildren().add(clientLabel);
			}
		}
	}

	private void repaintInvoiceUI(){
		checkInvoiceIntegrity();

		vBoxInvoices.getChildren().clear();

		Invoice invoice = model.getSelectedJobSite().getInvoice();

		if(invoice != null){
			do{
				final VBox vBox = new VBox();
				if(!invoice.isValid()){
					vBox.setStyle("-fx-background-color: #ff3333;");
				}

				final Label labelDate = new Label(FormatDate(invoice.getCreationDate()));
				labelDate.setFont(Font.font(labelDate.getFont().getFamily(), FontWeight.BOLD, labelDate.getFont().getSize() * 1.2));
				vBox.getChildren().add(labelDate);

				final Button buttonOpen = new Button("Open");
				vBox.getChildren().add(buttonOpen);

				final Label labelHash = new Label(invoice.getHashString());
				labelHash.setFont(Font.font(labelHash.getFont().getFamily(), labelHash.getFont().getSize() * 1.2));
				vBox.getChildren().add(labelHash);
				vBox.getChildren().add(new Separator());

				vBoxInvoices.getChildren().add(0, vBox);
			}while((invoice = invoice.getPrevious()) != null);
		}
	}

	private void selectClient(final Client client){
		if(!client.equals(model.getSelectedClient())){
			model.setSelectedClient(client);

			repaintUI();
		}
	}

	private void repaintUI(){
		final List<Client> buffer = new ArrayList<>(currentlyDisplayedClients.size());
		buffer.addAll(currentlyDisplayedClients);

		clearClientListUI();

		buffer.forEach(this::addClientToUI);

		currentlyDisplayedClients.addAll(buffer);
	}

	public void afterStartup(){
		final File root = new File(invoiceSaveLocation);

		if(root.exists()){
			final List<Client> clients = loadClientData(root);
			model.addClients(clients);

			for(final Client client : clients){
				addClientToUI(client);

				for(final JobSite jobSite : client.getJobSites()){
					Invoice invoice = jobSite.getInvoice();
					while(invoice != null){
						invoice = invoice.getPrevious();
					}
				}
			}
		}else{
			new IOException("File:'" + root.getAbsolutePath() + "' does not exist.").printStackTrace();

			// TODO: Show message that we failed to load data from disk. (Jan - 12/12/20)
		}
	}

	public void setInvoiceSaveLocation(final String invoiceSaveLocation){
		this.invoiceSaveLocation = invoiceSaveLocation;
	}

	private List<Client> loadClientData(final File dir){
		final File[] clients = requireNonNull(dir.listFiles(File::isDirectory));

		List<Client> clientList = null;

		if(clients.length != 0){
			clientList = new ArrayList<>(clients.length);

			for(final File client : clients){
				if(client.isDirectory()){
					final Client ret = new Client(client.getName());

					final List<JobSite> jobSites = loadJobSiteData(client);
					if(jobSites != null){
						ret.addJobSites(jobSites);
					}

					clientList.add(ret);
				}else{
					// TODO:  (Jan - 12/12/20)
				}
			}
		}

		return clientList;
	}

	private List<JobSite> loadJobSiteData(final File dir){
		final File[] jobSites = requireNonNull(dir.listFiles(File::isDirectory));

		List<JobSite> jobSiteList = null;

		if(jobSites.length != 0){
			jobSiteList = new ArrayList<>(jobSites.length);

			for(final File jobSite : jobSites){
				if(jobSite.isDirectory()){
					final JobSite ret = new JobSite(jobSite.getName());

					final Invoice invoice = loadInvoiceData(jobSite);
					if(invoice != null){
						ret.setInvoice(invoice);
					}

					jobSiteList.add(ret);
				}else{
					// TODO:  (Jan - 12/12/20)
				}
			}
		}

		return jobSiteList;
	}

	private Invoice loadInvoiceData(final File dir){
		Invoice invoice = null;

		final File[] invoices = dir.listFiles(File::isDirectory);

		if(requireNonNull(invoices).length != 0){
			sortInvoicesByDate(invoices);

			Invoice previous = null;

			for(final File invoiceDirectory : invoices){
				final LocalDateTime creationDate = FormatDate(invoiceDirectory.getName());

				final File[] files = requireNonNull(invoiceDirectory.listFiles(File::isFile));

				String fileLocation = null;
				byte[] hash = null;

				for(final File file : files){
					if(file.getName().equals(HASH_FILE_NAME)){
						try(final BufferedReader br = new BufferedReader(new FileReader(file))){
							hash = HexStringToByteArray(br.readLine());
						}catch(final IOException e){
							e.printStackTrace();
						}
					}else if(file.getName().equals(INVOICE_FILE_NAME)){
						fileLocation = file.getAbsolutePath();
					}
				}

				invoice = new Invoice(fileLocation, creationDate, hash, previous);

				previous = invoice;
			}
		}

		return invoice;
	}

	private void sortInvoicesByDate(final File[] invoices){
		sort(invoices, 0, invoices.length, (fileA, fileB) -> {
			final char[] a = fileA.getName().toCharArray();
			final char[] b = fileB.getName().toCharArray();

			for(int i = 0; i < min(a.length, b.length); i++){
				if(a[i] < b[i]){
					return -1;
				}

				if(a[i] > b[i]){
					return 1;
				}
			}

			return 0;
		});
	}

	// <- Getter & Setter ->
	public SFM_Model getModel(){
		return model;
	}

	// <- Static ->
}