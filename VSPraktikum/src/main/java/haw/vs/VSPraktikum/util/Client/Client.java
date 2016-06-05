package haw.vs.VSPraktikum.util.Client;

import haw.vs.VSPraktikum.gui.ClientUI;
import haw.vs.VSPraktikum.services.EventServiceProvider;
import haw.vs.VSPraktikum.util.YellowpagesData;

public class Client {
	
	private static String GAME_SERVICE_ID = "";
	private static String BOARD_SERVICE_ID = "";
	private static String BANK_SERVICE_ID = "";

	private YellowpagesData gameService = EventServiceProvider.getService(GAME_SERVICE_ID);
	private YellowpagesData boardService = EventServiceProvider.getService(BOARD_SERVICE_ID);
	private YellowpagesData bankService = EventServiceProvider.getService(BANK_SERVICE_ID);
	
	public Client() {
		
	}
	
	public static void main(String[] args) {
		new ClientUI().addElementToGamesList("bar");;
	}
	
}
