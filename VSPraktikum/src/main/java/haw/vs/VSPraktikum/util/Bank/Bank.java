package haw.vs.VSPraktikum.util.Bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Eine Bank verwaltet seine Accounts und Transactions
 * @author Marc
 *
 */
public class Bank {
	/**
	 * AccountNumber -> Account
	 */
	private Map<Integer, Account> accountMap = new HashMap<>();
	/**
	 * TID -> Transaction
	 */
	private Map<Integer, Transaction> transactionMap = new HashMap<>();
	/**
	 * int -> Transfer
	 */
	private Map<Integer, Transfer> transferMap = new HashMap<>();
	/**
	 * unique banknumber
	 */
	private int bankNumber;
	
	private int accountNumberCounter = 0;
	private int transferNumberCounter = 0;
	private String gameURI;	// /games/42
//	private String bankURI;
//	private String playerURI;
	
	public Bank(int bankNumber, String gameURI) { //, String playerURI, String bankURI) {
		this.bankNumber = bankNumber;
		this.gameURI = gameURI;
//		this.bankURI = bankURI;
//		this.playerURI = playerURI;
	}
	
	/**
	 * Die Bank erzeugt ein neues Konto und verwaltet dies
	 * @param saldo
	 * @throws NumberFormatException, wenn "saldo" ungueltig
	 * @return
	 */
	public int createAccount(String saldoS, String player) throws NumberFormatException {
		int saldo = Integer.parseInt(saldoS);
		Account account = new Account(accountNumberCounter++, saldo, player); //, bankURI, playerURI);
		accountMap.put(account.getAccountNumber(), account);
		return account.getAccountNumber();
	}
	
	/**
	 * Gibt den Saldo eines Account zurueck
	 * @param accNumber
	 * @throws NumberFormatException, wenn "accNumber" ungueltig
	 * @return
	 */
	public String getAccountSaldo(String accNumberS) throws NumberFormatException {
		int accNumber = Integer.parseInt(accNumberS);
		return String.valueOf(accountMap.get(accNumber).getSaldo());
	}
	
	public String getAccountPlayer(String accNumberS) throws NumberFormatException {
		int accNumber = Integer.parseInt(accNumberS);
		return String.valueOf(accountMap.get(accNumber).getPlayer());
	}
	
	public List<String> getAccountURIs() {
		List<String> res = new ArrayList<>();
		for(Account acc : accountMap.values()) {
			res.add(getSubURI() + "/accounts/" + acc.getAccountNumber());
		}
		return res;
	}
	
	/**
	 * Gibt die Banknummer zurueck
	 * @return
	 */
	public String getBankNumber() {
		return String.valueOf(bankNumber);
	}
	
	public String getGameURI() {
		return gameURI;
	}
	
	/**
	 * Gibt die (Sub-) URI zurueck
	 * Bsp. /banks/43
	 * @return
	 */
	public String getSubURI() {
		return "/banks/" + this.getBankNumber();
	}
	
	/**
	 * Gibt alle Transfers zurueck
	 * @throws NumberFormatException, wenn "idS" ungueltig
	 * @return
	 */
	public List<Transfer> getTransfers() {
		return new ArrayList<>(transferMap.values());
	}
	
	/**
	 * 
	 * @param idS
	 * @return
	 * @throws NumberFormatException
	 */
	public Transfer getTransfer(String idS) throws NumberFormatException {
		int id = Integer.parseInt(idS);
		
		return transferMap.get(id);
	}
	
	public Transaction getTransaction(String tidS) throws NumberFormatException {
		int tid = Integer.parseInt(tidS);
		
		return transactionMap.get(tid);
	}
 	
//	/**
//	 * Ueberweist einen Geldbetrag an ein Account
//	 * @param to, Accountnummer als String
//	 * @param amountS, Wert als String
//	 * @throws NumberFormatException, wenn "amountS" oder "toS" ungueltig
//	 * @return
//	 */
//	public boolean addAmount(String toS, String amountS) throws NumberFormatException {
//		int amountI = Integer.parseInt(amountS);
//		int to = Integer.parseInt(toS);
//		
//		if(accountMap.containsKey(to)) {
//			Account acc = accountMap.get(to);
//			Account bankAcc = new Account(accountNumberCounter++);
//			Transfer transfer = new Transfer(bankAcc, acc, amountI);
//			transferMap.put(transferNumberCounter++, transfer);
//			return transfer.run();
//		}
//		return false;
//	}
//	
//	/**
//	 * Zieht einen Geldbetrag von einem Account ab (sofern ausreichender Saldo)
//	 * @param from, Accountnummer als String
//	 * @param amountS, Wert als String
//	 * @throws NumberFormatException, wenn "amountS" oder "fromS" ungueltig
//	 * @return
//	 */
//	public boolean subtractAmount(String fromS, String amountS) throws NumberFormatException {
//		int amountI = Integer.parseInt(amountS);
//		int from = Integer.parseInt(fromS);
//		
//		if(accountMap.containsKey(from)) {
//			Account acc = accountMap.get(from);
//			Account bankAcc = new Account(accountNumberCounter++);
//			Transfer transfer = new Transfer(acc, bankAcc, amountI);
//			transferMap.put(transferNumberCounter++, transfer);
//			return transfer.run();
//		}
//		return false;
//	}
	
	/**
	 * Ueberweist einen Geldbetrag von einem Account zum Anderen (sofern ausreichender Saldo)
	 * @param from, Accountnummer als String
	 * @param to, Accountnummer als String
	 * @param amountS, Wert als String
	 * @throws NumberFormatException, wenn "amountS", "fromS" oder "toS" ungueltig
	 * @return
	 */
	public int transfer(String fromS, String toS, String amountS, String reason) throws NumberFormatException, IllegalArgumentException {
		int amount = Integer.parseInt(amountS);
		int from = Integer.parseInt(fromS);
		int to = Integer.parseInt(toS);
		
		if(to == from) {
			throw new IllegalArgumentException("angegebene Accounts sind identisch");
		}
		Transfer transfer = null;
		if(from == 0 && accountMap.containsKey(to)) {
			Account bankAcc = new Account(accountNumberCounter++);
			Account accTo = accountMap.get(to);
			transfer = new Transfer(bankAcc, accTo, amount, reason, transferNumberCounter++);
		} else if(to == 0 && accountMap.containsKey(from)) {
			Account bankAcc = new Account(accountNumberCounter++);
			Account fromAcc = accountMap.get(from);
			transfer = new Transfer(fromAcc, bankAcc, amount, reason, transferNumberCounter++);
			transferMap.put(transfer.getID(), transfer);
		}
		if(accountMap.containsKey(from) && accountMap.containsKey(to)) {
			Account accFrom = accountMap.get(from);
			Account accTo = accountMap.get(to);
			transfer = new Transfer(accFrom, accTo, amount, reason, transferNumberCounter++);
			transferMap.put(transfer.getID(), transfer);
		}
		if(transfer.run()) {
			return transfer.getID();
		}
		throw new IllegalArgumentException("Transfer konnte nicht ausgefuehrt werden");
	}
	
	/**
	 * Erzeugt eine Transaction und verwaltet diese
	 * @param tid, Transaction ID
	 * @throws NumberFormatException, wenn "tid" ungueltig
	 */
	private Transaction createTransaction(String tidS) throws NumberFormatException {
		int tid = Integer.parseInt(tidS);
		
		if(!transactionMap.containsKey(tid)) {
			Transaction transaction = new Transaction(tid);
			transactionMap.put(tid, transaction);
		}
		return transactionMap.get(tid);
	}
	
	/**
	 * Fuegt einen Transfer zu einer Transaction hinzu
	 * Transfer wird an dieser Stelle nicht ausgefuehrt
	 * Wenn Accountnumber gleich 0 -> wird als Bank interpretiert
	 * @param tid, Transaction ID
	 * @param from, Accountnumber
	 * @param to, Accountnumber
	 * @param amountS
	 * @throws NumberFormatException, wenn "tidS", "fromS", "toS" oder "amountS" ungueltig
	 * @return 
	 */
	public int addTransferToTransaction(String tidS, String fromS, String toS, String amountS, String reason) throws NumberFormatException, IllegalArgumentException {
		int amount = Integer.parseInt(amountS);
		int tid = Integer.parseInt(tidS);
		int from = Integer.parseInt(fromS);
		int to = Integer.parseInt(toS);
		Transaction transaction = null;
		Transfer transfer = null;
		
		if(from == to) {
			throw new IllegalArgumentException("angegebene Accounts sind identisch");
		}
		if(!transactionMap.containsKey(tid)) {
//			throw new IllegalArgumentException("Transaction ID nicht gefunden");
			transaction = createTransaction(tidS);
		} else {
			transaction = transactionMap.get(tid);
		}
		if(from == 0 && accountMap.containsKey(to)) {
			Account bankAcc = new Account(accountNumberCounter++);
			Account accTo = accountMap.get(to);
			transfer = new Transfer(bankAcc, accTo, amount, reason, transferNumberCounter++);
			transferMap.put(transfer.getID(), transfer);
			transaction.addTransfer(transfer);
		} else if(to == 0 && accountMap.containsKey(from)) {
			Account bankAcc = new Account(accountNumberCounter++);
			Account fromAcc = accountMap.get(from);
			transfer = new Transfer(fromAcc, bankAcc, amount, reason, transferNumberCounter++);
			transferMap.put(transfer.getID(), transfer);
			transaction.addTransfer(transfer);
		}
		if(accountMap.containsKey(from) && accountMap.containsKey(to)) {
			Account accFrom = accountMap.get(from);
			Account accTo = accountMap.get(to);
			transfer = new Transfer(accFrom, accTo, amount, reason, transferNumberCounter++);
			transferMap.put(transfer.getID(), transfer);
			transaction.addTransfer(transfer);
		}
		return transfer.getID();
	}
	
	/**
	 * Fuehrt alle einzelnen Transfers einer Transaction aus
	 * @param tidS
	 */
	public void commitTransaction(String tidS) throws NumberFormatException, IllegalArgumentException {
		int tid = Integer.parseInt(tidS);
		
		if(transactionMap.containsKey(tid)) {
			Transaction transaction = transactionMap.get(tid);
			if(transaction.commit()) {
				transactionMap.remove(tid);
			} else {
				throw new IllegalArgumentException("Transaction konnte nicht vollstaendig ausgefuehrt werden");
			}
		}
	}
	
	/**
	 * Entfernt eine Transaction, mitsamt aller enthaltenen Transfers
	 * @param tid, Transaction ID
	 */
	public void deleteTransaction(String tidS) throws NumberFormatException {
		int tid = Integer.parseInt(tidS);
		transactionMap.remove(tid);
	}
}
