package nrw.janikbau.sfm.controller;
// <- Import ->

// <- Static_Import ->

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import nrw.janikbau.sfm.Invoice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
		final int currentYear = LocalDate.now().getYear();

		labelPreviousYear.setText((currentYear - 1) + "");
		labelCurrentYear.setText(currentYear + "");
		labelNextYear.setText((currentYear + 1) + "");
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

		final File root = new File(invoiceSaveLocation);

		if(root.exists()){
			loadClientData(root);
		}else{
			new IOException("File:'" + root.getAbsolutePath() + "' does not exist.").printStackTrace();

			// TODO: Show message that we failed to load data from disk. (Jan - 12/12/20)
		}

		System.exit(0);
	}

	public void setInvoiceSaveLocation(final String invoiceSaveLocation){
		this.invoiceSaveLocation = invoiceSaveLocation;
	}

	private void loadClientData(final File dir){
		final File[] clients = requireNonNull(dir.listFiles(File::isDirectory));

		if(clients.length == 0){
			System.out.println("No clients");
		}else{
			for(final File client : clients){
				System.out.println(client.getName());

				if(client.isDirectory()){
					loadJobSiteData(client);
				}else{
					// TODO:  (Jan - 12/12/20)
				}
			}
		}
	}

	private void loadJobSiteData(final File dir){
		final File[] jobSites = requireNonNull(dir.listFiles(File::isDirectory));

		if(jobSites.length == 0){
			System.out.println("No job sites.");
		}else{
			for(final File jobSite : jobSites){
				if(jobSite.isDirectory()){
					System.out.println("\t" + jobSite.getName());

					loadInvoiceData(jobSite);
				}else{
					// TODO:  (Jan - 12/12/20)
				}
			}
		}
	}

	private void loadInvoiceData(final File dir){
		final File[] invoices = dir.listFiles(File::isDirectory);

		if(requireNonNull(invoices).length == 0){
			System.out.println("No invoices..");
		}else{
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

				final Invoice invoice = new Invoice(fileLocation, creationDate, hash, previous);
				previous = invoice;

				System.out.println("\t\t" + invoice.getCreationDate().toString());
				System.out.println("\t\t\t" + invoice.getHashString());
				System.out.println("\t\t\t" + invoice.getFileLocation());
			}
		}
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
	// <- Static ->
}



/*
			for(final File invoiceDir : invoices){
				if(invoiceDir.isDirectory()){
					System.out.println("\t\t" + invoiceDir.getName());

					final File[] invoiceData = invoiceDir.listFiles();

					for(final File file : requireNonNull(invoiceData)){
						if(file.getName().equals(HASH_FILE_NAME)){
							System.out.println("\t\t\thash");
						}else if(file.getName().equals(INVOICE_FILE_NAME)){
							System.out.println("\t\t\tinvoice");
						}
					}
				}
			}
* */