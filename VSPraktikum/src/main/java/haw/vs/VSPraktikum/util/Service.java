package haw.vs.VSPraktikum.util;

import java.util.Objects;

/**
 * Repräsentiert einen Web-Service der an Andrejs Yellow-Pages registriert wird.
 */
public class Service {
	private String _id;
	private String _name;
	private String _service;
	private String _uri;
	private String _status;
	private String _description;
	
	/**
	 * Dieser Konstruktor ist für Clienten die am {@link YellowPagesRestClient} einen Service registrieren möchten.
	 * @param name Der Name des Services, darf nicht {@code null} sein
	 * @param service Der Typ des Services, darf nicht {@code null} sein
	 * @param uri Die URI zu dem Service, darf nicht {@code null} sein
	 * @param description Die Beschreibung des Services
	 */
	public Service(String name, String service, String uri, String description) {
		this(null, name, service, uri, null, description);
	}
	
	/**
	 * Dieser Konstruktor wird nur intern vom {@link YellowPagesRestClient} aufgerufen, wenn ein Service-Objekt von dem
	 * Web-Service empfangen wird.
	 * @param id Die ID des Services
	 * @param name Der Name des Services, darf nicht {@code null} sein
	 * @param service Der Typ des Services, darf nicht {@code null} sein
	 * @param uri Die URI des Services, darf nicht {@code null} sein
	 * @param status Der Status des Services
	 * @param description Die Beschreibung des Services
	 */
	Service(String id, String name, String service, String uri, String status, String description) {
		_id = id;
		_name = Objects.requireNonNull(name);
		_service = Objects.requireNonNull(service);
		_uri = Objects.requireNonNull(uri);
		_status = status;
		_description = description;
	}
	
	/**
	 * Gibt die ID des Services zurück.
	 * @return Die ID des Services oder {@code null}, wenn der Service nirgends registriert ist
	 */
	public String getId() {
		return _id;
	}
	
	/**
	 * Gibt den Namen des Services zurück.
	 * @return Der Name des Services
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Gibt den Typ des Services zurück.
	 * @return Der Typ des Services
	 */
	public String getService() {
		return _service;
	}
	
	/**
	 * Gibt die URI des Services zurück.
	 * @return Die URI des Services
	 */
	public String getUri() {
		return _uri;
	}
	
	/**
	 * Gibt den Status des Services zurück.
	 * @return Der Status des Services oder {@code null}, wenn der Service nirgends registriert ist
	 */
	public String getStatus() {
		return _status;
	}
	
	/**
	 * Gibt die Beschreibung des Services zurück.
	 * @return Die Beschreibung des Services oder {@code null}, wenn keine angegeben wurde
	 */
	public String getDescription() {
		return _description;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_description == null) ? 0 : _description.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result + ((_service == null) ? 0 : _service.hashCode());
		result = prime * result + ((_status == null) ? 0 : _status.hashCode());
		result = prime * result + ((_uri == null) ? 0 : _uri.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Service other = (Service)obj;
		if(_description == null) {
			if(other._description != null)
				return false;
		} else if(!_description.equals(other._description))
			return false;
		if(_id == null) {
			if(other._id != null)
				return false;
		} else if(!_id.equals(other._id))
			return false;
		if(_name == null) {
			if(other._name != null)
				return false;
		} else if(!_name.equals(other._name))
			return false;
		if(_service == null) {
			if(other._service != null)
				return false;
		} else if(!_service.equals(other._service))
			return false;
		if(_status == null) {
			if(other._status != null)
				return false;
		} else if(!_status.equals(other._status))
			return false;
		if(_uri == null) {
			if(other._uri != null)
				return false;
		} else if(!_uri.equals(other._uri))
			return false;
		return true;
	}
	
	public String toString() {
		return "Service[name='" + getName() + "', service='" + getService() + "',uri='" + getUri() + "',description='" + getDescription() + "']";
	}
}
