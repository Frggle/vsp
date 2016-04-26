package haw.vs.VSPraktikum.services;

import static spark.Spark.get;
import static haw.vs.VSPraktikum.util.YellowService.*;
import java.util.concurrent.ThreadLocalRandom;
import com.google.gson.Gson;
import com.google.gson.JsonElement;


public class Dice {
	
	public static void main(String[] args) {
		registerService("DiceService", "Gives you a single dice roll", "dice", "/1337");
		
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
