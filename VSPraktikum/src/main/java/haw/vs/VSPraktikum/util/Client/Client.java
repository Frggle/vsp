package haw.vs.VSPraktikum.util.Client;

public class Client {
	String id;
	String name;
	String uri;
	
	public Client(String id, String name) {
		this.id = id;
		this.name = name;
		this.uri = "";
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
}
