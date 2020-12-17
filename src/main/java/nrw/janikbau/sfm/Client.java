package nrw.janikbau.sfm;
// <- Import ->

// <- Static_Import ->

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class Client{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final SimpleStringProperty name;

	private final List<JobSite> jobSites;

	// <- Static ->

	// <- Constructor ->
	public Client(final String name){
		this.name = new SimpleStringProperty(name);

		jobSites = new LinkedList<>();
	}

	// <- Abstract ->

	// <- Object ->
	public void addJobSite(final JobSite jobSite){
		jobSites.add(jobSite);
	}

	public void addJobSites(final List<JobSite> jobSites){
		this.jobSites.addAll(jobSites);
	}

	// <- Getter & Setter ->
	public List<JobSite> getJobSites(){
		return jobSites;
	}

	public String getName(){
		return name.get();
	}

	public SimpleStringProperty nameProperty(){
		return name;
	}

	public void setName(final String name){
		this.name.set(name);
	}

	// <- Static ->
}
