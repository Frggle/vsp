package haw.vs.VSPraktikum.services;

import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
import static spark.Spark.get;
import static spark.Spark.post;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import haw.vs.VSPraktikum.util.YellowpagesData;
import haw.vs.VSPraktikum.util.Client.Client;

public class ClientService {
	private static String GAME_SERVICE_ID = "foo";
	private static String BOARD_SERVICE_ID = "foo";
	private static Client client;
	
	public static void main(String[] args) {
//		client = new Client("2", "marc");
		client = new Client(args[0], args[1]);
		registerService("jenny_marc_vsp_client", "represent a human client", "client", "http://172.18.0.73:4567/client");
		
		YellowpagesData gameService = EventServiceProvider.getService(GAME_SERVICE_ID);
		/* wuerfeln (post), aktiven Games (get), aktiven Spieler (get) */
		YellowpagesData boardService = EventServiceProvider.getService(BOARD_SERVICE_ID);
		
		JSONObject results = new JSONObject();
		results.put("id", client.getId());
		results.put("name", client.getName());
		System.err.println(results.toString());
		
		get("/client", (request, response) -> {
			response.status(HttpStatus.OK_200);
			response.type("application/json");
			JSONObject result = new JSONObject();
			result.put("id", client.getId());
			result.put("name", client.getName());
			if(!client.getUri().equals("")) {
				result.put("uri", client.getUri());
			}
			response.body(result.toString());
			return "";
		});
		
		post("/client/turn", (request, response) -> {
			JSONObject json = new JSONObject(request.body());
			
			if(json.getString("player").equals(client.getName())) {
				// TODO: inform GUI, it's my turn
			} else {
				// TODO: inform GUI, it's the turn of ..
			}
			return "";
		});
		
		// TODO: GUI
		// TODO: alle aktuellen (offenen) Spiele anzeigen
		// TODO: einem Spiel beitreten
		// TODO: würfeln
	}
}
