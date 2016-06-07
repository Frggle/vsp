package haw.vs.VSPraktikum.util.Client;

import java.util.ArrayList;
import java.util.List;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import haw.vs.VSPraktikum.Config;
import haw.vs.VSPraktikum.gui.ClientUI;
import haw.vs.VSPraktikum.services.ServiceProvider;
import haw.vs.VSPraktikum.util.YellowpagesData;

public class Client {
	
	private YellowpagesData gameService = ServiceProvider.getService(Config.GAME_SERVICE);
	private YellowpagesData boardService = ServiceProvider.getService(Config.BOARD_SERVICE);
	private YellowpagesData bankService = ServiceProvider.getService(Config.BANK_SERVICE);
	
	private String gameID;	// Spiel Nummer
	private String pawnID;	// Spielfigur Nummer
	
	public Client() {
		
	}
	
	public static void main(String[] args) {
		ClientUI clientUI = ClientUI.getInstance();
		clientUI.execute();
		
		List<String> list = new ArrayList<>();
		list.add("foo");
		list.add("bar");
		
//		clientUI.updateGamesList(list);
		
	}
	
	/**
	 * Würfeln
	 * @return
	 */
	public boolean roll() {
		try {
			HttpResponse<JsonNode> response = Unirest.post(boardService.getUri() + "/" + gameID + "/pawns/" + pawnID + "/roll").asJson();
			return response.getStatus() == 200 ? true : false;
		} catch(UnirestException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 */
}
