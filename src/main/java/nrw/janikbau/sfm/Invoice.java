package nrw.janikbau.sfm;
// <- Import ->

// <- Static_Import ->

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;

import static java.lang.System.identityHashCode;
import static nrw.janikbau.sfm.util.Util.FormatDate;
import static nrw.janikbau.sfm.util.Util.HashToString;

public class Invoice{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final Invoice previous;

	private final SimpleStringProperty fileLocation;

	private final SimpleStringProperty hashString;

	private final LocalDateTime creationDate;

	private byte[] hash;

	private boolean valid;

	// <- Static ->

	// <- Constructor ->
	public Invoice(final String fileLocation, final LocalDateTime creationDate, final byte[] hash, final Invoice previous){
		this.creationDate = creationDate;

		this.fileLocation = new SimpleStringProperty();
		setFileLocation(fileLocation);

		hashString = new SimpleStringProperty();
		setHash(hash);

		this.previous = previous;

		valid = true;
	}

	// <- Abstract ->
	// <- Object ->

	@Override
	public String toString(){
		final  StringBuilder b = new StringBuilder();
		b.append(getClass().getSimpleName());
		b.append(":[");
		b.append("creationDate='").append(FormatDate(creationDate)).append("', ");
		b.append("fileName='").append(fileLocation.get()).append("', ");
		b.append("previous='").append(previous == null ? "null" : "0x" + identityHashCode(previous)).append("'");
		b.append("]");

		return b.toString();
	}

	// <- Getter & Setter ->
	public boolean isValid(){
		return valid;
	}

	public void setValid(final boolean valid){
		this.valid = valid;
	}

	public LocalDateTime getCreationDate(){
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
