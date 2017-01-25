package jw84_ym12_chatApp.view;
import javax.swing.*;


import java.awt.event.*;
import java.util.HashSet;
import java.awt.*;


/**
 * The view of the client MVC system.
 * 
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 * @param <TDropList> 
 */
public class MainGUI<TDropList> extends JFrame {

	/**
	 * SerialVersionUId for the class.
	 */
	private static final long serialVersionUID = -199099598475124566L;

	/**
	 * The default remote host reference.
	 */
	private static final String DEFAULT_REMOTE_HOST = "localhost"; 

	/**
	 * The adapter to the model.
	 */
	private IModelAdapter<TDropList> model;
	
	private JFrame frame;

	/**
	 * The top control panel
	 */
	private JPanel controlPnl;

	/**
	 * The status output text area
	 */
	private JTextArea outputTA;

	/**
	 * The connect button
	 */
	private JButton connectBtn;

	/**
	 * The quit button
	 */
	private JButton quitBtn;

	/**
	 * The remote server's IP address info input text field.
	 */
	private JTextField remoteHostTF; 
	private JPanel panel;
	private JTabbedPane chatRoomPane;
	private JScrollPane scrollPane;
	private JButton btnJoinRoom;
	private JComboBox<TDropList> comboBox = new JComboBox<TDropList>();
	private JSplitPane splitPane;
	private JTabbedPane friendListPane;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_5;
	private JLabel label_6;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> userList ;
	//private int count = 2;

	/**
	 * Constructor of the class
	 * @param ma - the ModelAdapter 
	 */
	public MainGUI(IModelAdapter<TDropList> ma) {
		super("ChatApp: user1");
		model = ma;
		initGUI();
	}

	/**
	 * Initializes the view and its components.
	 */
	protected void initGUI() {
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			/**
			 * tell the model to quit if the window is closing, even if very slowly.
			 */
			public void windowClosing(WindowEvent evt) {
				System.out.println("this.windowClosing, event="+evt);
				model.quit();
			}
		});
		setSize(1000,800);
		setLocationRelativeTo(null);
		controlPnl = new JPanel();
		Container contentPane = getContentPane();
		contentPane.add(controlPnl, BorderLayout.NORTH);
		controlPnl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel remoteHostLbl = new JLabel("Remote Host:");
		controlPnl.add(remoteHostLbl);
		
		label = new JLabel("");
		controlPnl.add(label);
		
		label_1 = new JLabel("");
		controlPnl.add(label_1);
		
		label_2 = new JLabel("");
		controlPnl.add(label_2);
		
		label_3 = new JLabel("");
		controlPnl.add(label_3);
		
		label_4 = new JLabel("");
		controlPnl.add(label_4);
		
		label_5 = new JLabel("");
		controlPnl.add(label_5);
		
		label_6 = new JLabel("");
		controlPnl.add(label_6);
		remoteHostTF = new JTextField(DEFAULT_REMOTE_HOST);
		remoteHostTF.setToolTipText("IP address of remote host");
		remoteHostTF.setPreferredSize(new Dimension(100,25));
		controlPnl.add(remoteHostTF);
		{
			connectBtn = new JButton();
			connectBtn.setToolTipText("connect to the remote host");
			controlPnl.add(connectBtn);
			connectBtn.setText("Connect");
			connectBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					System.out.println("connectBtn.actionPerformed, event="+evt);
					connect();
				}
			});
		}
		remoteHostTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		
		controlPnl.add(comboBox);
		
		btnJoinRoom = new JButton("Join Room");
		btnJoinRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.joinRoom(comboBox.getItemAt(comboBox.getSelectedIndex()));
			}
		});
		controlPnl.add(btnJoinRoom);
		JButton chatroomBtn = new JButton("Create Room");
		controlPnl.add(chatroomBtn);
		chatroomBtn.setToolTipText("Click it to create a new chat room.");
		chatroomBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					append("Create a chat room...\n");
					model.createRoom();
				}
				catch(Exception excpt) {
					append("Create chat room exception: "+excpt+"\n");
				}
			}
		});

		panel = new JPanel();
		controlPnl.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		{
			quitBtn = new JButton();
			quitBtn.setToolTipText("Quit and stop the client");
			controlPnl.add(quitBtn);
			quitBtn.setText("Quit");			
			splitPane = new JSplitPane();
			getContentPane().add(splitPane, BorderLayout.CENTER);

			userList = new JList<String>(listModel);
			
	        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	        userList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	        JPanel userListPanel = new JPanel(false);
	        userListPanel.setLayout(new GridLayout(1, 1));
	        
	        userListPanel.add(userList);
	        
			chatRoomPane = new JTabbedPane(JTabbedPane.TOP);
			
			scrollPane = new JScrollPane();
			chatRoomPane.addTab("Info", null, scrollPane, null);
			outputTA = new JTextArea();
			scrollPane.setViewportView(outputTA);
			outputTA.setLineWrap(true);
			outputTA.setWrapStyleWord(true);
			quitBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					System.out.println("quitBtn.actionPerformed, event="+evt);
					model.quit();
				}
			});
			splitPane.setRightComponent(chatRoomPane);
			
			friendListPane = new JTabbedPane(JTabbedPane.TOP);
			friendListPane.add("Friend List", userListPanel);
			splitPane.setLeftComponent(friendListPane);
		}	
	}

	/**
	 * Have the model connect to the remote server.
	 */
	private void connect() {
		append("Connecting...\n");
		append(model.connectTo(remoteHostTF.getText())+"\n");
	}

	/**
	 * Set the displayed remote host text field to the actual remote system's IP address or host name 
	 * @param host The name of the remote host 
	 */
	public void setRemoteHost(String host){
		remoteHostTF.setText(host);
	}

	/**
	 * Append the given string(s) to the view's output text adapter.  
	 * @param s the string to display.
	 */
	public void append(String s) {
		outputTA.append(s);
		//Force the JScrollPane to go to scroll down to the new text
		outputTA.setCaretPosition(outputTA.getText().length());
	}

	/**
	 * Starts the view by making it visible.
	 */
	public void start() {
		String username = JOptionPane.showInputDialog(null, "Please input your user name:", "User Login", 
				JOptionPane.INFORMATION_MESSAGE);
		if (username.isEmpty() || username == null){
			username = "Anonymous";
		}
		setTitle("ChatApp: " + username);
		setVisible(true);
		model.setUsername(username);
	}
	public void addUsertoList(String userStub){
		listModel.addElement(userStub);
	}
	/**
	 * Install the miniView into the JFrame
	 * @param miniView - a MiniView type
	 * @param rmName - chat room name
	 */
	public void install(String rmName, JPanel miniView) {
		chatRoomPane.addTab(rmName, miniView);
	}
	/**
	 * close the chat room
	 * @param miniView the according view
	 */
	public void closeRoom(JPanel miniView) {
		chatRoomPane.remove(miniView);
	}
	/**
	 * set the list of chatrooms
	 * @param chatrooms the chatroom list
	 */
	public void setChatroomList(HashSet<TDropList> chatrooms) {
		comboBox.removeAllItems();
		for (TDropList chatroom: chatrooms){
			comboBox.insertItemAt(chatroom, 0);
		}
		
	}
	/**
	 * remove all the chatrooms in the list
	 */
	public void removeChatroomList(){
		comboBox.removeAllItems();
	}
	/**
	 * pop up a window and give the warning message
	 * @param context the message to be displayed
	 */
	public void warning(String context) {
		JOptionPane.showMessageDialog(frame, context);
	}
}