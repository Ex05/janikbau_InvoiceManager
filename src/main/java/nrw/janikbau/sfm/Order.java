package nrw.janikbau.sfm;
// <- Import ->

// <- Static_Import ->

import javafx.beans.property.SimpleStringProperty;

public class Order{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final SimpleStringProperty description;

	// <- Static ->

	// <- Constructor ->
	public Order(){
		description = new SimpleStringProperty();
	}

	// <- Abstract ->
	// <- Object ->

	// <- Getter & Setter ->
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
