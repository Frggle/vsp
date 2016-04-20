package haw.vs.VSPraktikum.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class User {
	private String id;	// "users/<name>"
	private String name;	// "<name>"
	private String uri;	// "http://localhost:4567/client/<name>"
	
	public User(String name, String uri){
		this.id = "/users/" + name;
		this.name = name;
		this.uri = uri;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public JsonObject userInfo() {
		Gson gson = new Gson();
		String res = "{\"id\":" + "\"" + id + "\""
				+ ",\"name\":" + "\"" + name + "\"" 
				+ ",\"uri\":" + "\"" + uri + "\"}";
		JsonElement jsonElem = gson.fromJson(res, JsonElement.class);
		
		return jsonElem.getAsJsonObject();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
}
