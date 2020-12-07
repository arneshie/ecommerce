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
import bean.Order;
import bean.User;

//DAO Class for Order Table
public class OrderDAO {		
		DataSource ds;

		public OrderDAO() throws ClassNotFoundException{
			try {
				ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");

			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		
		public ArrayList<Order> retrieveOrders(String string) throws SQLException {
			String query1= "Select * from Orders where username = ?";
			ArrayList<Order> rv = new ArrayList<>();
			Connection con = this.ds.getConnection();
			Statement stmt = con.createStatement();
			PreparedStatement p1 = con.prepareStatement(query1);
			p1.setString(1, string);
			ResultSet r1 = p1.executeQuery();
			while (r1.next()) {
				String book_title = r1.getString("orderid");
				String username = r1.getString("username");
				String status = r1.getString("status");
				rv.add(new Order(username, Integer.parseInt(status)));
			}
			r1.close();
			p1.close();
			con.close();
			return rv;
		}
				
		public void updateOrder(String user, int status) throws ClassNotFoundException, SQLException {
			Connection con = this.ds.getConnection();
			PreparedStatement p = con.prepareStatement("UPDATE ORDERS SET status = ? WHERE username = ?");
			p.setString(1, status+"");
			p.setString(2, user);
			p.executeUpdate();
			p.close();
			con.close();
		}

		public void insert(Order o) throws SQLException {
			Connection con = this.ds.getConnection();
			PreparedStatement p = con.prepareStatement("insert into orders values(?, ?, ?)");
			p.setString(1, o.getId()+"");
			p.setString(2, o.getUser());
			p.setString(3,  o.getStatus()+"");
			p.executeUpdate();
			p.close();
			con.close();
		}
	}


