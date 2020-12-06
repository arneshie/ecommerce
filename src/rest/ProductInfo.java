package rest;

import javax.xml.bind.annotation.XmlRootElement;


public class ProductInfo {
	
	private String title;
	private String genre;

	
	public ProductInfo() {
		
	}
	
	public ProductInfo(String title, String genre) {
		this.title = title;
		this.genre = genre;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	
	
	

}
