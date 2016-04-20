package haw.vs.VSPraktikum.services;

import static spark.Spark.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import haw.vs.VSPraktikum.util.User;
import org.eclipse.jetty.http.HttpStatus;

public class Users {
	private static Map<String, User> usersMap = new HashMap<>(); // ID -> User
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		
		get("/users", (request, response) -> {
			response.status(HttpStatus.OK_200);
			response.type("application/json");
			List<String> uriList = new ArrayList<>();
			for(User user : usersMap.values()) {
				uriList.add(user.getUri());
			}
			return gson.toJson(uriList);
		});
		
		post("/users", (request, response) -> {
			response.status(HttpStatus.OK_200);

			User user = gson.fromJson(request.body(), User.class);
			
			if(isValid(user)) {
				response.status(HttpStatus.OK_200);
				usersMap.put(user.getId(), user);	// update user in map
			} else {
				response.status(HttpStatus.BAD_REQUEST_400);
			}
			return "";
		});
		
		get("/users/:id", (request, response) -> {
			String playername = "users/" + request.params(":id");
			
			if(!usersMap.containsKey(playername)) {
				response.status(HttpStatus.NOT_FOUND_404);
				return "";				
			} 
			
			User user = usersMap.get(playername);
			response.status(HttpStatus.OK_200);
			response.type("application/json");
			
			return gson.toJson(user.userInfo());
		});
		
		put("users/:id", (request, response) -> {
			String playername = "users/" + request.params(":id");
			
			String body = request.body();
			JsonElement jsonElem = gson.fromJson(body, JsonElement.class);
			JsonObject jsonOb = jsonElem.getAsJsonObject();
			
			if(usersMap.containsKey(playername)) {
				response.status(HttpStatus.OK_200);
				User user = usersMap.get(playername);
				user.setName(jsonOb.get("name").getAsString());
				user.setUri(jsonOb.get("uri").getAsString());
				usersMap.put(playername, user);
			} else {
				response.status(HttpStatus.NOT_FOUND_404);
			}
			return "";
		});
		
		delete("/users/:id", (request, response) -> {
			String playername = "users/" + request.params(":id");
			
			if(usersMap.containsKey(playername)) {
				usersMap.remove(playername);
				response.status(HttpStatus.OK_200);
			} else {
				response.status(HttpStatus.NOT_FOUND_404);
			}
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
