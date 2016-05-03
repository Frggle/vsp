package haw.vs.VSPraktikum.util.Bank;

public class Account {
	String playerURI; // URI
	int saldo;	// Guthaben
	String gameid; // URI vom Game

	public Account(String playerURI, String gameID, int saldo) {
		this.playerURI = playerURI;
		this.gameid = gameID;
		this.saldo = saldo;
	}
	
	public String getPlayerURI() {
		return playerURI;
	}
	
	public int getSaldo() {
		return saldo;
	}
	
	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}
	
	public String getGameID() {
		return gameid;
	}
	
	public void setGameID(String gameid) {
		this.gameid = gameid;
	}
}
