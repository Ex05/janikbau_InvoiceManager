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

	private final SimpleStringProperty address;

	private final List<Order> orders;

	// <- Static ->

	// <- Constructor ->
	public Client(final String name, final String address){
		this.name = new SimpleStringProperty(name);

		this.address = new SimpleStringProperty(address);

		orders = new LinkedList<>();
	}

	// <- Abstract ->

	// <- Object ->
	public void addOrder(final Order order){
		orders.add(order);
	}

	// <- Getter & Setter ->
	public List<Order> getOrders(){
		return orders;
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

	public String getAddress(){
		return address.get();
	}

	public SimpleStringProperty addressProperty(){
		return address;
	}

	public void setAddress(final String address){
		this.address.set(address);
	}

	// <- Static ->
}
