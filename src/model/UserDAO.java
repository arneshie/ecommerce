package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.User;

public class UserDAO {
	
	DataSource ds;
	
	//USER DAO class
	public UserDAO() {
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getAllUsernames() throws SQLException {
		ArrayList<String> rv = new ArrayList<String>();
		String preparedStatement = "select * from users";
		Connection con = this.ds.getConnection();
		PreparedStatement stmt = con.prepareStatement(preparedStatement);
		ResultSet r = stmt.executeQuery(); 
		int i = 0;
		while (r.next()) {
			String username = r.getString("username");
			try {
				rv.add(username);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		r.close();
		stmt.close();
		con.close();
		return rv;
	}
	
	public HashMap<Integer, User> getAllUsers() throws Exception{
		HashMap<Integer, User> rv = new HashMap<>();
		String preparedStatement = "select * from users";
		Connection con = this.ds.getConnection();
		PreparedStatement stmt = con.prepareStatement(preparedStatement);
		ResultSet r = stmt.executeQuery(); 
		int i = 0;
		while (r.next()) {
			String username = r.getString("username");
			String pword = r.getString("password");
			int id = r.getInt("id");
			User u = new User(username, pword, id);
			try {
				rv.put(id, u);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		r.close();
		stmt.close();
		con.close();
		return rv;
	}
	
	public int insert(int i, String name, String password, String address) throws Exception {
		String preparedStatement = "insert into users values(?,?,?,?)";
		Connection con = this.ds.getConnection();
		PreparedStatement stmt = con.prepareStatement(preparedStatement);
		stmt.setString(1, i+"");
		stmt.setString(2, name);
		stmt.setString(3, password);
		stmt.setString(4,  address);
		return stmt.executeUpdate();
	}
	
	

}
