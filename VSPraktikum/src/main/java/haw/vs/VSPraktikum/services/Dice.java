package haw.vs.VSPraktikum.services;

import java.util.concurrent.ThreadLocalRandom;
import spark.Request;
import spark.Response;

public class Dice {

	public static Integer roll(Request request, Response response) {
		
		response.status(200);
		response.type("application/json");
		
		String player = request.queryParams("player");
		String game = request.queryParams("game"); 
		
		try{
			
		} catch(Exception e) {
			//
		}
		
		return (Integer) ThreadLocalRandom.current().nextInt(1, 7);
	}
}
