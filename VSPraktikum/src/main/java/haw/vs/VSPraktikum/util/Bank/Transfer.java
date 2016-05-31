package haw.vs.VSPraktikum.util.Bank;

/**
 * Repraesentiert einen einzelnen Transfer
 * Dient als Speicher
 * Entweder zwischen:
 * 	Player <-> Player
 * 	Bank <-> Player
 * 	Player <-> Bank 
 * @author Marc
 *
 */
public class Transfer {
	private Account accFrom;
	private Account accTo;
	private int amount;
	private String reason;
	private int id;
	
	public Transfer(Account from, Account to, int amount, String reason, int id) {
		this.accFrom = from;
		this.accTo = to;
		this.amount = amount;
		this.reason = reason;
		this.id = id;
	}

	/**
	 * Prueft ob der Transfer durchgefuehrt werden kann, ohne das ein Dispo entsteht
	 */
	public boolean checkIfValid() {
		return accFrom.getSaldo() - this.amount < 0 ? false : true;
	}
	
	/**
	 * Fuehrt den Transfer durch
	 */
	public boolean run() {
		if(checkIfValid()) {
			if(accFrom.subtractAmount(amount)) {
				accTo.addAmount(amount);
				return true;
			}
		} 
		return false;
	}
	
	/**
	 * @return the accFrom
	 */
	public Account getAccFrom() {
		return accFrom;
	}

	/**
	 * @return the accTo
	 */
	public Account getAccTo() {
		return accTo;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * @return the id
	 */
	public int getID() {
		return id;
	}
}
