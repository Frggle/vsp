package haw.vs.VSPraktikum.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import haw.vs.VSPraktikum.services.ServiceProvider;
import haw.vs.VSPraktikum.util.YellowpagesData;

public class ClientUI {
	
	private YellowpagesData gameService = ServiceProvider.getGameService();
	private YellowpagesData boardService = ServiceProvider.getBoardService();
	
	private String gameID;	// Spiel Nummer
	private String boardID;	// Board Nummer
	private String pawnID;	// Spielfigur Nummer
	private String playerName;	// Spieler Name
	
	private JFrame frame;
	private JTextField textFieldPlayerName;
	private JList<String> listAvailibleGames;
	private JButton btnJoin;
	private JButton btnRoll;
	private JButton btnOk;
	private JButton btnUpdategames;
	private JPanel panelDice;
	private JPanel panelPlayer;
	private JLabel lblEnterPlayerName;
	private JLabel lblDiceRes;
	private DefaultListModel<String> listModel;
	
	private static final ClientUI window = new ClientUI();
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void execute() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.initialize();
					window.frame.setVisible(true);					
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
	private ClientUI() {
	}
	
	public static ClientUI getInstance() {
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
				
				/** erzeuge Spieler im Game **/
//				createPlayer();
				
				/** aktiviere Game List **/ 
				listAvailibleGames.setEnabled(true);
				updateGamesList();
				btnUpdategames.setEnabled(true);
				btnJoin.setEnabled(true);
			}
		});
		panelPlayer.add(btnOk);
		
		listAvailibleGames = new JList<>();
		listAvailibleGames.setEnabled(false);
		frame.getContentPane().add(listAvailibleGames, BorderLayout.CENTER);
		
		btnJoin = new JButton("join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!listAvailibleGames.isSelectionEmpty()) {
					String selectedGame = listAvailibleGames.getSelectedValue();
					gameID = selectedGame.substring(selectedGame.lastIndexOf("/") + 1);
					lblDiceRes.setText("well done " + gameID);
					joinGame();
					btnRoll.setEnabled(true);
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
		listAvailibleGames.setModel(listModel);
		
		btnUpdategames = new JButton("update Games");
		btnUpdategames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUpdategames.setEnabled(false);
				updateGamesList();
				btnUpdategames.setEnabled(true);
			}
		});
		btnUpdategames.setEnabled(false);
		frame.getContentPane().add(btnUpdategames, BorderLayout.WEST);
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
					listAvailibleGames.setModel(model);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void joinGame() {
		try {
			btnJoin.setEnabled(false);
			listAvailibleGames.setEnabled(false);
			/** erzeuge Spieler **/
			JSONObject body = new JSONObject();
			body.put("user", "/" + playerName);
			body.put("ready", true);
			HttpResponse<JsonNode> response = Unirest.post(gameService.getUri() + "/" + gameID + "/players").body(body).asJson();
			String playerLocation = response.getHeaders().getFirst(HttpHeader.LOCATION.asString());
			System.err.println("Playerlocation: " + playerLocation);
			if(response.getStatus() == HttpStatus.OK_200) {
				HttpResponse<JsonNode> mutexRes = Unirest.put(gameService.getUri() + "/" + gameID + "/players/turn?player=" + playerName).asJson();
				System.err.println("Mutex Response Code: " + mutexRes.getStatus());
				if(mutexRes.getStatus() == HttpStatus.OK_200 || mutexRes.getStatus() == HttpStatus.CREATED_201) {
					/** erzeuge Board **/
					JSONObject boardBody = new JSONObject();
					boardBody.put("game", "/games/" + gameID);
					HttpResponse<JsonNode> boardRes = Unirest.post(boardService.getUri()).body(boardBody).asJson();
					System.err.println("Board Response: " + boardRes.getStatus());
					String boardURI = boardRes.getBody().getObject().getString("id");
					boardID = boardURI.substring(boardURI.lastIndexOf("/") + 1);
					
					/** erzeuge pawn **/
					JSONObject pawnBody = new JSONObject();
					pawnBody.put("player", "/games/" + gameID + "/players/" + playerName);
					pawnBody.put("position", 0);
					HttpResponse<JsonNode> pawnRes = Unirest.post(boardService.getUri() + "/" + gameID + "/pawns").asJson();
					pawnID = pawnRes.getBody().getObject().getString("id");
				}
			}
		} catch(UnirestException e) {
			e.printStackTrace();
		}
	}
	
	private void rollDice() {
		try {
			HttpResponse<JsonNode> response = Unirest.post(boardService.getUri() + "/" + gameID + "/pawns/" + pawnID + "/roll").asJson();
			lblDiceRes.setText(response.getStatus() == 200 ? "true" : "false");
		} catch(UnirestException e) {
			e.printStackTrace();
		}
		lblDiceRes.setText("Fehler");
	}
	
	private void connectToGameService() {
		/**
		 * meldet sich beim Game oder Board Service, damit dieser uns mittels post(/client/turn)
		 * informiert, wenn wir an der Reihe sind
		 * -> wir stellen uns vor, das die Mutex in einer Queue sind die abgearbeitet wird und 
		 * jedes mal den Client benachrichtigt
		 */
	}
}
