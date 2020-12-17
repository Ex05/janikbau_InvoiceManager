package nrw.janikbau.sfm;
// <- Import ->

// <- Static_Import ->

import javafx.beans.property.SimpleStringProperty;

public class JobSite{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final SimpleStringProperty description;

	private Invoice invoice;

	// <- Static ->

	// <- Constructor ->
	public JobSite(final String description){
		this.description = new SimpleStringProperty();;

		setDescription(description);
	}

	// <- Abstract ->

	// <- Object ->

	// <- Getter & Setter ->
	public Invoice getInvoice(){
		return invoice;
	}

	public void setInvoice(final Invoice invoice){
		this.invoice = invoice;
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
