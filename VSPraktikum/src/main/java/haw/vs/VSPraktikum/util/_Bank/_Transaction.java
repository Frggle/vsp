package haw.vs.VSPraktikum.util._Bank;

public class _Transaction {
	String uri; // URI
	String bankuri;	// URI der Bank, wo das Event zugehoert
	String reason;	// a description why this event occured
	String player; 	// URI of the player having triggered the event
	
	public _Transaction(int id, String bankuri, String reason, String player) {
		this.uri = "transaction/" + id;
		this.bankuri = bankuri;
		this.reason = reason;
		this.player = player;
	}
	
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	
	/**
	 * @return the game
	 */
	public String getBank() {
		return bankuri;
	}
	
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * @return the player
	 */
	public String getPlayer() {
		return player;
	}
}
