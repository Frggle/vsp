package haw.vs.VSPraktikum.util.Bank;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
	private int tid;
	private List<Transfer> transferList;
	
	public Transaction(int tid) {
		transferList = new ArrayList<>();
		this.tid = tid;
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
		return res;
	}
	
	public int getTID() {
		return tid;
	}
}
