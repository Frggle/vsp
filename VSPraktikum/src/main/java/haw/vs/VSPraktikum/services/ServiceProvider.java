package haw.vs.VSPraktikum.services;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import haw.vs.VSPraktikum.Config;
import haw.vs.VSPraktikum.util.YellowpagesData;

/**
 * @author Marc Kaepke
 * @since 07/06/16
 */
public class ServiceProvider {
	
	public static YellowpagesData getService(String serviceURI) {
		try {
			String url = Config.YELLOWPAGE_URI_WITH_VPN + serviceURI;
			
			// Only one time
			Unirest.setObjectMapper(new ObjectMapper() {
				private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
				
				public <T> T readValue(String value, Class<T> valueType) {
					try {
						return jacksonObjectMapper.readValue(value, valueType);
					} catch(IOException e) {
						throw new RuntimeException(e);
					}
				}
				
				public String writeValue(Object value) {
					try {
						return jacksonObjectMapper.writeValueAsString(value);
					} catch(JsonProcessingException e) {
						throw new RuntimeException(e);
					}
				}
			});
			
			HttpResponse<YellowpagesData> response = Unirest.get(url).asObject(YellowpagesData.class);
			return response.getBody();
		} catch(UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}
}
