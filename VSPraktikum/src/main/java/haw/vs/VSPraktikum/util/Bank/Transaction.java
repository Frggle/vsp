package haw.vs.VSPraktikum.util.Bank;

public class Transaction {
	String id; // URI
	String game;	// URI des Spiels, wo das Event zugehoert
	String type;	// internal type of the event (e.g bank transfer, rent, got to jail, estate transfer)
	String name;	// human readable name for this event
	String reason;	// a description why this event occured
	String resource;	// URI of the resource related to this event where more information my be found (e.g. an URI to a transfer or similar)
	String player; 	// URI of the player having triggered the event
	
	public Transaction() {
		id = null;
		game = null;
		type = null;
		name = null;
	}
}
