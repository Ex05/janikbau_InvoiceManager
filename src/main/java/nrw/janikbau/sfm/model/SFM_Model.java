package nrw.janikbau.sfm.model;
// <- Import ->

// <- Static_Import ->

import nrw.janikbau.sfm.Client;
import nrw.janikbau.sfm.JobSite;

import java.util.ArrayList;
import java.util.List;

public class SFM_Model{
	// <- Public ->
	// <- Protected ->

	// <- Private->
	private final List<Client> clients;

	private Client selectedClient = null;

	private JobSite selectedJobSite = null;

	// <- Static ->

	// <- Constructor ->
	public SFM_Model(){
		clients = new ArrayList<>();
	}

	// <- Abstract ->

	// <- Object ->
	public void addClient(final Client client){
		clients.add(client);
	}

	public void addClients(final Client... clients){
		for(final Client client : clients){
			addClient(client);
		}
	}

	public void addClients(final List<Client> clients){
		this.clients.addAll(clients);
	}

	// <- Getter & Setter ->
	public List<Client> getClients(){
		return clients;
	}

	public Client getSelectedClient(){
		return selectedClient;
	}

	public void setSelectedClient(final Client selected){
		this.selectedClient = selected;
	}

	public JobSite getSelectedJobSite(){
		return selectedJobSite;
	}

	public void setSelectedJobSite(final JobSite selectedJobSite){
		this.selectedJobSite = selectedJobSite;
	}

	// <- Static ->
}
