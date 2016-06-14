package haw.vs.VSPraktikum.gui;

import static spark.Spark.post;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;
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
	
	private YellowpagesData gameService = ServiceProvider.getGameService();
	private YellowpagesData boardService = ServiceProvider.getBoardService();
	private YellowpagesData clientService = ServiceProvider.getClientService();
	
	private String gameURI;	// 		/games/3
	private String pawnURI;	// 		/pawns/mario
	private String playerName;	// 	mario
	private String playerURI;	// 	/players/mario
	
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
					gameURI = listAvailableGames.getSelectedValue();
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
				btnCreateGame.setEnabled(true);
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
			/** melde Client beim ClientService an **/
//			String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
			registerClient();
			
			/** erzeuge Spieler **/
			JSONObject body = new JSONObject();
			body.put("user", "/players/" + playerName);
			body.put("ready", true);
//			body.put("client", url);	// TODO: gameService kann damit noch nicht umgehen!!
			HttpResponse<JsonNode> response = Unirest.post(gameService.getURL() + gameURI + "/players").body(body).asJson();
			playerURI = response.getBody().getObject().getString("id");
			if(response.getStatus() == HttpStatus.OK_200) {
				playerJoined = true;
			} else {
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createAndJoinGame() {
		try {
			JSONObject gameBody = new JSONObject();
			int id = ThreadLocalRandom.current().nextInt(10, 100);
			gameBody.put("name", "VSP Game" + id);
			gameBody.put("services", "/games/" + id + "/services");
			gameBody.put("id", String.valueOf(id));
			gameBody.put("components", "/games/" + id + "/components");
			gameBody.put("players", "/games/" + id + "/players");
			gameBody.put("status", "Registration");
			HttpResponse<JsonNode> gameRes = Unirest.post(gameService.getUri()).body(gameBody).asJson();
			if(gameRes.getStatus() == HttpStatus.CREATED_201) {
				gameURI = String.valueOf(id);
				joinGame();
			}
		} catch(UnirestException e) {
			
		}
	}
	
	/**
	 * Client release Mutex wenn fertig!
	 */
	private void rollDice() {
		try {
			HttpResponse<JsonNode> mutexRes = Unirest.put(gameService.getURL() + gameURI + "/players/turn?player=" + gameURI + playerURI).asJson();
			System.err.println("Mutex Response Code: " + mutexRes.getStatus());
			if(mutexRes.getStatus() == HttpStatus.OK_200 || mutexRes.getStatus() == HttpStatus.CREATED_201) {
				HttpResponse<JsonNode> response = Unirest.post(boardService.getURL() + gameURI + pawnURI + "/roll").asJson();
				lblDiceRes.setText(response.getStatus() == 200 ? "true" : "false");
				
				/** release mutex **/
				Unirest.delete(boardService.getURL() + gameURI + "/players/turn");
			}
		} catch(UnirestException e) {
			e.printStackTrace();
		}
		lblDiceRes.setText("Fehler");
	}
	
	private void registerClient() {
		/** melde Client beim ClientService an **/
		try {
			String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":4567";
			
			JSONObject body = new JSONObject();
			body.put("uri", url);
			Unirest.post(clientService.getUri()).body(body);
		} catch(UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ClientUI_with_uri clientUI = ClientUI_with_uri.getInstance();
		clientUI.execute();
		
		post("/client/turn", (req, res) -> {
			if(playerJoined) {
				JOptionPane.showMessageDialog(frame, "It's your turn!");
				clientUI.btnRoll.setEnabled(true);
				return "button enabled";	
			}
			return "";
		});
	}
}
