package haw.vs.VSPraktikum.services;

import static haw.vs.VSPraktikum.util.YellowServiceRegistration.registerService;
import static spark.Spark.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import haw.vs.VSPraktikum.util.Bank.Bank;

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
		
		post("/banks", (req, res) -> {
			return "";
		});
		
		get("/banks", (req, res) -> {
			return "";
		});
		
		put("/banks/:bankNum", (req, res) -> {
			return "";
		});
		
		get("/banks/:bankNum", (req, res) -> {
			return "";
		});
		
		get("/banks/:bankNum/transfers", (req, res) -> {
			return "";
		});
		
		get("/banks/:bankNum/transfers/:tid", (req, res) -> {
			return "";
		});
		
		post("/banks/:bankNum/transfer/from/:accountFromNum/to/:accountToNum/:amount", (req, res) -> {
			return "";
		});
		
		post("/banks/:bankNum/transfer/to/:accountToNum/:amount", (req, res) -> {
			return "";
		});
		
		post("/banks/:bankNum/transfer/from/:accountFromNum/:amount", (req, res) -> {
			return "";
		});
		
		get("/banks/:bankNum/transaction/:tid", (req, res) -> {
			return "";
		});
		
		put("/banks/:bankNum/transaction/:tid", (req, res) -> {
			return "";
		});
		
		delete("/banks/:bankNum/transaction/:tid", (req, res) -> {
			return "";
		});
		
		get("/banks/:bankNum/accounts", (req, res) -> {
			return "";
		});
		
		post("/banks/:bankNum/accounts", (req, res) -> {
			return "";
		});
		
		get("/banks/:bankNum/accounts/:accountNum", (req, res) -> {
			return "";
		});
		
		try {
			URI = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
		} catch(UnknownHostException e) {}
		
		registerService("jenny_marc_vsp_bank", "central bank in a game", "bank", "http://172.18.0.73:4567/bank");
	}
	
}
