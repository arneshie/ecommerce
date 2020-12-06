package bean;

import java.util.HashMap;

public class Order {
	private String user;
	private int status;
	private final String[] possibleStatuses = {"ORDERED", "PROCESSED", "COMPLETED"};
	private int id;
	private static int counter = 0;
	
	public Order(String string, int s) {
		user = string;
		id = counter;
		counter++;
		status = s;
	}
	
	public void updateStatus(int s) {
		this.status = s;
	}
	public String getUser() {
		return user;
	}

	public int getStatus() {
		return status;
	}

	public String[] getPossibleStatuses() {
		return possibleStatuses;
	}

	public int getId() {
		return id;
	}

	public static int getCounter() {
		return counter;
	}
	
	
}
