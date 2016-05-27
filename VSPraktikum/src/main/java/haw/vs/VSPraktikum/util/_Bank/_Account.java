package haw.vs.VSPraktikum.util._Bank;

public class _Account {
	private String player; // URI: '/games/42/players/mario'
	private int saldo;	// Guthaben
	private String bankuri; // URI von Bank
	private String accountNumber; // int. Nummer vom Konto
	private String accountID;	// /accounts/<accountNumber>

	public _Account(String player, String bankuri, int saldo, int accountNumber) {
		this.player = player;
		this.bankuri = bankuri;
		this.saldo = saldo;
		this.accountNumber = String.valueOf(accountNumber);
		this.accountID = "/accounts/" + this.accountNumber;
	}

	public String getPlayerURI() {
		return player;
	}

	public String getSaldo() {
		return String.valueOf(saldo);
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

	public void setBankURI(String bankNumber) {
		this.bankuri = "/banks/" + bankNumber;
	}

	public String getAccountNumber(){
		return this.accountNumber;
	}

	public String getAccountID() {
		return this.accountID;
	}
}
