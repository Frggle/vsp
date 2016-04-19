package haw.vs.VSPraktikum.services;

import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import haw.vs.VSPraktikum.util.User;

public class Users {
	private static Map<String, User> usersMap = new HashMap<>();
	private static final int INVALID_VALUES = 400; // Bad request
	private static final int RESOURCE_NOT_FOUND = 404; // Resource not found
	private static final int OK = 200;
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		
		get("/users", (request, response) -> {
			response.status(OK);
			response.type("application/json");
			return gson.toJson(usersMap.keySet());
		});
		
		post("/users", (request, response) -> {
			User user = gson.fromJson(request.body(), User.class);
			
			if(isValid(user)) {
				usersMap.put(user.getUri(), user);	// update user in map
			} else {
				response.status(INVALID_VALUES);
			}
			return "";
		});
		
		get("/users/{id}", (request, response) -> {
			String id = request.queryParams("id");
			
			for(User user : usersMap.values()) {
				if(user.getId().equals(id)) {
					response.status(OK);
					response.type("application/json");			
					
					String res = "{\"id\":" + "\"" + user.getId() + "\""
							+ ",\"name\":" + "\"" + user.getName() + "\"" 
							+ ",\"uri\":" + "\"" + user.getUri() + "\"}";
					return gson.toJson(res);
				}
			}
			response.status(RESOURCE_NOT_FOUND);
			return "";
		});
	}
	
	/**
	 * Prueft ob neuer User gueltige Werte besitzt
	 * @param user
	 * @return
	 */
	private static boolean isValid(User user) {
		if(user.getName() == null || user.getName() == "") {
			return false;
		} else if(user.getUri() == null || user.getUri() == "") {
			return false;
		}
		return true;
	}
}
