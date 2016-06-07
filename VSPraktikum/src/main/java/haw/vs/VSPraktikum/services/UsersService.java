package haw.vs.VSPraktikum.services;

import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import haw.vs.VSPraktikum.Config;
import haw.vs.VSPraktikum.util.User;

/**
 * Ein Service bei dem sich Benutzer registrieren können.
 * @author Marc
 *
 */
public class UsersService {
	/*
	 * alle registrierten Benutzer
	 * Abbildung von Name auf User Objekt
	 */
	private static Map<String, User> usersMap = new HashMap<>();
	
	private static String URI;

	public static void main(String[] args) {
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
		} catch(UnknownHostException e) {}
		
		Gson gson = new Gson();

		registerService("jenny_marc_vsp_users", "The users service registers users of the system", "users", Config.USERS_DOCKER_URI);

		get("/users", (request, response) -> {
			response.status(HttpStatus.OK_200);
			response.type("application/json");
			List<String> idList = new ArrayList<>();
			for(User user : usersMap.values()) {
				idList.add(user.getID());
			}
			return gson.toJson(idList);
		});

		post("/users", (request, response) -> {
			response.status(HttpStatus.OK_200);

			User user = gson.fromJson(request.body(), User.class);

			if(isValid(user)) {
				response.status(HttpStatus.OK_200);
				usersMap.put(user.getName(), user);	// update user in map
				response.header(HttpHeader.LOCATION.asString(), URI + user.getID());
			} else {
				response.status(HttpStatus.BAD_REQUEST_400);
			}
			return "";
		});

		get("/users/:name", (request, response) -> {
			String playername = request.params(":name");

			if(!usersMap.containsKey(playername)) {
				response.status(HttpStatus.NOT_FOUND_404);
				return "";
			}

			User user = usersMap.get(playername);
			response.status(HttpStatus.OK_200);
			response.type("application/json");

			return gson.toJson(user.userInfo());
		});

		put("/users/:name", (request, response) -> {
			String playername = request.params(":name");

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

		delete("/users/:name", (request, response) -> {
			String playername = request.params(":name");

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
