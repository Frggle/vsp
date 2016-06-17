package haw.vs.VSPraktikum.gui;

import static spark.Spark.post;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import haw.vs.VSPraktikum.services.ServiceProvider;
import haw.vs.VSPraktikum.util.YellowpagesData;

public class ClientUI_with_uri {
	
	/** wird initial benoetigt, um alle offenen Games bereitzustellen **/
	private static YellowpagesData gameService = ServiceProvider.getGameService();
	/** zum registrieren des Client im ClientService, um kein Polling zu betreiben **/
	private YellowpagesData clientService = ServiceProvider.getClientService();
	
	/** absolute URLs **/
	private String pawnURL;		// http://<host>:<port>/boards/0/pawns/mario
	private static String playerURL	;	// http://<host>:<port>/games/3/players/mario
	private String rollURL;		// http://<host>:<port>/boards/0/pawns/mario/roll
	
	private static String gameURL;	// /games/3
	private String playerName;	// mario
	
	protected static boolean playerJoined = false;
	
	private static JFrame frame;
	private JTextField textFieldPlayerName;
	private JList<String> listAvailableGames;
	private JButton btnJoin;
	protected JButton btnRoll;
	private JButton btnOk;
	private JButton btnCreateGame;
	private JPanel panelDice;
	private JPanel panelPlayer;
	private JLabel lblEnterPlayerName;
	private JLabel lblDiceRes;
	private DefaultListModel<String> listModel;
	
	private static final ClientUI_with_uri window = new ClientUI_with_uri();
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void execute() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.initialize();
					ClientUI_with_uri.frame.setVisible(true);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	private ClientUI_with_uri() {
	}
	
	public static ClientUI_with_uri getInstance() {
		return window;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		panelPlayer = new JPanel();
		frame.getContentPane().add(panelPlayer, BorderLayout.NORTH);
		
		lblEnterPlayerName = new JLabel("enter player name");
		panelPlayer.add(lblEnterPlayerName);
		
		btnOk = new JButton("OK");
		btnOk.setEnabled(false);
		
		textFieldPlayerName = new JTextField();
		textFieldPlayerName.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				changed();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				changed();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				changed();
			}
			
			public void changed() {
				if(textFieldPlayerName.getText().equals("")) {
					btnOk.setEnabled(false);
				} else {
					btnOk.setEnabled(true);
				}
			}
		});
		
		panelPlayer.add(textFieldPlayerName);
		textFieldPlayerName.setColumns(10);
		
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/** nachdem Name bestaetigt sind keine Aenderungen mehr moeglich **/
				playerName = textFieldPlayerName.getText();
				btnOk.setEnabled(false);
				textFieldPlayerName.setEnabled(false);
				
				/** aktiviere Game List **/
				listAvailableGames.setEnabled(true);
				updateGamesList();
				btnCreateGame.setEnabled(true);
				btnJoin.setEnabled(true);
			}
		});
		panelPlayer.add(btnOk);
		
		listAvailableGames = new JList<>();
		listAvailableGames.setEnabled(false);
		frame.getContentPane().add(listAvailableGames, BorderLayout.CENTER);
		
		btnJoin = new JButton("join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!listAvailableGames.isSelectionEmpty()) {
					gameURL = listAvailableGames.getSelectedValue();
					lblDiceRes.setText("well done");
					btnJoin.setEnabled(false);
					listAvailableGames.setEnabled(false);
					joinGame();
				}
			}
		});
		btnJoin.setEnabled(false);
		frame.getContentPane().add(btnJoin, BorderLayout.EAST);
		
		panelDice = new JPanel();
		frame.getContentPane().add(panelDice, BorderLayout.SOUTH);
		
		btnRoll = new JButton("roll dice");
		btnRoll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rollDice();
			}
		});
		btnRoll.setEnabled(false);
		panelDice.add(btnRoll);
		
		lblDiceRes = new JLabel();
		panelDice.add(lblDiceRes);
		
		listModel = new DefaultListModel<>();
		listModel.addElement("foo");
		listAvailableGames.setModel(listModel);
		
		btnCreateGame = new JButton("create game");
		btnCreateGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCreateGame.setEnabled(false);
				createAndJoinGame();
			}
		});
		btnCreateGame.setEnabled(false);
		frame.getContentPane().add(btnCreateGame, BorderLayout.WEST);
	}
	
	private void updateGamesList() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DefaultListModel<String> model = new DefaultListModel<>();
					System.err.println(gameService.getUri());
					HttpResponse<JsonNode> response = Unirest.get(gameService.getUri()).asJson();
					JSONArray jsnAry = response.getBody().getObject().getJSONArray("games");
					jsnAry.forEach(s -> {
						model.addElement(s.toString());
					});
					listAvailableGames.setModel(model);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 
	 */
	private void joinGame() {
		try {
			/** erzeuge Spieler **/
			JSONObject body = new JSONObject();
			body.put("user", "http://some:4567/users/" + playerName);
			body.put("ready", true);
			
			if(playerURL == null) {
				playerURL = gameService.getURL() + gameURL + "/players";
			}
			
			HttpResponse<JsonNode> response = Unirest.post(playerURL).body(body).asJson();
			playerURL = response.getBody().getObject().getString("id");
			System.err.println(playerURL);
			if(response.getStatus() == HttpStatus.OK_200) {
				playerJoined = true;
				System.err.println("Player joined " + playerJoined);
				
				/** melde Client beim ClientService an **/
				registerClient();
			} else {
				System.err.println("Spieler konnte nicht erzeugt werden");
			}
		} catch(UnirestException e) {
			e.printStackTrace();
		}
	}
	
	private void createAndJoinGame() {
		try {
			JSONObject services = new JSONObject();
			services.put("dice", "http://localhost:8080/dice");
			services.put("brokers", "http://localhost:8080/dice");
			services.put("boards", "http://localhost:8080/dice");
			services.put("banks", "http://localhost:8080/dice");
			services.put("decks", "http://localhost:8080/dice");
			services.put("events", "http://localhost:8080/dice");
			services.put("games", "http://localhost:8080/dice");
			JSONObject gameBody = new JSONObject();
			gameBody.put("services", services);
			
			HttpResponse<JsonNode> gameRes = Unirest.post(gameService.getUri()).body(gameBody).asJson();
			gameURL = gameRes.getBody().getObject().getString("id");
			System.err.println(gameRes.getStatus());
			
			if(gameRes.getStatus() == HttpStatus.CREATED_201 || gameRes.getStatus() == HttpStatus.OK_200) {
				playerURL = gameRes.getBody().getObject().getString("players");
				joinGame();
			}
		} catch(UnirestException e) {
			e.printStackTrace();
		}
	}
	
	private void setPawnURL() {
		try {
			HttpResponse<JsonNode> response = Unirest.get(playerURL).asJson();
			pawnURL = response.getBody().getObject().getString("pawn");
		} catch(UnirestException e) {
			e.printStackTrace();
		}
	}
	
	private void setRollURL() {
		try {
			HttpResponse<JsonNode> response = Unirest.get(pawnURL).asJson();
			rollURL = response.getBody().getObject().getString("roll");
		} catch(UnirestException e) {
			e.printStackTrace();
		}
	}
	
	private boolean aquireRollMutex() {
		try {
			System.err.println("playerURL " + playerURL);
			String player = playerURL.substring(playerURL.lastIndexOf("7") + 1);
			System.err.println("player " + player);
			System.err.println(gameService.getURL() + gameURL);
			HttpResponse<JsonNode> response = Unirest.put(gameURL + "/players/turn?player=" + player).asJson();
			if(response.getStatus() == HttpStatus.OK_200 || response.getStatus() == HttpStatus.CREATED_201) {
				return true;
			}
		} catch(UnirestException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void rollDice() {
		try {
			/** setzt die pawn uri **/
			setPawnURL();
			System.err.println("pawn " + pawnURL);
			/** setzt die roll uri **/
			setRollURL();
			System.err.println("roll " + rollURL);
			
			/** braucht mutex um wuerfeln zu duerfen **/
			if(aquireRollMutex()) {
				HttpResponse<JsonNode> response = Unirest.post(rollURL).asJson();
				lblDiceRes.setText(response.getStatus() == 200 ? "true" : "false");
				
				String placeURL = response.getBody().getObject().getString("place");
				
				HttpResponse<JsonNode> res = Unirest.get(placeURL).asJson();
				String brokerURL = res.getBody().getObject().getString("broker");
				
				HttpResponse<JsonNode> res2 = Unirest.get(brokerURL).asJson();
				String ownerURL = res2.getBody().getObject().getString("owner");
				
				/** kauf Strasse Digga**/
				JSONObject body = new JSONObject();
				body.put("owner", placeURL);
				HttpResponse<JsonNode> res3 = Unirest.post(ownerURL).body(body).asJson();
				System.err.println(res3.getBody());
				
				HttpResponse<JsonNode> res4 = Unirest.put(playerURL).asJson();
				System.err.println("ready Status gesetzt: " + res4.getStatus());
				
				/** release mutex **/
				HttpResponse<String> r = Unirest.delete(gameURL + "/players/turn").asString();
				System.err.println(r.getBody());
			} 
		} catch(UnirestException e) {
			e.printStackTrace();
		}
	}
	
	private void registerClient() {
		/** melde Client beim ClientService an **/
		try {
			String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
			
			JSONObject body = new JSONObject();
			body.put("uri", url);
			body.put("player", playerURL);
			System.err.println(clientService.getUri());
			Unirest.post(clientService.getUri()).body(body).asString();
		} catch(UnknownHostException | UnirestException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ClientUI_with_uri clientUI = ClientUI_with_uri.getInstance();
		clientUI.execute();
		
		post("/client/turn", (req, res) -> {
//			JSONObject jsnBody = new JSONObject(req.body());
//			playerURL = jsnBody.getString("player");
			
//			if(playerJoined) {
				JOptionPane.showMessageDialog(frame, "It's your turn!");
				clientUI.btnRoll.setEnabled(true);
				
				return "button enabled";
//			}
//			return "";
		});
	}
}
