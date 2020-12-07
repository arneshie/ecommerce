package model;

import java.sql.SQLException;
import java.util.ArrayList;
import bean.*;
import java.util.HashMap;
import java.util.Map;

public class Sys {

//	private static HashMap<Integer, User> users;
//	private static HashMap<Book, Integer> catalogue;
//	private static ArrayList<Order> orders;
//	private static int counter;
//	private BookDAO bookData;
//	
//	public Sys() {
////		users = new HashMap<>();
////		catalogue = new HashMap<>();
////		orders = new ArrayList<>();
//		try {
//			this.bookData = new BookDAO();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}

	private static Sys instance;
	private BookDAO bookData;
	private UserDAO userData;
	private CartDAO cartData;
	private OrderDAO orderData;
	private static HashMap<Integer, User> users;
	private static HashMap<Book, Integer> catalogue;
	private static ArrayList<Order> orders;
	private static int counter;

	public static Sys getInstance() throws ClassNotFoundException {
		if (instance == null) {
			instance = new Sys();
			instance.bookData = new BookDAO();
			instance.userData = new UserDAO();
			instance.orderData = new OrderDAO();
			instance.users = new HashMap<>();
			instance.catalogue = new HashMap<>();
			instance.cartData = new CartDAO();

		}
		return instance;
	}

	private Sys() {

	}

	public HashMap<Book, Integer> retrieveRestBook(String bid) {
		HashMap<Book, Integer> rv = new HashMap<Book, Integer>();
		try {
			rv = bookData.retrieveProductInfo(bid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rv;
	}
	 public int retrievePrice(String s) throws SQLException {
	        return bookData.retrievePrice(s);
	    }

	public HashMap<Book, Integer> retrieveBooks() {
		System.out.println("retrieving books...");
		HashMap<Book, Integer> rv = new HashMap<Book, Integer>();
		try {

			rv = bookData.retrieveAllBooks();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rv;
	}

	public HashMap<Book, Integer> retrieveCategory(String category) {
		System.out.println("Retrieve books from category: " + category);
		HashMap<Book, Integer> rv = new HashMap<Book, Integer>();
		try {
			rv = bookData.retrieveCat(category);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rv;
	}

	public void addUser(User u) throws Exception {
		userData.insert(u.getId(), u.getUsername(), u.getPassword(), u.getAddress());
	}

	public void addOrder(Order o) {
		try {
			orderData.insert(o);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<Integer, User> getUsers() throws Exception {	
		HashMap<Integer, User> rv = new HashMap<>();
		try {
			rv = userData.getAllUsers();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return rv;
	}
	
	public ArrayList<String> getUsernames() {
		ArrayList<String> rv = new ArrayList<>();
		try {
			rv = userData.getAllUsernames();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rv;
	}

	public static int getCounter() {
		return counter;
	}

	public static void upCounter() {
		counter++;
	}

	public void addBook(Book b, int q) {
		if (!(catalogue.containsKey(b)))
			catalogue.put(b, q);
		else
			catalogue.put(b, catalogue.get(b) + q);
	}

	public HashMap<Book, Integer> getCatalogue() {
		return catalogue;
	}

	@Override
	public String toString() {
		String result = "";
		result += "Users: ";
		for (User u : users.values()) {
			result += u.toString() + "\n";
		}
		result += "Catalogue: ";
		for (Book b : catalogue.keySet()) {
			result += b.toString() + ", quantity: " + catalogue.get(b) + "\n";
		}
		return result;
	}

	public void addCart(String b, String attribute, int quantity) throws SQLException, ClassNotFoundException {
			cartData.insert(b, attribute, quantity);
	}
	
	public HashMap<String, Integer> retrieveCart (String string) throws SQLException{
		return cartData.retrieveCart(string);
	}

	public void updateCart(String title, String user, int parseInt) throws ClassNotFoundException, SQLException {
		cartData.updateCart(title, user, parseInt);
	}

	public void removeFromCart(String title, String user) throws ClassNotFoundException, SQLException {
		cartData.removeFromCart(title, user);
		
	}

}
