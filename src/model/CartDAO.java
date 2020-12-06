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
import bean.User;


public class CartDAO {		
		DataSource ds;

		public CartDAO() throws ClassNotFoundException{
			try {
				ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");

			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		
		public HashMap<String, Integer> retrieveCart(String string) throws SQLException {
//			ArrayList<String> reviews = new ArrayList<String>();
			String query1= "Select * from Cart where username = ?";
			HashMap<String, Integer> rv = new HashMap<>();
			Connection con = this.ds.getConnection();
			Statement stmt = con.createStatement();
			PreparedStatement p1 = con.prepareStatement(query1);
			p1.setString(1, string);
			ResultSet r1 = p1.executeQuery();
			while (r1.next()) {
				String book_title = r1.getString("book");
				String username = r1.getString("username");
				int quantity = r1.getInt("quantity");
//				r2 = stmt.executeQuery("select sid from ENROLLMENT where cid = '"+cid+"'");
				rv.put(book_title, quantity);
			}
			r1.close();
			p1.close();
			con.close();
			return rv;
		}
		
		public void insert (String b, String user, int quantity) throws SQLException, ClassNotFoundException {
			HashMap<String, Integer> rv = new HashMap<>();
			rv = Sys.getInstance().retrieveCart(user);
			if (rv.containsKey(b)) {
				updateCart(b, user, quantity);
			}
			else {
			Connection con = this.ds.getConnection();
			PreparedStatement p = con.prepareStatement("insert into cart values(?, ?, ?)");
			p.setString(1, b);
			p.setString(2, user);
			p.setString(3,  quantity+"");
			p.executeUpdate();
			p.close();
			con.close();
			}
		}
		
		public void updateCart (String b, String user, int quantity) throws SQLException, ClassNotFoundException {
			HashMap<String, Integer> rv = new HashMap<>();
			rv = Sys.getInstance().retrieveCart(user);
			int newQ = 0;
			if (rv.containsKey(b)) newQ = rv.get(b);
			Connection con = this.ds.getConnection();
			PreparedStatement p = con.prepareStatement("UPDATE CART SET quantity = ? WHERE book = ? AND username = ?");
			p.setString(1, quantity+newQ+"");
			p.setString(2, b);
			p.setString(3, user);
			p.executeUpdate();
			p.close();
			con.close();
		}
		
		public void removeFromCart(String b, String user) throws ClassNotFoundException, SQLException {
			HashMap<String, Integer> rv = new HashMap<>();
			rv = Sys.getInstance().retrieveCart(user);
			int newQ = 0;
			if (rv.containsKey(b)) newQ = rv.get(b);
			Connection con = this.ds.getConnection();
			PreparedStatement p = con.prepareStatement("DELETE FROM CART WHERE book = ? AND username = ?");
			p.setString(1, b);
			p.setString(2, user);
			p.executeUpdate();
			p.close();
			con.close();
		}
		
		public void clearCart(String user) throws SQLException {
			Connection con = this.ds.getConnection();
			PreparedStatement p = con.prepareStatement("DELETE FROM CART WHERE username = ?");
			p.setString(1, user);
			p.executeUpdate();
			p.close();
			con.close();
		}
	}


