package haw.vs.VSPraktikum.util;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Client für den Yellow-Pages Web-Service von Andrej.
 */
public class YellowRESTClient {
	private String _url;
	
	/**
	 * Die URL, auf der sich der Yellow-Pages Web-Service befindet.
	 * @param url Die URL des Yellow-Pages
	 */
	public YellowRESTClient(String url) {
		_url = url;
	}
	
	/**
	 * Ermittelt eine Liste aller am Yellow-Pages registrierten Services. Dieser Aufruf entspricht der API-Funktion
	 * {@code GET /services?expanded}.
	 * @return Eine Liste aller am Yellow-Pages registrierten Services
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public List<Service> getServices() throws UnirestException {
		List<Service> services = new ArrayList<Service>();
		
		HttpResponse<JsonNode> response = Unirest.get(_url + "/services?expanded").asJson();
		JSONObject responseJson = response.getBody().getObject();
		JSONArray servicesJson = responseJson.getJSONArray("services");
		for(Object jsonEntry : servicesJson) {
			JSONObject serviceJson = (JSONObject)jsonEntry;
			services.add(unmarshall(serviceJson));
		}
		
		return services;
	}
	
	/**
	 * Registriert den übergebenen Service am Yellow-Pages. Der Aufruf entspricht der API-Funktion
	 * {@code POST /services}.
	 * @param service Der zu registrierende Service
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public void postService(Service service) throws UnirestException {
		Unirest.post(_url + "/services").header("Content-Type", "application/json").body(marshall(service)).asString();
	}
	
	/**
	 * Gibt den am Yellow-Pages registrierten Service mit der übergebenen ID zurück. Der Aufruf entspricht der
	 * API-Funktion {@code GET /services/:id?expanded}.
	 * @param id Die ID des Services
	 * @return Der registrierte Service oder {@code null}, wenn es keinen gibt
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public Service getService(String id) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.post(_url + "/services/" + id + "?expanded").asJson();
		if(response.getStatus() == 404) {
			return null;
		}
		return unmarshall(response.getBody().getObject());
	}
	
	/**
	 * Registriert bzw. aktualisiert den Eintrag des Services hinter der übergebenen ID mit dem übergebenen Service. Der
	 * Aufruf entspricht der API-Funktion {@code PUT /services/:id}.
	 * @param id Die ID des Services
	 * @param service Die neuen Angaben des Service
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public void putService(String id, Service service) throws UnirestException {
		Unirest.put(_url + "/services/" + id).header("Content-Type", "application/json").body(marshall(service)).asString();
	}
	
	/**
	 * Entfernt den eingetragenen Service hinter der übergebenen ID. Der Aufruf entspricht der API-Funktion
	 * {@code DELETE /services/:id}.
	 * @param id Die ID des auszutragenden Services
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public void deleteService(String id) throws UnirestException {
		Unirest.delete(_url + "/services/" + id).asString();
	}
	
	/**
	 * Gibt eine Liste aller am Yellow-Pages registrierten Services zurück, die den übergebenen Namen haben. Der Aufruf
	 * entspricht der API-Funktion {@code GET /services/of/name/:name?expanded}.
	 * @param name Der Name
	 * @return Eine Liste aller Services mit dem übergebenen Namen
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public List<Service> getServicesOfName(String name) throws UnirestException {
		List<Service> services = new ArrayList<Service>();
		
		HttpResponse<JsonNode> response = Unirest.get(_url + "/services/of/name/" + name + "?expanded").asJson();
		JSONObject responseJson = response.getBody().getObject();
		JSONArray servicesJson = responseJson.getJSONArray("services");
		for(Object jsonEntry : servicesJson) {
			JSONObject serviceJson = (JSONObject)jsonEntry;
			services.add(unmarshall(serviceJson));
		}
		
		return services;
	}
	
	/**
	 * Gibt eine Liste aller am Yellow-Pages registrierten Services zurück, die dem übergebenen Typ entsprechen. Der
	 * Aufruf entspricht der API-Funktion {@code GET /services/of/type/:type?expanded}.
	 * @param type Der Typ
	 * @return Eine Liste aller Services mit dem übergebenen Typ
	 * @throws UnirestException Wenn ein Fehler bei der Übermittlung des Requests auftritt
	 */
	public List<Service> getServicesOfType(String type) throws UnirestException {
		List<Service> services = new ArrayList<Service>();
		
		HttpResponse<JsonNode> response = Unirest.get(_url + "/services/of/type/" + type + "?expanded").asJson();
		JSONObject responseJson = response.getBody().getObject();
		JSONArray servicesJson = responseJson.getJSONArray("services");
		for(Object jsonEntry : servicesJson) {
			JSONObject serviceJson = (JSONObject)jsonEntry;
			services.add(unmarshall(serviceJson));
		}
		
		return services;
	}
	
	/**
	 * Verpackt den übergebenen {@link Service} in ein {@link JSONObject}.
	 * @param service Der zu verpackende Service
	 * @return Die JSON-Repräsentation des Objekts
	 */
	private static JSONObject marshall(Service service) {
		JSONObject json = new JSONObject();
		json.put("name", service.getName());
		json.put("service", service.getService());
		json.put("uri", service.getUri());
		json.put("description", service.getDescription());
		return json;
	}
	
	/**
	 * Entpackt ein {@link Service}-Objekt aus dem übergebenen {@link JSONObject}.
	 * @param json Das zu entpackende JSON
	 * @return Der entpackte Service
	 */
	private Service unmarshall(JSONObject json) {
		String id = json.getString("_uri").replace("/services/", "");
		String name = json.getString("name");
		String service = json.getString("service");
		String uri = json.getString("uri");
		String status = json.optString("status");
		String description = json.optString("description");
		return new Service(id, name, service, uri, status, description);
	}
}
