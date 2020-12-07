package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.Book;

public class BookDAO {
	
	DataSource ds;

	public BookDAO() throws ClassNotFoundException{
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<Book, Integer> retrieveAllBooks() throws SQLException {
//		ArrayList<String> reviews = new ArrayList<String>();
		String query1= "Select * from Book";
		HashMap<Book, Integer> rv = new HashMap<Book, Integer>();
		Connection con = this.ds.getConnection();
		Statement stmt = con.createStatement();
		PreparedStatement p1 = con.prepareStatement(query1);
		ResultSet r1 = p1.executeQuery();
		int i = 0;
		while (r1.next()) {
			String bid = r1.getString("BID");
			String category = r1.getString("CATEGORY");
			String title = r1.getString("TITLE");
			
			rv.put(new Book(category,  title), i);
			i++;
		}
		r1.close();
		p1.close();
		con.close();
		return rv;
	}
	
	 public int retrievePrice(String s) throws SQLException{
	        Connection con = this.ds.getConnection();
	        int price = 0;
	        PreparedStatement p = con.prepareStatement("Select * from book where title like ?");
	        p.setString(1, s);
	        ResultSet r = p.executeQuery();
	        while (r.next()) {
				 price = r.getInt("price");
			}
	        r.close();
			p.close();
			con.close();
			return price;
	    }
	
	public HashMap<Book, Integer> retrieveProductInfo(String bid) throws SQLException {
		HashMap<Book, Integer> rv = new HashMap<Book, Integer>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement("Select * from book where bid = ?");
		p.setString(1, bid);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			String bookId = r.getString("BID");
			String cat = r.getString("CATEGORY");
			String title = r.getString("TITLE");
		
			rv.put(new Book(cat, title), 1);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	public HashMap<Book, Integer> retrieveCat(String category) throws SQLException {

		HashMap<Book, Integer> rv = new HashMap<Book, Integer>();
		Connection con = this.ds.getConnection();
		
		PreparedStatement P =con.prepareStatement("SELECT * from book WHERE  category = ?");
		P.setString(1, category);
	
		ResultSet r = P.executeQuery();
		System.out.println(r);
		int i = 0;
		while (r.next()) {
			String bid = r.getString("BID");
			String cat = r.getString("CATEGORY");
			String title = r.getString("TITLE");
			rv.put(new Book(cat, title), i);
			i++;
		}
		r.close();
		P.close();
		con.close();
		return rv;
	}

}
