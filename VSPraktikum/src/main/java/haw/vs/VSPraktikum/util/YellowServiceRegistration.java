package haw.vs.VSPraktikum.util;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

public class YellowServiceRegistration {
	private static final String YELLOW_PAGES = "http://172.18.0.17:4567/services";
//	private static final String YELLOW_PAGES = "http://141.22.34.15/cnt/172.18.0.5/4567/services";

	public static void registerService(String name, String description, String service, String uri) {
		try {
			JSONObject json = new JSONObject();
			json.put("name", name);
			json.put("description", description);
			json.put("service", service);
			json.put("uri", uri);

			Unirest.post(YELLOW_PAGES).header("Content-Type", "application/json").body(json).asString();
		} catch (Exception e) {}
	}
}