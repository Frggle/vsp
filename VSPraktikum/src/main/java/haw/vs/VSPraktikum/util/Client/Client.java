package haw.vs.VSPraktikum.util.Client;

import java.util.ArrayList;
import java.util.List;
import haw.vs.VSPraktikum.gui.ClientUI;

public class Client {
	
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
}
