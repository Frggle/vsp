package haw.vs.VSPraktikum;

public class Config {
	/**
	 * Die Service ID aus dem Yellowpage
	 * Like: /services/232
	 */
	public static final String BANK_SERVICE = "/services/7";
	public static final String DICE_SERVICE = "/services/35";
	public static final String USERS_SERVICE = "/services/10";
	public static final String EVENT_SERVICE = "/services/foo";
	public static final String BOARD_SERVICE = "/services/11";
	public static final String BROKER_SERVICE = "/services/foo";
	public static final String GAME_SERVICE = "/services/foo";
	
	/**
	 * Die Yellowpage URI, mit oder ohne aktive VPN Verbindung
	 */
	public static final String YELLOWPAGE_URI_WITH_VPN = "http://172.18.0.17:4567";
	public static final String YELLOWPAGE_URI_WITHOUT_VPN = "https://141.22.34.15/cnt/172.18.0.17:4567";
}
