package bean;
import java.util.ArrayList;
import java.util.HashMap;

public class Cart {
	HashMap<Book, Integer> cart;
	
	public Cart() {
		cart = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<Book, Integer> getCart(){
		return (HashMap<Book, Integer>) cart.clone();
	}
	
	public void addToCart(Book b, int q) {
		if (!(cart.containsKey(b))) {
			cart.put(b,q);
		}
		else {
			cart.put(b, cart.get(b) + q);
		}
	}
	
	public void removeFromCart(Book b, int q) throws Exception{
		if (!(cart.containsKey(b))) throw new Exception("book not in cart");
		else if (q > cart.get(b)) throw new Exception("there are fewer than that quantity in the cart");
		else cart.put(b, cart.get(b) - q);
		updateCart();
	}
	
	private void updateCart() {
		@SuppressWarnings("unchecked")
		HashMap<Book, Integer> copy = (HashMap<Book, Integer> ) cart.clone();
		for (Book b: copy.keySet()) {
			if (cart.get(b) == 0) {
				cart.remove(b);
			}
		}
	}
	
	private void clearCart() {
		cart.clear();
	}
	
	@Override 
	public String toString() {
		String result = "";
		result += "Cart:\n";
		for (Book b: cart.keySet()){
			result += b.toString() + " x" + cart.get(b) + "\n";
		}
		return result;
	}
}