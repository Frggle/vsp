package haw.vs.VSPraktikum.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import haw.vs.VSPraktikum.util.User;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

public class Users {
	private static List<Users> userlist = new ArrayList<>();
	
	public static JSONObject getUserlist(Request request, Response response){
		response.status(200);
		response.type("application/json");
		return null;
		
	}
	
}
