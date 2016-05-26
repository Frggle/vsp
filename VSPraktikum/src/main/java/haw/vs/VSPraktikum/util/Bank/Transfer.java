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
	
	public Transfer(Account from, Account to, int amount) {
		this.accFrom = from;
		this.accTo = to;
		this.amount = amount;
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
}
