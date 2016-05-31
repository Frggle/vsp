package haw.vs.VSPraktikum.util.Bank;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
	private int tid;
	private List<Transfer> transferList;
	private State state;
//	private String baseURI;
	
	public Transaction(int tid) { //, String baseURI) {
		transferList = new ArrayList<>();
		this.tid = tid;
		this.state = State.READY;
//		this.baseURI = baseURI;
	}
	
	public void addTransfer(Transfer transfer) {
		transferList.add(transfer);
	}
	
	/**
	 * Fuehrt jeden Transfer durch, veraendert jedoch nicht die Kontostaende
	 * @return boolean ob alle Transfer gueltig
	 */
	public boolean checkAllTransfers() {
		boolean res = true;
		for(Transfer transfer : transferList) {
			res = res && transfer.checkIfValid();
		}
		return res;
	}
	
	/**
	 * Fuehrt jeden Transfer durch UND veraendert die Kontostaende
	 * @return
	 */
	public boolean commit() {
		boolean res = true;
		for(Transfer transfer : transferList) {
			res = res && transfer.run();
		}
		state = State.COMMITED;
		return res;
	}
	
	/**
	 * Gibt die Transaction ID zurueck
	 * @return
	 */
	public int getTID() {
		return tid;
	}
	
	public String getStatus() {
		return state.toString();
	}
	
//	/**
//	 * Gibt die Basis URI zurueck (ohne spezifische Ressource der Transaction)
//	 * @return
//	 */
//	public String getBaseURI() {
//		return baseURI;
//	}
//
//	/**
//	 * Gibt die URI zur Ressource
//	 * @return
//	 */
//	public String getURI() {
//		return (baseURI.endsWith("/") ? baseURI : baseURI + "/") + tid;
//	}
	
	public enum State {
		READY, COMMITED, DELETED;
	}
}
