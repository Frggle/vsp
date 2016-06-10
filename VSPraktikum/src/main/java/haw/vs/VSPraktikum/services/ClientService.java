package haw.vs.VSPraktikum.services;

import static spark.Spark.post;

public class ClientService {
	
	public static void main(String[] args) {

		post("/client/turn", (req, res) -> {
			return "";
		});
	}
	
}
