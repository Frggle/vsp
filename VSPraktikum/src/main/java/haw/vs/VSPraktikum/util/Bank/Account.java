package haw.vs.VSPraktikum.util.Bank;

public class Account {
	/**
	 * unique accountnumber
	 */
	private String accountNumber;
	/**
	 * saldo of a player
	 */
	private int saldo;
	/**
	 * URIs of different resources
	 */
//	private String bankURI;
//	private String playerURI;
//	private String accountURI;
	
	public Account(int accountNumber, int saldo) {//, String bankURI, String playerURI) {
		this.accountNumber = String.valueOf(accountNumber);
		this.saldo = saldo;
//		this.bankURI = bankURI;
//		this.playerURI = playerURI;
	}
	
	public Account(int accountNumber) {
		this.accountNumber = String.valueOf(accountNumber);
		this.saldo = Integer.MAX_VALUE / 2;
	}
	
	public void addAmount(int amount) {
		this.saldo += amount;
	}
	
	public boolean subtractAmount(int amount) {
		if((saldo - amount) < 0) {
			return false;
		} else {
			saldo -= amount;
			return true;
		}
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public int getSaldo() {
		return saldo;
	}
}
