//package haw.vs.VSPraktikum.services;
//
//import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
//import static spark.Spark.get;
//import static spark.Spark.post;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.eclipse.jetty.http.HttpStatus;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;
//
//import haw.vs.VSPraktikum.util.YellowpagesData;
//import haw.vs.VSPraktikum.util.Client.Client;
//
//public class _ClientService {
//	private static String GAME_SERVICE_ID = "foo";
//	private static String BOARD_SERVICE_ID = "foo";
//	private static String BANK_SERVICE_ID = "bar";
//
//	private YellowpagesData gameService = EventServiceProvider.getService(GAME_SERVICE_ID);
//	private YellowpagesData boardService = EventServiceProvider.getService(BOARD_SERVICE_ID);
//	private YellowpagesData bankService = EventServiceProvider.getService(BANK_SERVICE_ID);
//
//	public static void main(String[] args) {
//		registerService("jenny_marc_vsp_client", "represent a human client", "client", "http://172.18.0.73:4567/client");
//
//		JSONObject results = new JSONObject();
//		results.put("id", client.getId());
//		results.put("name", client.getName());
//		System.err.println(results.toString());
//
//		get("/client", (request, response) -> {
//			response.status(HttpStatus.OK_200);
//			response.type("application/json");
//			JSONObject result = new JSONObject();
//			result.put("id", client.getId());
//			result.put("name", client.getName());
//			if(!client.getUri().equals("")) {
//				result.put("uri", client.getUri());
//			}
//			response.body(result.toString());
//			return "";
//		});
//
//		post("/client/turn", (request, response) -> {
//			JSONObject json = new JSONObject(request.body());
//
//			if(json.getString("player").equals(client.getName())) {
//				// TODO: inform GUI, it's my turn
//			} else {
//				// TODO: inform GUI, it's the turn of ..
//			}
//			return "";
//		});
//		// TODO: GUI
//
//	}
//
//	/*
//	 * Zeigt alle aktuellen (offenen) Spiele an
//	 * @return Liste der offenen Spiele
//	 * */
//	public List<String> getActiveGames(){
//		List<String> idList = new ArrayList<>();
//		HttpResponse<JsonNode> response = null;
//		try {
//			response = Unirest.get(gameService.getUri() + "/games").asJson();
//		} catch (UnirestException e) {}
//		JSONArray array = response.getBody().getArray();
//		array.forEach((string) ->{
//			JSONObject json = new JSONObject(string);
//			idList.add(json.getString("id"));
//		});
//
//		return idList;
//	}
//	/*
//	 * würfeln
//	 *
//	 * */
//	public void rollDice(String gameId){
//		Unirest.post(boardService.getUri() + "/boards/" + gameId + "/pawns/" + client.getName() + "/roll");
//	}
//
//
//	// TODO: einem Spiel beitreten
//	public void registerPlayer(String gameId){
//		//Bankuri
//		HttpResponse<JsonNode> response = null;
//		try {
//			response = Unirest.get(gameService.getUri() + "/games/" + gameId + "/services").asJson();
//		} catch (UnirestException e2) {}
//
//		JSONObject bankJson = response.getBody().getObject();
//		String bankUri = bankJson.getString("bank");
//		//Accounterstellen mit Hilfe von Bankuri
//		JSONObject accountJson = new JSONObject();
//		accountJson.put("player", client.getName());
//		accountJson.put("saldo", "0");
//		try {
//			Unirest.post(bankService.getUri() + "/banks/" + bankUri + "/players").header("Content-Type", "application/json").body(accountJson).asString();
//		} catch (UnirestException e1) {};
//
//
//
//		JSONObject json = new JSONObject();
//		json.put("id", client.getId());
//		json.put("user", client.getName());
//		json.put("pawn", client.getName());
//		json.put("account", "/banks/" + bankUri + "/players/" + client.getName());
//		json.put("ready", "true");
//		try {
//			Unirest.post(gameService.getUri() + "/games/" + gameId + "/players").header("Content-Type", "application/json").body(json).asString();
//		} catch (UnirestException e) {}
//
//	}
//}
