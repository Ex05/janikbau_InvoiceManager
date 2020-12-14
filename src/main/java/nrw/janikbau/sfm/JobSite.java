package nrw.janikbau.sfm;
// <- Import ->

// <- Static_Import ->

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class JobSite{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final SimpleStringProperty description;

	private final List<Invoice> invoices;

	// <- Static ->

	// <- Constructor ->
	public JobSite(){
		description = new SimpleStringProperty();

		invoices = new LinkedList<>();
	}

	// <- Abstract ->

	// <- Object ->
	public void addInvoice(final Invoice invoice){
		invoices.add(invoice);
	}

	public void removeInvoice(final Invoice invoice){
		invoices.remove(invoice);
	}

	// <- Getter & Setter ->
	public List<Invoice> getInvoices(){
		return invoices;
	}

	public String getDescription(){
		return description.get();
	}

	public SimpleStringProperty descriptionProperty(){
		return description;
	}

	public void setDescription(final String description){
		this.description.set(description);
	}

	// <- Static ->
}
