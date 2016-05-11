package haw.vs.VSPraktikum.util.Bank;

public class Account {
	String player; // URI: '/games/42/players/mario'
	int saldo;	// Guthaben
	String bankuri; // URI von Bank

	public Account(String player, String bankuri, int saldo) {
		this.player = player;
		this.bankuri = bankuri;
		this.saldo = saldo;
	}
	
	public String getPlayerURI() {
		return player;
	}
	
	public int getSaldo() {
		return saldo;
	}
	
	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}
	
	// Bank zahlt auf PlayerKonto ein
	public void fromBank(int amount) {
		this.saldo += amount;
	}

	// Player zahlt in Bank ein
	public boolean toBank(int amount) {
		if((this.saldo - amount) < 0) {
			return false;
		} else {
			this.saldo -= amount;
			return true;
		}
	}
	
	public String getBankURI() {
		return bankuri;
	}
	
	public void setBankURI(String bankuri) {
		this.bankuri = bankuri;
	}
}
