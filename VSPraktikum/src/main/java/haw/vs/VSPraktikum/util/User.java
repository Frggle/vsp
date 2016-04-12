package haw.vs.VSPraktikum.util;

public class User {
	private int _id;
	private String _name;
	private String _uri;
	
	public User(int id, String name, String uri){
		_id = id;
		_name = name;
		_uri = uri;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public String getName() {
		return _name;
	}
	public void setName(String _name) {
		this._name = _name;
	}
	public String getUri() {
		return _uri;
	}
	public void setUri(String _uri) {
		this._uri = _uri;
	}
	
}
