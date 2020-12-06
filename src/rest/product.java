package rest;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

import bean.Book;
import model.Sys;


@Path("product")
public class product {
	
	@GET
	@Path("/print/")
	@Produces(MediaType.APPLICATION_JSON)
	public String produceJSON( @QueryParam("bid") String bid ) {
		Genson genson = new GensonBuilder().useConstructorWithArguments(true).create();
		HashMap<Book, Integer> rs = new HashMap<Book, Integer>();
		ProductInfo prod = new ProductInfo();
		try {
			rs = Sys.getInstance().retrieveRestBook(bid);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Entry<Book, Integer> mapElement : rs.entrySet()) { 
            String name = ((Book)mapElement.getKey()).getTitle();
            String genre = ((Book)mapElement.getKey()).getGenre();
            
            prod.setGenre(genre);
            prod.setTitle(name);
  
//            System.out.println(key + " : " + value); 
        }
		String json = genson.serialize(prod);
		return json;

	}

}
