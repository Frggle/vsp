package haw.vs.VSPraktikum.services;

import static spark.Spark.get;

import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import spark.Request;
import spark.Response;

public class Dice {
	
	private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services";
	
	public static void main(String[] args) {
		try{
			JSONObject json = new JSONObject();
			json.put("name", "DiceService");
			json.put("description", "Gives you a single dice roll");
			json.put("service", "dice");
			json.put("uri", "http://abp154_docker_0:4567/dice"); // TODO: URI richtig ???
			
			System.err.println(json);
			
			String result = Unirest.post(YELLOW_PAGES + "/1337")
					.header("Content-Type", "application/json")
					.body(json)
					.asString().getBody();	
			
			get("/dice", Dice::roll);
			System.err.println("Result: " + result);
		} catch (Exception e) {
			//
		}
    }
	
	public static String roll(Request request, Response response) {
		
		response.status(200);
		response.type("application/json");
		
		String player = request.queryParams("player");
		String game = request.queryParams("game");
		
		Integer randomNumber = (Integer) ThreadLocalRandom.current().nextInt(1, 7);
		String output = "{\"number\": " + randomNumber + "}";
		return output;
	}
}
