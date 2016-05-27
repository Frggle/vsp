package haw.vs.VSPraktikum.services;

import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
import static spark.Spark.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import haw.vs.VSPraktikum.util.Bank.Bank;

/**
 * Ein BankService verwaltet alle Banken und bietet REST-Methoden an
 * @author Marc
 *
 */
public class BankService {
	/**
	 * bankNum -> Bank 
	 */
	private static Map<String, Bank> bankMap = new HashMap<>();
	/**
	 * generate unique banknumbers 
	 */
	private static int bankNumberCounter = 0;
	/**
	 * Service URI
	 * e.g. http://localhost:4567/services/255
	 */
	private static String URI;
	
	public static void main(String[] args) {
		
		/**
		 * Erzeugt eine neue Bank
		 */
		post("/banks", (req, res) -> {
			return "";
		});
		
		/**
		 * Liste mit allen Banken
		 */
		get("/banks", (req, res) -> {
			return "";
		});
		
		/**
		 * TODO: was wird hier gemacht?
		 */
		put("/banks/:bankNum", (req, res) -> {
			return "";
		});
		
		/**
		 * Gibt eine spezifische Bank zurueck
		 */
		get("/banks/:bankNum", (req, res) -> {
			return "";
		});
		
		/**
		 * Liste mit allen Transferen einer Bank
		 */
		get("/banks/:bankNum/transfers", (req, res) -> {
			return "";
		});
		
		/**
		 * Gibt fuer eine spezifische Bank einen bestimmten Transfer zueruck
		 */
		get("/banks/:bankNum/transfers/:tid", (req, res) -> {
			return "";
		});
		
		/**
		 * Ueberweist einen Geldbetrag von einem Account zum Anderen
		 * Wenn im QueryParams eine Transaction ID gesetzt ist -> erzeugt Transaction
		 */
		post("/banks/:bankNum/transfer/from/:accountFromNum/to/:accountToNum/:amount", (req, res) -> {
			return "";
		});
		
		/**
		 * Ueberweist einen Geldbetrag von der Bank zum Account
		 * Wenn im QueryParams eine Transaction ID gesetzt ist -> erzeugt Transaction
		 */
		post("/banks/:bankNum/transfer/to/:accountToNum/:amount", (req, res) -> {
			return "";
		});
		
		/**
		 * Zieht von einem Account einen Betrag ab
		 * Wenn im QueryParams eine Transaction ID gesetzt ist -> erzeugt Transaction
		 */
		post("/banks/:bankNum/transfer/from/:accountFromNum/:amount", (req, res) -> {
			return "";
		});
		
		/**
		 * Gibt den Status einer spezifischen Transaction zueruck
		 */
		get("/banks/:bankNum/transaction/:tid", (req, res) -> {
			return "";
		});
		
		/**
		 * Commitet (fuehrt aus) eine spezifische Transaction
		 */
		put("/banks/:bankNum/transaction/:tid", (req, res) -> {
			return "";
		});
		
		/**
		 * Entfernt eine Transaction
		 */
		delete("/banks/:bankNum/transaction/:tid", (req, res) -> {
			return "";
		});
		
		/**
		 * Liste mit allen Accounts
		 */
		get("/banks/:bankNum/accounts", (req, res) -> {
			return "";
		});
		
		/**
		 * Erzeugt einen neuen Account
		 */
		post("/banks/:bankNum/accounts", (req, res) -> {
			return "";
		});
		
		/**
		 * Gibt den Saldo eines spezifischen Accounts zurueck
		 */
		get("/banks/:bankNum/accounts/:accountNum", (req, res) -> {
			return "";
		});
		
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
		} catch(UnknownHostException e) {}
		
		registerService("jenny_marc_vsp_bank", "central bank in a game", "bank", "http://172.18.0.73:4567/bank");
	}
	
}
