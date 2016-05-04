package haw.vs.VSPraktikum.services;

import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
import static spark.Spark.get;
import static spark.Spark.post;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.http.HttpStatus;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import haw.vs.VSPraktikum.util.Bank.Account;
import haw.vs.VSPraktikum.util.Bank.Bank;

public class BankService {
	
	private static Map<String, Bank> bankMap = new HashMap<>(); // BankURI -> Bank
	private static Map<String, Account> accountMap = new HashMap<>(); // PlayerURI -> Account
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		
		registerService("jenny_marc_vsp_bank", "central bank in a game", "bank", "http://172.18.0.73:4567/bank");
		
		get("/banks", (request, response) -> {
			response.status(HttpStatus.OK_200);
			response.type("application/json");
			List<String> uriList = new ArrayList<>();
			for(Bank bank : bankMap.values()) {
				uriList.add(bank.getURI());
			}
			return gson.toJson(uriList);
		});
		
		/**
		 * neues Konto erstellen
		 */
		post("/banks/:bankuri/players", (request, response) -> {
			String bankuri = request.params(":bankuri");
			
			Account account = gson.fromJson(request.body(), Account.class);
			account.setBankURI(bankuri);
			
			if(accountMap.containsKey(account.getPlayerURI())) {
				response.status(HttpStatus.CONFLICT_409);
				response.type("text/plain");
				response.body("player already got a bank account");
			} else {
				response.status(HttpStatus.CREATED_201);
				response.type("text/plain");
				response.body("bank account has been created");
				accountMap.put(account.getPlayerURI(), account);
			}
			return "";
		});
		
		/**
		 * Kontostand abfragen
		 */
		get("banks/:bankuri/players/:playeruri", (request, response) -> {
			String playeruri = request.params(":playeruri");
			
			if(accountMap.containsKey(playeruri)) {
				Account account = accountMap.get(playeruri);
				response.status(HttpStatus.OK_200);
				response.type("application/json");
				String result = "{"+ "\"player\":" + account.getPlayerURI() + 
						", \"saldo\":" + account.getSaldo() +"}";
				JsonElement jsonElem = gson.fromJson(result, JsonElement.class);
				return jsonElem.getAsJsonObject();
			} else {
				response.status(HttpStatus.BAD_REQUEST_400);
				response.type("text/plain");
				response.body("account uri doesn't exist");
				return "";
			}
		});
		
		post("banks/:bankuri/transfer/to/:playerid/:amount", (request, response) -> {
			String playerid = request.params(":playerid");
			String amount_s = request.params(":amount");
			
			int amount = 0;
			try {
				amount = Integer.parseInt(amount_s);
			} catch (NumberFormatException e) {
				response.status(HttpStatus.BAD_REQUEST_400);
				response.type("text/plain");
				response.body("invalid amount");
			}
			Account account = null; 
			if(accountMap.containsKey(playerid)) {
				account = accountMap.get(playerid);
				account.fromBank(amount);
				response.status(HttpStatus.CREATED_201);
				response.type("application/json");
//				response.body("A new bank transfer has been created");
			}
			
			return "";
		});
	}
	
}
