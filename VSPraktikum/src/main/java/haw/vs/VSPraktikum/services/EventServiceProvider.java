package haw.vs.VSPraktikum.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import haw.vs.VSPraktikum.util.YellowpagesData;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author DucNguyenMinh
 * @since 02/05/16
 */
public class EventServiceProvider {
//	private static final String EVENT_SERVICE_NAME = "gerrit_duc_vsp_Services";
	
//	private static final String EVENT_SERVICE_ID = "232";
	
	private static final String YELLOW_SERVICE_URL = "http://172.18.0.5:4567";
	
	public static YellowpagesData getService(String eventServiceID) {
		try {
			URL url = new URL(YELLOW_SERVICE_URL + "/services/" + eventServiceID);
//			URL url = new URL(YELLOW_SERVICE_URL + "/services/" + EVENT_SERVICE_ID);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			int responseCode = connection.getResponseCode();
			
			if(!(200 <= responseCode && responseCode <= 300)) {
				return null;
			}
			
			InputStream is = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			
			reader.close();
			
			ObjectMapper mapper = new ObjectMapper();
			YellowpagesData service = mapper.readValue(response.toString(), YellowpagesData.class);
			return service;
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(ProtocolException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
