package haw.vs.VSPraktikum.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import haw.vs.VSPraktikum.services.ServiceProvider;

/**
 * Wird nicht benutzt!
 * Nur als komfortable Abfragemöglichkeit
 * @author Marc
 *
 */
public class ServiceHelper {
	
	/**
	 * Durchsucht jeden Service im YellowPage nach searchName gleich dem Service-Name 
	 * @return String, die Service URI (/services/<id>)
	 * @throws UnirestException 
	 */
	public static void getService(Map<String, List<String>> ourServices) throws UnirestException {
		
		HttpResponse<JsonNode> responseNode = Unirest.get(ServiceProvider.YELLOWPAGE_URI_WITH_VPN + "/services").asJson();
		JSONArray jsnArray = responseNode.getBody().getObject().getJSONArray("services"); // alle verfuegbaren Services
		
		/**
		 * nimmt jede ServiceURI (/service/<service-id>) und fragt diesen nach seinem Namen
		 * wenn der Name identisch mit einem unserer Services ist, wird die ServiceURI gespeichert
		 */
		jsnArray.forEach((serviceUri) -> {
			try {
				HttpResponse<JsonNode> resonseNodeSingleService = Unirest.get(ServiceProvider.YELLOWPAGE_URI_WITH_VPN + serviceUri).asJson();
				String serviceName = resonseNodeSingleService.getBody().getObject().getString("name");
				String uri = resonseNodeSingleService.getBody().getObject().getString("uri");
				
				for(String service : ourServices.keySet()) {
					if(serviceName.equals(service)) {
						ourServices.get(service).add(0, serviceUri.toString());
						ourServices.get(service).add(uri);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static void main(String[] args) {
		Map<String, List<String>> ourServices = new HashMap<>();
		ourServices.put("jenny_marc_vsp_dice", new ArrayList<>());
		ourServices.put("jenny_marc_vsp_bank", new ArrayList<>());
		ourServices.put("jenny_marc_vsp_users", new ArrayList<>());
		ourServices.put("jenny_marc_vsp_client", new ArrayList<>());
		
		ourServices.put("BoardServices_GerritDuc", new ArrayList<>());
		ourServices.put("GameService_GerritDuc", new ArrayList<>());
		ourServices.put("BrokerService_GerritDuc", new ArrayList<>());
		ourServices.put("EventService_GerritDuc", new ArrayList<>());
		
		try {
			getService(ourServices);
		} catch(UnirestException e) {
			e.printStackTrace();
		}
		
		for(String s : ourServices.keySet()) {
			System.err.println("Service \"" + s + "\" hat folgende Yellowpage Registrierungen: " + ourServices.get(s));
		}
	}
}
