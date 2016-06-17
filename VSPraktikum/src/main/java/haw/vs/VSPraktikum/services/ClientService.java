package haw.vs.VSPraktikum.services;

import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
import static spark.Spark.get;
import static spark.Spark.post;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

public class ClientService {
	
	private static String URI;
	
	private static Map<String, String> clientMap = new HashMap<>();
	
	public static void main(String[] args) {
		
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
		} catch(UnknownHostException e) {
		}
		
		registerService("jenny_marc_vsp_client", "holds IPs of clients", "/client", URI + "/client");
		
		/** als QueryParam wird die PlayerURI angegeben **/
		get("/client", (req, res) -> {
			String playerURI = req.queryParams("player");
			
			if(playerURI != null) {
				JSONObject jsn = new JSONObject();
				if(clientMap.containsKey(playerURI)) {
					jsn.put("uri", clientMap.get(playerURI) + "/client/turn");
					return jsn;	
				} else {
					res.status(HttpStatus.NO_CONTENT_204);
					return "Client isn't registered";
				}
			} else {
				res.status(HttpStatus.BAD_REQUEST_400);
				return clientMap.toString();
			}
		});
		
		post("/client", (req, res) -> {
			if(req.body() != null) {
				JSONObject jsnBody = new JSONObject(req.body());
				String clientIP = jsnBody.getString("uri");
				String player = jsnBody.getString("player");
				clientMap.put(player, clientIP);
				res.status(HttpStatus.CREATED_201);
				return "client registered";
			} else {
				res.status(HttpStatus.BAD_REQUEST_400);
				return "client not registered";
			}
		});
	}
}
