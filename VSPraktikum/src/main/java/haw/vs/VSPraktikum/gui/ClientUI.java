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

public class ClientUI {
	
	private String playerName;
	
	private JFrame frame;
	private JTextField textFieldPlayerName;
	private JTextField textFieldDiceRes;
	private JList<String> listAvailibleGames;
	private JButton btnJoin;
	private JButton btnRoll;
	private JButton btnOk ;
	private JPanel panelDice;
	private JPanel panelPlayer;
	private JLabel lblEnterPlayerName;
	private DefaultListModel<String> listModel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientUI window = new ClientUI();
					window.frame.setVisible(true);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public ClientUI() {
		initialize();
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
				playerName = textFieldPlayerName.getText();
			}
		});
		panelPlayer.add(btnOk);
		
		listAvailibleGames = new JList<>();
		frame.getContentPane().add(listAvailibleGames, BorderLayout.CENTER);
		
		btnJoin = new JButton("join");
		frame.getContentPane().add(btnJoin, BorderLayout.EAST);
		
		panelDice = new JPanel();
		frame.getContentPane().add(panelDice, BorderLayout.SOUTH);
		
		btnRoll = new JButton("roll");
		panelDice.add(btnRoll);
		
		textFieldDiceRes = new JTextField();
		panelDice.add(textFieldDiceRes);
		textFieldDiceRes.setColumns(10);
		
		listModel = new DefaultListModel<>();
		listModel.addElement("foo");
		listAvailibleGames.setModel(listModel);
	}
	
	public void addElementToGamesList(String element) {
		DefaultListModel<String> model = (DefaultListModel<String>)listAvailibleGames.getModel();
		model.addElement(element);
		
		listAvailibleGames.setModel(model);
	}
}
