package jw84_ym12_chatApp.view;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Rectangle;

import javax.swing.JLabel;

/**
 * A mini-View
 * @author Weili
 * @param <TList> 
 * @param <UserType> a generic type for JList
 *
 */
public class MiniGUI<TList> extends JPanel {

	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The adapter to the model
	 */
	private IMiniModelAdapter<TList> miniModelAdpt;

	private DefaultListModel<TList> listModel = new DefaultListModel<TList>();
	private JTextField textField;
	private JList<TList> list = new JList<TList>(listModel);
	private JPanel north_panel = new JPanel();
	private JPanel center_panel = new JPanel();
	private JPanel south_panel = new JPanel();
	private ScrollTextPanel scrollPane = new ScrollTextPanel();
	private JScrollPane scrollPane_1 = new JScrollPane();


	/**
	 * The constructor of MiniView
	 * @param miniModelAdpt - the IMiniModelAdapter type
	 */
	public MiniGUI(IMiniModelAdapter<TList> miniModelAdpt) {
		super();
		this.miniModelAdpt = miniModelAdpt;
		initGUI();
	}



	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		
		
		add(north_panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("IP Address: ");
		north_panel.add(lblNewLabel);
		
		textField = new JTextField();
		north_panel.add(textField);
		textField.setColumns(10);
		
		JButton btnAddUser = new JButton("Invite User");
		btnAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miniModelAdpt.addUser(textField.getText());
			}
		});
		north_panel.add(btnAddUser);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel.clear();
				refresh();
			}
		});
		
		JButton btnExit = new JButton("Leave Room");
		north_panel.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO about send "remove this user" message to all other users in this chat room
				miniModelAdpt.closeRoom();
			}
		});
		north_panel.add(btnRefresh);
		
		
		add(center_panel, BorderLayout.CENTER);
		center_panel.setLayout(new BorderLayout(0, 0));
		
		center_panel.add(list, BorderLayout.EAST);
		
		
		center_panel.add(scrollPane, BorderLayout.CENTER);
		

//		scrollPane.setViewportView(textAreaDis);
//		TitledBorder title = BorderFactory.createTitledBorder("Message List");
//		scrollPane.setBorder(title);
		
		TitledBorder title1 = BorderFactory.createTitledBorder("Chat Participants");
		list.setBorder(title1);
		TitledBorder title2 = BorderFactory.createTitledBorder("Input Message");
		scrollPane_1.setBorder(title2);
		
		
		add(south_panel, BorderLayout.SOUTH);
		
		
		south_panel.add(scrollPane_1);
		
		JTextArea textAreaInp = new JTextArea();
		scrollPane_1.setViewportView(textAreaInp);
		textAreaInp.setRows(3);
		textAreaInp.setColumns(50);
		
		JButton btnSend = new JButton("send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miniModelAdpt.sendMsg(textAreaInp.getText());
			}
		});
		
		JButton btnSendimg = new JButton("Start Game");
		btnSendimg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO about send "remove this user" message to all other users in this chat room
				//FileDialog fileChooser = new FileDialog(new JFrame(),"Select the image you want to send",FileDialog.LOAD);
				//fileChooser.setVisible(true);
				String imgPath = "nothing";
				miniModelAdpt.sendimage(imgPath);
			}
		});
		south_panel.add(btnSendimg);
		south_panel.add(btnSend);
	}
	public void append(String username, String text) {
		// TODO Auto-generated method stub
		TextPanel panel = scrollPane.textPanel;
		panel.append("[" + username + "]: " + text + "\n");
		int height = (int)scrollPane.getPreferredSize().getHeight();
        Rectangle rect = new Rectangle(0,height,10,10);
        scrollPane.scrollRectToVisible(rect);
//		System.out.println("[" + username + "]: " + text + "************************\n");
	}
	public void refresh(){
		start(miniModelAdpt.getChatStubs());
	}
//	/**
//	 * Starts the view by making it visible.
//	 * @param stub - A user stub
//	 */
//	public void start(T stub) {
//		listModel.addElement(stub);
//		System.out.println(stub.toString());
//	}

	/**
	 * Starts the view by showing its TList
	 * @param stubs - A TList set
	 */
	public void start(HashSet<TList> stubs) {
		System.out.println("Number of stubs in MiniGUI perspective: " + stubs.size());
		listModel.removeAllElements();
		for (TList stub : stubs){
			listModel.addElement(stub);
			System.out.println(stub.toString());
		}
		
	}
	/**
	 * append the sended image to the local GUI
	 * @param username People who send the image
	 * @param image the image file was sent
	 */
	public void append(String username, ImageIcon image) {
		// TODO Auto-generated method stub
		TextPanel panel = scrollPane.textPanel;
		panel.append("[" + username + "]: ");
		panel.append(image);
		int height = (int)scrollPane.getPreferredSize().getHeight();
        Rectangle rect = new Rectangle(0,height,10,10);
        scrollPane.scrollRectToVisible(rect);
	}



	public void add2nonscrollable(JComponent comp) {
		
		
	}
}
