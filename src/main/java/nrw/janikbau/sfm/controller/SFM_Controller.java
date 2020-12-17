package nrw.janikbau.sfm.controller;
// <- Import ->

// <- Static_Import ->

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nrw.janikbau.sfm.Client;
import nrw.janikbau.sfm.Invoice;
import nrw.janikbau.sfm.JobSite;
import nrw.janikbau.sfm.model.SFM_Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;
import static java.util.Arrays.sort;
import static java.util.Objects.requireNonNull;
import static nrw.janikbau.sfm.util.Constants.HASH_FILE_NAME;
import static nrw.janikbau.sfm.util.Constants.INVOICE_FILE_NAME;
import static nrw.janikbau.sfm.util.Util.FormatDate;
import static nrw.janikbau.sfm.util.Util.HexStringToByteArray;

public class SFM_Controller{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final SFM_Model model;

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
	public SFM_Controller(){
		model = new SFM_Model();
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
	}

	@FXML
	void onButtonSearchPressed(final ActionEvent e){
		log("Searching...");

		new Thread(() -> {
			final String searchText = textFieldSearchBar.getText();

			Platform.runLater(() -> {
				textFieldSearchBar.setText("");

				textFieldSearchBar.setDisable(true);
			});

			Object searchResult = null;

			for(final Client client : model.getClients()){
				try{
					Thread.sleep(2_000);
				}catch(final InterruptedException exception){
					exception.printStackTrace();
				}

				if(client.getName().contains(searchText)){
					searchResult = client;

					break;
				}else{
					searchResult = client.getJobSites().stream().filter(jobSite -> jobSite.getDescription().contains(searchText)).findFirst().orElse(null);
				}
			}

			if(searchResult != null){
				final Object finalSearchResult = searchResult;

				if(finalSearchResult instanceof Client){
					Platform.runLater(() -> log(((Client) finalSearchResult).getName()));
				}else if(finalSearchResult instanceof JobSite){
					Platform.runLater(() -> log(((JobSite) finalSearchResult).getDescription()));
				}
			}

			Platform.runLater(() -> textFieldSearchBar.setDisable(false));
		}, "SearchBar").start();
	}

	public void afterStartup(){
		buttonSearch.requestFocus();

		final File root = new File(invoiceSaveLocation);

		if(root.exists()){
			final List<Client> clients = loadClientData(root);
			model.addClients(clients);

			for(final Client client : clients){
				System.out.println(client.getName());

				for(final JobSite jobSite : client.getJobSites()){
					System.out.println("\t" + jobSite.getDescription());

					Invoice invoice = jobSite.getInvoice();
					while(invoice != null){
						System.out.println("\t\t" + invoice);

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