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
	
	private static Map<Integer, Bank> bankMap = new HashMap<>();
	private static Map<String, Account> accountMap = new HashMap<>(); // PlayerURI -> Account
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		
		registerService("jenny_marc_vsp_bank", "central bank in a game", "bank", "http://172.18.0.73:4567/bank");
		
		get("/banks", (request, response) -> {
			response.status(HttpStatus.OK_200);
			response.type("application/json");
			List<Integer> idList = new ArrayList<>();
			for(Bank bank : bankMap.values()) {
				idList.add(bank.getID());
			}
			return gson.toJson(idList);
		});
		
		/**
		 * neues Konto erstellen
		 */
		post("/banks/:gameid/players", (request, response) -> {
			String gameid = request.params(":gameid");
			
			Account account = gson.fromJson(request.body(), Account.class);
			account.setGameID(gameid);
			
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
		
		get("banks/:gameid/players/:playerid", (request, response) -> {
			String playerid = request.params(":playerid");
			
			if(accountMap.containsKey(playerid)) {
				Account account = accountMap.get(playerid);
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
	}
	
}
