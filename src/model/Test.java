package model;
import bean.*;

import java.util.HashMap;

public class Test {
	public static void main(String[] args) throws Exception {
		Sys system = null;
		try {
			system = Sys.getInstance();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int x = 1;
		User u = null;
		try {
			u = new User("Arneshie", "asdf123", x);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		system.addUser(u);
		for (int i = 0; i < 10; i++) {
			Book b = new Book("Science Fiction", "Book" + x);
			system.addBook(b, x);
			x++;
		}
		Cart c = u.getCart();
		HashMap<Book, Integer> cat = system.getCatalogue();
		for (Book b: cat.keySet()) c.addToCart(b, 1);
		System.out.println(system);
		System.out.println(c);
	}
}
