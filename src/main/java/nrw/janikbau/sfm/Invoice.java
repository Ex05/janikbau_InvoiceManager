package nrw.janikbau.sfm;
// <- Import ->

// <- Static_Import ->

import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

import static nrw.janikbau.sfm.util.Util.HashToString;

public class Invoice{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final Invoice previous;

	private final SimpleStringProperty fileLocation;

	private final SimpleStringProperty hashString;

	private final Date creationDate;

	private byte[] hash;

	// <- Static ->

	// <- Constructor ->
	public Invoice(final String fileLocation, final Date creationDate, final byte[] hash, final Invoice previous){
		this.creationDate = creationDate;

		this.fileLocation = new SimpleStringProperty();
		setFileLocation(fileLocation);

		hashString = new SimpleStringProperty();
		setHash(hash);

		this.previous = previous;
	}

	// <- Abstract ->
	// <- Object ->

	// <- Getter & Setter ->
	public Date getCreationDate(){
		return creationDate;
	}

	public byte[] getHash(){
		return hash;
	}

	public void setHash(final byte[] hash){
		this.hash = hash;

		hashString.set(HashToString(hash));
	}

	public String getHashString(){
		return hashString.get();
	}

	public SimpleStringProperty hashStringProperty(){
		return hashString;
	}

	public SimpleStringProperty fileLocationProperty(){
		return fileLocation;
	}

	public String getFileLocation(){
		return fileLocation.get();
	}

	public void setFileLocation(final String fileLocation){
		this.fileLocation.set(fileLocation);
	}

	public Invoice getPrevious(){
		return previous;
	}

	// <- Static ->
}
