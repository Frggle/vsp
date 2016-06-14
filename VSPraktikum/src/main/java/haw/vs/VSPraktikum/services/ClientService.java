package haw.vs.VSPraktikum.services;

import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
import static spark.Spark.get;
import static spark.Spark.post;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

public class ClientService {
	
	private static String clientIP = null;
	private static String URI;
	
	public static void main(String[] args) {
		
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
		} catch(UnknownHostException e) {
		}
		
		registerService("jenny_marc_vsp_client", "holds IP of client", "/client", URI + "/client");
		
		get("/client", (req, res) -> {
			if(clientIP != null) {
				JSONObject jsn = new JSONObject();
				jsn.put("uri", clientIP + "/client/turn");
				return jsn;
			} else {
				res.status(HttpStatus.NO_CONTENT_204);
				return "Client isn't registered";
			}
			
		});
		
		post("/client", (req, res) -> {
			if(req.body() != null) {
				JSONObject jsnBody = new JSONObject(req.body());
				clientIP = jsnBody.getString("uri");
			}
			return "client registered";
		});
	}
}
