package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Book;
import bean.Cart;
import bean.Order;
import bean.User;
import model.Sys;

/**
 * Servlet implementation class SYS
 */
@WebServlet({"/BookStore/*"})
public class SYS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SYS() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = this.getServletContext();
		//Singleton implementation
		Sys mSys = null;
		try {
			mSys = Sys.getInstance();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
	}	//context variables initialization
		context.setAttribute("modelSYS", mSys);	
		context.setAttribute("loggedIn", null);	
		context.setAttribute("numpayments", 0);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String target = "/Form.jspx";
		String path = request.getRequestURI();
		HttpSession hs = request.getSession();
		System.out.println(path);
		ServletContext sc = getServletContext();
		Sys model = (Sys) sc.getAttribute("modelSYS");
		HashMap<Book, Integer> bookCat = new HashMap<Book, Integer>();
		HashMap<Book, Integer> retrievedCat = new HashMap<Book, Integer>();
		hs.setAttribute("registerAttempt", null);
		
		//Handle category display via EL and JSPX session variables
		if (path.contains("Item")) {
			target = "/Category.jspx";
			String category = request.getParameter("cat");
			retrievedCat = model.retrieveCategory(category);
			System.out.println(retrievedCat);
			hs.setAttribute("category", category);
			hs.setAttribute("books", retrievedCat);
			System.out.println("Category selected: " + category);
			request.getRequestDispatcher(target).forward(request, response);
		}
		//Logout sets session attributes to null, equivalent of fresh session for our implementation
		else if (path.contains("Logout")) {
			hs.setAttribute("username", null);
			hs.setAttribute("loggedIn", null);
			hs.setAttribute("attemptRegister", null);
			hs.setAttribute("attempt", null);
			target = "/Form.jspx";
			request.getRequestDispatcher(target).forward(request, response);
		}
		//Login to the system.
		else if (path.contains("Login")){
			if (!(request.getParameter("attempt") == null)) {
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				HashMap<Integer, User> users = null;
				try {
					users = model.getUsers();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Ensure this combination of username and password is registered
				User u = null;
				for (int i: users.keySet()) {
					if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password)) {
						u = users.get(i);
					}
				}
				//if the user is registered, set session variables to indiace a successful login
				if (!(u == null)) {
					hs.setAttribute("loggedIn", true);
					hs.setAttribute("username", u.getUsername());
					hs.setAttribute("attempt", null);
					try {
						hs.setAttribute("cart", model.retrieveCart(u.getUsername()));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//failed login sets attempt, provides error message in Login.jspx
				hs.setAttribute("attempt", "attempted");
				target = "/Login.jspx";
				request.getRequestDispatcher(target).forward(request, response);
			}
			//forward request
			target = "/Login.jspx";
			request.getRequestDispatcher(target).forward(request, response);
		}
		//Similar to login, session variables change the content of Register.jspx
		else if (path.contains("Register")) {
			if (hs.getAttribute("attemptRegister") != null) {
				//debug
				System.out.println("reached");
				String username = request.getParameter("u_name");
				String pword = request.getParameter("pwrd");
				String address = request.getParameter("address");
				ArrayList<String> usernames = null;
				try {
					usernames = model.getUsernames();
				}
				catch (Exception e) {
					
				}
				if (usernames.contains(username)) {
					request.getSession().setAttribute("registerError", "true");
				}
				//Update user table via DAO access from singleton model
				else {
					try {
						model.addUser(new User(username, pword, address));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					hs.setAttribute("registerError", null);
					hs.setAttribute("username", username);
					try {
						System.out.println(model.getUsers().values());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				hs.setAttribute("registerAttempt", null);
				target = "/Register.jspx";
				request.getRequestDispatcher(target).forward(request, response);
				}
			else {
				target = "/Register.jspx";
				request.getRequestDispatcher(target).forward(request, response);
			}
		}
		//adding to cart
		else if (path.contains("AddCart")) {
			//user must be logged in, request them to log in if they are not.
			if(hs.getAttribute("loggedIn") == null) {
				target = "/Login.jspx";
				request.getRequestDispatcher(target).forward(request, response);
			}
			else {
			//find which books were selected in the form by comparing their quantity selected and ensuring its non-zero.
			Map<String, String[]> params = request.getParameterMap();
			ArrayList<String> titles = new ArrayList<>();
			for (String s: params.keySet()) {
				for(String subs: params.get(s)) {
					if (!(subs.equals(""))) {
						titles.add(s);
					}
				}
			} 
			//ensure the quantity of the book requested exists.
			HashMap<Book, Integer> cat = model.retrieveBooks();
			for (Book b: cat.keySet()) {
				System.out.println(b.getTitle());
				if (titles.contains(b.getTitle())){
					try {
						//complete the adding to cart process by updating table via DAO + model.
						model.addCart(b.getTitle(), (String) hs.getAttribute("username"), Integer.parseInt(params.get(b.getTitle())[0]));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//Update user's cart on the session scope, to display when they press view cart.
			try {
				hs.setAttribute("cart", model.retrieveCart((String) hs.getAttribute("username")));
				System.out.println(hs.getAttribute("cart"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			target = "/Form.jspx";
			request.getRequestDispatcher(target).forward(request, response);
		}
		}
		//Remove is mirror of add, except servlet handles forwarding to payment page if button is pressed. (Using same form, simpler xhtml/jspx)
		else if (path.contains("RemoveCart")) {
			if(hs.getAttribute("loggedIn") == null) {
				target = "/Login.jspx";
				request.getRequestDispatcher(target).forward(request, response);
			}
			//Forward to checkout, if pay button is pressed
			else if (!(request.getParameter("checkout") == null)) {
				target = "/Checkout.jspx";
				request.getRequestDispatcher(target).forward(request, response);
			}
			else {
			Map<String, String[]> params = request.getParameterMap();
			ArrayList<String> titles = new ArrayList<>();
			for (String s: params.keySet()) {
				for(String subs: params.get(s)) {
					if (!(subs.equals(""))) {
						titles.add(s);
					}
				}
			} 
			HashMap<Book, Integer> cat = model.retrieveBooks();
			for (Book b: cat.keySet()) {
				System.out.println(b.getTitle());
				if (titles.contains(b.getTitle())){
					String user = (String) hs.getAttribute("username");
					try {
						if ((model.retrieveCart(user).get(b.getTitle()) - Integer.parseInt(request.getParameter(b.getTitle())) > 0)){
							model.updateCart(b.getTitle(), user, -1 * Integer.parseInt(request.getParameter(b.getTitle())));
						}
						else {
							model.removeFromCart(b.getTitle(), user);
						}
					} catch (NumberFormatException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				hs.setAttribute("cart", model.retrieveCart((String) hs.getAttribute("username")));
				System.out.println(hs.getAttribute("cart"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			target = "/Cart.jspx";
			request.getRequestDispatcher(target).forward(request, response);

		}
		}
		//Display the cart
		else if (path.contains("ViewCart")) {
//            if (hs.getAttribute("loggedIn") != null) {
//            target = "/Cart.jspx";
//            HashMap<String, Integer> cart = null;
//            try {
//                cart = model.retrieveCart((String)hs.getAttribute("username"));
//            } catch (SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            if (cart != null) {
//                int price = 0;
//                for (String s:cart.keySet()) {
//                    try {
//                        price += model.retrievePrice(s);
//                    } catch (SQLException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//                hs.setAttribute("subtotal", price);
//            }
//            }
//            else {
//                target = "/Login.jspx";
//            }
			
				if (hs.getAttribute("loggedIn") != null) {
				    target = "/Cart.jspx";
				}
				else {
				target = "/Login.jspx";
				}
				request.getRequestDispatcher(target).forward(request, response);
				}
		//Payment
		else if (path.contains("Pay")) {
			hs.setAttribute("attemptedPayment", "attempt");
			int num = (int) sc.getAttribute("numpayments");
			num++;
			//HARD CODE AS REQUESTED: EVERY 3rd PAYMENT FAILS
			if (num % 3 == 0) {
				hs.setAttribute("valid", null);
				hs.setAttribute("attemptedPayment", null);
			}
			else {
				hs.setAttribute("valid", "valid");
				model.addOrder(new Order((String)hs.getAttribute("username"), 0));
			}
				
			target = "/Checkout.jspx";
			request.getRequestDispatcher(target).forward(request, response);
		}
		//Default, home page, display catalog w/no filter
		else {
		bookCat = model.retrieveBooks();
		hs.setAttribute("bookCat", bookCat);
		response.getWriter().append("Served at: ").append(request.getContextPath());
		request.getRequestDispatcher(target).forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
