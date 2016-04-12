package haw.vs.VSPraktikum;
import static spark.Spark.*;
import org.json.JSONObject;
import com.mashape.unirest.http.Unirest;
import haw.vs.VSPraktikum.services.Dice;

public class App {
	private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services";
	
	public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
		get("/dice", Dice::roll);
		
		try{
			JSONObject json = new JSONObject();
			json.put("name", "DiceService");
			json.put("description", "Gives you a single dice roll");
			json.put("service", "dice");
			json.put("uri", "http://apb154_docker_0:4567/dice"); // TODO: URI richtig ???
			
			System.err.println(json);
			
			String result = Unirest.put(YELLOW_PAGES + "/1")
					.header("Content-Type", "application/json")
					.body(json)
					.asString().getBody();	
			
			System.err.println("Result: " + result);
		} catch (Exception e) {
			//
		}
		

    }
}
