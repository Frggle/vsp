package haw.vs.VSPraktikum.services;

import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
import static spark.Spark.delete;
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

import haw.vs.VSPraktikum.util.Bank.Bank;
import haw.vs.VSPraktikum.util.Bank.Transaction;
import haw.vs.VSPraktikum.util.Bank.Transfer;

/**
 * Ein BankService verwaltet alle Banken und bietet REST-Methoden an
 * @author Marc
 */
public class BankService {
	/**
	 * bankNum -> Bank
	 */
	private static Map<String, Bank> bankMap = new HashMap<>();
	/**
	 * generate unique banknumbers
	 */
	private static int bankNumberCounter = 1;
	/**
	 * Service URI e.g. http://localhost:4567/services/255
	 */
	private static String URI;

	public static void main(String[] args) {
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
		} catch(UnknownHostException e) {
		}

		registerService("jenny_marc_vsp_bank", "central bank in a game", "bank", "http://172.18.0.73:4567/bank");

		/**
		 * Erzeugt eine neue Bank
		 */
		post("/banks", (req, res) -> {
			JSONObject jsnBody = new JSONObject(req.body());
			Bank bank = new Bank(bankNumberCounter++, jsnBody.getString("game"));
			bankMap.put(bank.getBankNumber(), bank);
			res.header(HttpHeader.LOCATION.asString(), URI + bank.getSubURI());
			res.status(HttpStatus.CREATED_201);
			return "";
		});

		/**
		 * Liste mit allen Banken
		 */
		get("/banks", (req, res) -> {
			JSONObject jsn = new JSONObject();
			List<String> bankURIList = new ArrayList<>();
			for(Bank bank : bankMap.values()) {
				bankURIList.add(bank.getSubURI());
			}
			jsn.put("banks", bankURIList);
			res.type("application/json");
			return jsn;
		});

		/**
		 * TODO: was wird hier gemacht?
		 */
		put("/banks/:bankNum", (req, res) -> {
			return "not implemented";
		});

		/**
		 * Gibt eine spezifische Bank zurueck
		 */
		get("/banks/:bankNum", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				JSONObject jsn = new JSONObject();
				jsn.put("accounts", URI + bank.getSubURI() + "/accounts");
				jsn.put("transfers", URI + bank.getSubURI() + "/transfers");
				res.type("application/json");

				return jsn;
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Liste mit allen Transferen einer Bank
		 */
		get("/banks/:bankNum/transfers", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				JSONObject jsn = new JSONObject();
				jsn.put("transfers", bank.getTransfers());
				res.type("application/json");

				return jsn;
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Gibt fuer eine spezifische Bank einen bestimmten Transfer zurueck
		 */
		get("/banks/:bankNum/transfers/:id", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				Transfer transfer = bank.getTransfer(req.params(":id"));
				JSONObject jsn = new JSONObject();
				jsn.put("from", bank.getSubURI() + "/accounts/" + transfer.getAccFrom().getAccountNumber());
				jsn.put("to", bank.getSubURI() + "/accounts/" + transfer.getAccTo().getAccountNumber());
				jsn.put("amount", transfer.getAmount());
				jsn.put("reason", transfer.getReason());
				res.status(HttpStatus.OK_200);
				res.type("application/json");
				return jsn;
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}

		});

		/**
		 * Ueberweist einen Geldbetrag von einem Account zum Anderen Wenn im QueryParams eine Transaction ID gesetzt ist
		 * -> erzeugt Transaction ( ../to/4/300?transaction=42 )
		 */
		post("/banks/:bankNum/transfer/from/:accountFromNum/to/:accountToNum/:amount", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				String accFrom = req.params(":accountFromNum");
				String accTo = req.params(":accountToNum");
				String amount = req.params(":amount");
				String tid = req.queryParams("transaction");

				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				int transferID = 0;
				if(tid == null) {
					transferID = bank.transfer(accFrom, accTo, amount, req.body().toString());
				} else {
					transferID = bank.addTransferToTransaction(tid, accFrom, accTo, amount, req.body().toString());
				}

				res.header(HttpHeader.LOCATION.asString(), URI + bank.getSubURI() + "/transfers/" + transferID);
				res.status(HttpStatus.CREATED_201);

				// TODO: im response fehlt create Event

				return "done";
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Ueberweist einen Geldbetrag von der Bank zum Account Wenn im QueryParams eine Transaction ID gesetzt ist ->
		 * erzeugt Transaction
		 */
		post("/banks/:bankNum/transfer/to/:accountToNum/:amount", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				String accTo = req.params(":accountToNum");
				String amount = req.params(":amount");
				String tid = req.queryParams("transaction");

				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				int transferID = 0;
				if(tid == null) {
					transferID = bank.transfer("0", accTo, amount, req.body().toString());
				} else {
					transferID = bank.addTransferToTransaction(tid, "0", accTo, amount, req.body().toString());
				}

				res.header(HttpHeader.LOCATION.asString(), URI + bank.getSubURI() + "/transfers/" + transferID);
				res.status(HttpStatus.CREATED_201);

				// TODO: im response fehlt create Event

				return "done";
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Zieht von einem Account einen Betrag ab Wenn im QueryParams eine Transaction ID gesetzt ist -> erzeugt
		 * Transaction
		 */
		post("/banks/:bankNum/transfer/from/:accountFromNum/:amount", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				String accFrom = req.params(":accountFromNum");
				String amount = req.params(":amount");
				String tid = req.queryParams("transaction");

				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				int transferID = 0;
				if(tid == null) {
					transferID = bank.transfer(accFrom, "0", amount, req.body().toString());
				} else {
					transferID = bank.addTransferToTransaction(tid, accFrom, "0", amount, req.body().toString());
				}

				res.header(HttpHeader.LOCATION.asString(), URI + bank.getSubURI() + "/transfers/" + transferID);
				res.status(HttpStatus.CREATED_201);

				// TODO: im response fehlt create Event

				return "done";
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Gibt den Status einer spezifischen Transaction zueruck
		 */
		get("/banks/:bankNum/transaction/:tid", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				String tid = req.params(":tid");

				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				Transaction transaction = bank.getTransaction(tid);
				return transaction == null ? "Transaction ID nicht gefunden" : transaction.getStatus();
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Commitet (fuehrt aus) eine spezifische Transaction
		 */
		put("/banks/:bankNum/transaction/:tid", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				String tid = req.params(":tid");
				String state = req.queryParams("state");

				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				if(bank.getTransaction(tid) == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Transaction " + tid + " wurde nicht gefunden";
				}
				if(state == null || state == "commit") {
					bank.commitTransaction(tid);
				} else {
					res.status(HttpStatus.NOT_IMPLEMENTED_501);
					return "not implemented";
				}
				return "done";
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Entfernt eine Transaction
		 */
		delete("/banks/:bankNum/transaction/:tid", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				String tid = req.params(":tid");

				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				bank.deleteTransaction(tid);
				res.status(HttpStatus.OK_200);
				return "Transaction " + tid + " wurde geloescht";
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Liste mit allen Accounts
		 */
		get("/banks/:bankNum/accounts", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");

				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				JSONObject jsn = new JSONObject();
				jsn.put("accounts", bank.getAccountURIs());
				res.type("application/json");
				res.status(HttpStatus.OK_200);
				return jsn;
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Erzeugt einen neuen Account
		 */
		post("/banks/:bankNum/accounts", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");

				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				JSONObject jsnBody = new JSONObject(req.body());
				String player = jsnBody.getString("player");
				int saldo = jsnBody.getInt("saldo");
				int accID = bank.createAccount(saldo, player);

				res.header(HttpHeader.LOCATION.asString(), URI + bank.getSubURI() + "/accounts/" + accID);
				res.status(HttpStatus.CREATED_201);
				return "done";
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});

		/**
		 * Gibt den Saldo eines spezifischen Accounts zurueck
		 */
		get("/banks/:bankNum/accounts/:accountNum", (req, res) -> {
			try {
				String bankNum = req.params(":bankNum");
				String accNum = req.params(":accountNum");

				Bank bank = bankMap.get(bankNum);
				if(bank == null) {
					res.status(HttpStatus.NOT_FOUND_404);
					return "Die Bank " + bankNum + " wurde nicht gefunden";
				}
				JSONObject jsn = new JSONObject();
				jsn.put("player", bank.getAccountPlayer(accNum));
				jsn.put("saldo", bank.getAccountSaldo(accNum));
				return jsn;
			} catch(Exception e) {
				res.status(HttpStatus.PRECONDITION_FAILED_412);
				return e.getMessage();
			}
		});
	}
}
