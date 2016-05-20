package haw.vs.VSPraktikum.util.Bank;

import java.util.HashMap;
import java.util.Map;
import haw.vs.VSPraktikum.services.TransactionService;

public class Bank {
	private String id;	// /banks/<zahl>
	private String number;	// <zahl>
	private String gameid;	// /games/3
	private static final TransactionService transService = TransactionService.getInstance();
	private Map<String, Account> accountMap;
	
	// TODO: transService verwenden
	
	public Bank(String gameid, int number) {
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
				Account acc = new Account(playerURI, id, saldoAsInt);
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
			Account acc = accountMap.get(playerURI);
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
			Account acc = accountMap.get(playerURI);
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
			Account accFrom = accountMap.get(playerURIFrom);
			Account accTo = accountMap.get(playerURITo);
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
}
