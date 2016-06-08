package haw.vs.VSPraktikum.util;

import org.json.JSONArray;
import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import haw.vs.VSPraktikum.services.ServiceProvider;

public class YellowServiceRegistration {
	
	private static boolean alreadyInExistence = false;
	
	public static void registerService(String name, String description, String service, String uri) {
		try {
			/**
			 * loesche erst alle Registrierungen des Services
			 */
			HttpResponse<JsonNode> response = Unirest.get(ServiceProvider.YELLOWPAGE_URI_WITH_VPN + "/services/of/name/" + name).asJson();
			JSONArray jsnAry = response.getBody().getObject().getJSONArray("services");
			if(jsnAry.length() > 0) {
				jsnAry.forEach(serviceUri -> {
					try {
						HttpResponse<JsonNode> res = Unirest.get(ServiceProvider.YELLOWPAGE_URI_WITH_VPN + serviceUri.toString()).asJson();
						/** loesche Registrierung nur, wenn sich die URI geaendert hat **/
						if(!res.getBody().getObject().getString("uri").equals(uri)) {
							Unirest.delete(ServiceProvider.YELLOWPAGE_URI_WITH_VPN + serviceUri.toString()).asJson();
						} else {
							alreadyInExistence = true;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				});
			}
			
			/**
			 * registriere den Service
			 */
			/** registriere nur, wenn URI noch nicht registriert **/
			if(!alreadyInExistence) {
				JSONObject json = new JSONObject();
				json.put("name", name);
				json.put("description", description);
				json.put("service", service);
				json.put("uri", uri);
				
				Unirest.post(ServiceProvider.YELLOWPAGE_URI_WITH_VPN + "/services").header("Content-Type", "application/json").body(json).asString();
			}
		} catch(Exception e) {
		}
	}
}
