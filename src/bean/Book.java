package bean;
import java.util.ArrayList;
import java.util.HashMap;

public class Book {
	private String genre;
	private String title;
	private ArrayList<Review> reviews;
	
	public Book(String g, String t) {
		reviews = new ArrayList<>();
		genre = g;
		title = t;
		
	}


	public void addReview(Review r) {
		reviews.add(r);
	}

	public String getGenre() {
		return genre;
	}

	public String getTitle() {
		return title;
	}
	
	private double averageRating() {
		double sum = 0;
		for (Review r : reviews) {
			sum += r.getRating();
		}
		return sum / reviews.size();
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}
	
	@Override 
	public String toString() {
		return title;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj.getClass() == this.getClass())) return false;
		Book asBook = (Book) obj;
		if (title.equals(asBook.getTitle()))
			return true;
		return false;
	}
}
