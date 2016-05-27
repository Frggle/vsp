package haw.vs.VSPraktikum.util._Bank;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import haw.vs.VSPraktikum.services._._TransactionService;

public class _Bank {
	private String id;	// /banks/<zahl>
	private String number;	// <zahl>
	private String gameid;	// /games/3
	private static final _TransactionService transService = _TransactionService.getInstance();
	private Map<String, _Account> accountMap; //playerUri -> Account
	private int accountNumCounter = 0;

	// TODO: transService verwenden

	public _Bank(String gameid, int number) {
		this.gameid = gameid;
		this.number = "" + number;
		this.id = "/banks/" + number;
		accountMap = new HashMap<>();
	}

	public String getID() {
		return id;
	}

	public String getNumber() {
		return number;
	}

	public boolean createAccount(String playerURI, String saldo) {
		int saldoAsInt = 0;
		if((saldoAsInt = convertSaldoToInt(saldo)) != Integer.MAX_VALUE) {
			if(accountMap.containsKey(playerURI)) {
				_Account acc = new _Account(playerURI, id, saldoAsInt, accountNumCounter++);
				accountMap.put(playerURI, acc);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean transferFromPlayerToBank(String playerURI, String saldo) {
		if(accountMap.containsKey(playerURI)) {
			_Account acc = accountMap.get(playerURI);
			int saldoAsInt = 0;
			if((saldoAsInt = convertSaldoToInt(saldo)) != Integer.MAX_VALUE) {
				if(acc.toBank(saldoAsInt)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean transferFromBankToPlayer(String playerURI, String saldo) {
		if(accountMap.containsKey(playerURI)) {
			_Account acc = accountMap.get(playerURI);
			int saldoAsInt = 0;
			if((saldoAsInt = convertSaldoToInt(saldo)) != Integer.MAX_VALUE) {
				acc.fromBank(saldoAsInt);
				return true;
			}
		}
		return false;
	}

	public boolean transferFromPlayerToPlayer(String playerURIFrom, String playerURITo, String saldo) {
		if(accountMap.containsKey(playerURIFrom) && accountMap.containsKey(playerURITo)) {
			_Account accFrom = accountMap.get(playerURIFrom);
			_Account accTo = accountMap.get(playerURITo);
			int saldoAsInt = 0;
			if((saldoAsInt = convertSaldoToInt(saldo)) != Integer.MAX_VALUE) {
				if(!accFrom.toBank(saldoAsInt)) {
					return false;
				}
				accTo.fromBank(saldoAsInt);
				return true;
			}
		}
		return false;
	}

	private int convertSaldoToInt(String saldo) {
		int saldo_asInt = 0;
		try {
			saldo_asInt = Integer.parseInt(saldo);
		} catch(NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
		return saldo_asInt;
	}

	public boolean accountExists(String playerUri){
		return accountMap.containsKey(playerUri);
	}

//	public Account getAccount(String playerUri) {
//		return accountMap.get(playerUri);
//	}

	public _Account getAccount(String accountNumber) {
		for(Entry<String, _Account> entry : accountMap.entrySet()) {
			if(entry.getValue().getAccountNumber().equals(accountNumber)) {
				return entry.getValue();
			}
		}
		return null;
	}
}
