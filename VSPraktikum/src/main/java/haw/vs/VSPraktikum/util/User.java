package haw.vs.VSPraktikum.util;

public class User {
	private String id;
	private String name;
	private String uri;
	
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
	
	// TODO: wird auch die ID geaendert?
	public void setName(String name) {
		this.name = name;
		this.id = "/users/" + name;
	}
	
	public String getUri() {
		return this.uri;
	}
}
