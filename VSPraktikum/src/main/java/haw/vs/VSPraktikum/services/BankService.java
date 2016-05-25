package haw.vs.VSPraktikum.services;

import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

import com.google.gson.Gson;

import haw.vs.VSPraktikum.util.Bank.Account;
import haw.vs.VSPraktikum.util.Bank.Bank;

public class BankService {

	private static int bankNumCounter = 0;
	private static String URI;
	private static Map<String, Bank> bankMap = new HashMap<>(); // number -> Bank
	//private static Map<String, Account> accountMap = new HashMap<>(); // PlayerID -> Account

	public static void main(String[] args) {
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
		} catch(UnknownHostException e) {}

		Gson gson = new Gson();

		registerService("jenny_marc_vsp_bank", "central bank in a game", "bank", "http://172.18.0.73:4567/bank");

		/*
		 * Initialisiert eine neue Bank
		 */
		post("/banks", (req, res) -> {
			JSONObject json = new JSONObject(req.body());
			String gameID = json.getString("game");
			Bank bank = new Bank(gameID, bankNumCounter++);
			bankMap.put(bank.getNumber(), bank);
			res.header(HttpHeader.LOCATION.asString(), URI + bank.getID());

			return "";
		});

		/*
		 * Gibt die Liste aller Banken aus
		 */
		get("/banks", (request, response) -> {
			response.status(HttpStatus.OK_200);
			response.type("application/json");
			List<String> idList = new ArrayList<>();
			for(Bank bank : bankMap.values()) {
				idList.add(bank.getID());
			}
			JSONObject json = new JSONObject();
			json.put("banks", idList);
			return json;
		});

		/*
		 * Gibt die geforderte Bank zurück inkl. Transfers und Konten, falls vorhanden
		 * Sollte die angegebene Bank noch nicht existieren, wird diese erstellt
		 */
		// TODO: noch nicht fertig!!!
		put("/banks/:number", (request, response) -> {
			String bankid = request.params(":number");
			JSONObject json = new JSONObject(request.body());
			String accountUri = json.getString("accounts");
			String transferUri = json.getString("transfers");
			if(accountUri != null){

			}
			if(transferUri != null){

			}

//			if(!bankMap.containsKey(bankid)) {
//				response.status(HttpStatus.CREATED_201);
//			} else {
//				response.status(HttpStatus.BAD_REQUEST_400);
//			}

			return "";
		});

		/**
		 * neues Konto erstellen
		 */
		post("/banks/:number/accounts", (request, response) -> {
			String number = request.params(":number");

			Account account = gson.fromJson(request.body(), Account.class);
			account.setBankURI(number);
			Bank bank = bankMap.get(number);
			if(bank.accountExists(account.getPlayerURI())){
				response.status(HttpStatus.CONFLICT_409);
				response.type("text/plain");
				response.body("player already got a bank account");
			}else{
				response.status(HttpStatus.CREATED_201);
				response.type("text/plain");
				response.body("bank account has been created");

				bank.createAccount(account.getPlayerURI(), account.getSaldo());
				response.header(HttpHeader.LOCATION.asString(), URI + account.getAccountID());
			}
			return "";
		});

		/**
		 * Kontostand abfragen
		 */
		get("/banks/:number/accounts/:accountNumber", (request, response) -> {
			String bankNumber = request.params(":number");
			String accountNumber = request.params(":accountNumber");

			Bank bank = null;
			if(bankMap.containsKey(bankNumber)){
				bank = bankMap.get(bankNumber);
			} else{
				response.status(HttpStatus.NOT_FOUND_404);
				response.type("text/plain");
				response.body("bank doesn't exist");
				return "";
			}
			Account account = null;
			if(bank.accountExists(accountNumber)) {
				account = bank.getAccount(accountNumber);
			} else{
				response.status(HttpStatus.NOT_FOUND_404);
				response.type("text/plain");
				response.body("account doesn't exist");
				return "";
			}
			response.status(HttpStatus.OK_200);
			response.type("application/json");
			JSONObject jsn = new JSONObject();
			jsn.put("player", account.getPlayerURI());
			jsn.put("saldo", account.getSaldo());
			return jsn;
		});

		// ------------------- old code downstairs

		/*
		 * Geldtransfer von der Bank zum angegebenen Spieler
		 */
		post("/banks/:bankuri/transfer/to/:playerid/:amount", (request, response) -> {
			String playerid = "/users/" + request.params(":playerid");
			String amount_s = request.params(":amount");
			String bankuri = request.params(":bankuri");

			int amount = 0;
			try {
				amount = Integer.parseInt(amount_s);
			} catch(NumberFormatException e) {
				response.status(HttpStatus.BAD_REQUEST_400);
				response.type("text/plain");
				response.body("invalid amount");
			}
			Account account = null;
			if(accountMap.containsKey(playerid)) {
				account = accountMap.get(playerid);
				account.fromBank(amount);
				JSONObject json = new JSONObject(request.body());
				TransactionService.getInstance().createTransaction(bankuri, json.getString("description"), playerid);
				response.status(HttpStatus.CREATED_201);
				// response.type("application/json");
				// response.body("A new bank transfer has been created");
			} else {
				response.status(HttpStatus.NOT_FOUND_404);
				response.type("text/plain");
				response.body("player not found");
			}

			return "";
		});


		/*
		 * Geldtransfer vom Spieler zur Bank
		 */
		post("/banks/:bankuri/transfer/from/:playerid/:amount", (request, response) -> {
			String playerid = "/users/" + request.params(":playerid");
			String amount_s = request.params(":amount");
			String bankuri = request.params(":bankuri");

			int amount = 0;
			try {
				amount = Integer.parseInt(amount_s);
			} catch(NumberFormatException e) {
				response.status(HttpStatus.BAD_REQUEST_400);
				response.type("text/plain");
				response.body("invalid amount");
			}
			Account account = null;
			if(accountMap.containsKey(playerid)) {
				account = accountMap.get(playerid);
				if(account.toBank(amount)) {
					JSONObject json = new JSONObject(request.body());
					TransactionService.getInstance().createTransaction(bankuri, json.getString("description"), playerid);
					response.status(HttpStatus.CREATED_201);
					// response.type("application/json");
					// response.body("A new bank transfer has been created");
				} else {
					response.status(HttpStatus.FORBIDDEN_403);
					response.type("text/plain");
					response.body("invalid amount");
				}
			} else {
				response.status(HttpStatus.NOT_FOUND_404);
				response.type("text/plain");
				response.body("player not found");
			}

			return "";
		});

		/*
		 * Geldtransfer von einem Spieler zu einen anderen Spieler
		 */
		post("/banks/:bankuri/transfer/from/:from/to/:to/:amount", (request, response) -> {
			String bankuri = request.params(":bankuri");
			String fromPlayer = "/users/" + request.params(":from");
			String toPlayer = "/users/" + request.params(":to");
			String amount_s = request.params(":amount");

			int amount = 0;
			try {
				amount = Integer.parseInt(amount_s);
			} catch(NumberFormatException e) {
				response.status(HttpStatus.BAD_REQUEST_400);
				response.type("text/plain");
				response.body("invalid amount");
			}
			Account accountFrom = null;
			Account accountTo = null;
			if(accountMap.containsKey(fromPlayer) && accountMap.containsKey(toPlayer)) {
				accountFrom = accountMap.get(fromPlayer);
				accountTo = accountMap.get(toPlayer);
				if(accountFrom.toBank(amount)) {
					accountTo.fromBank(amount);
					JSONObject json = new JSONObject(request.body());
					TransactionService.getInstance().createTransaction(bankuri, json.getString("description"), fromPlayer);
					response.status(HttpStatus.CREATED_201);
					// response.type("application/json");
					// response.body("A new bank transfer has been created");
				} else {
					response.status(HttpStatus.FORBIDDEN_403);
					response.type("text/plain");
					response.body("invalid amount");
				}
			} else {
				response.status(HttpStatus.NOT_FOUND_404);
				response.type("text/plain");
				response.body("player not found");
			}

			return "";
		});
	}
}
