package haw.vs.VSPraktikum.services;

import static spark.Spark.get;
import java.util.concurrent.ThreadLocalRandom;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mashape.unirest.http.Unirest;

public class Dice {
	
	private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services";
	
	public static void main(String[] args) {
		try {
			JSONObject json = new JSONObject();
			json.put("name", "DiceService");
			json.put("description", "Gives you a single dice roll");
			json.put("service", "dice");
			json.put("uri", "http://abp154_docker_0:4567/dice");
			
			System.err.println(json);
		
			Unirest.post(YELLOW_PAGES + "/1337").header("Content-Type", "application/json").body(json).asString().getBody();
		} catch(Exception e) {
			//
		}
		
		get("/dice", (request, response) -> {
			response.status(200);
			response.type("application/json");
			
			Gson gson = new Gson();
			
			String player = request.params("player");
			String game = request.params("game");
			
			Integer randomNumber = (Integer)ThreadLocalRandom.current().nextInt(1, 7);
			
			String output = "{\"number\": " + randomNumber + "}";
			JsonElement jsonElem = gson.fromJson(output, JsonElement.class);
			return jsonElem.getAsJsonObject();
		});
	};
}
