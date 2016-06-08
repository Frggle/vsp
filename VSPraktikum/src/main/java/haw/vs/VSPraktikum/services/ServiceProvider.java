package haw.vs.VSPraktikum.services;

import java.io.IOException;
import org.json.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import haw.vs.VSPraktikum.util.YellowpagesData;

/**
 * @author Marc Kaepke
 * @since 07/06/16
 */
public class ServiceProvider {

	/**
	 * Service-Namen
	 */
	private static final String diceService = "jenny_marc_vsp_dice";
	private static final String bankService = "jenny_marc_vsp_bank";
	private static final String userService = "jenny_marc_vsp_users";
	
	private static final String boardService = "BoardServices_GerritDuc";
	private static final String gameService = "GameService_GerritDuc";
	private static final String brokerService = "BrokerService_GerritDuc";
	private static final String eventService = "EventService_GerritDuc";
	
	/**
	 * Die Yellowpage URI, mit oder ohne aktive VPN Verbindung
	 */
	public static final String YELLOWPAGE_URI_WITH_VPN = "http://172.18.0.5:4567";
	public static final String YELLOWPAGE_URI_WITHOUT_VPN = "https://141.22.34.15/cnt/172.18.0.5:4567";
	
	private static YellowpagesData getService(String serviceURI) {
		try {
			/** bspw.: /services/92 **/
			String url = YELLOWPAGE_URI_WITH_VPN + serviceURI;
			
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
	
	private static YellowpagesData smallHelper(String service) {
		try {
			/** hole alle Services mit dem Namen **/
			HttpResponse<JsonNode> resAllServices = Unirest.get(YELLOWPAGE_URI_WITH_VPN + "/services/of/name/" + service).asJson();
			JSONArray jsnAry = resAllServices.getBody().getObject().getJSONArray("services");
			/** nimm die letzte Service Nummer **/
			String recentService = jsnAry.get(jsnAry.length() - 1).toString();
			
			YellowpagesData result = getService(recentService);
			return result;
		} catch(UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static YellowpagesData getBankService() {
		return smallHelper(bankService);
	}
	
	public static YellowpagesData getUserService() {
		return smallHelper(userService);
	}
	
	public static YellowpagesData getDiceService() {
		return smallHelper(diceService);
	}
	
	public static YellowpagesData getBrokerService() {
		return smallHelper(brokerService);
	}
	
	public static YellowpagesData getGameService() {
		return smallHelper(gameService);
	}
	
	public static YellowpagesData getBoardService() {
		return smallHelper(boardService);
	}
	
	public static YellowpagesData getEventService() {
		return smallHelper(eventService);
	}
}
