package jw84_ym12_chatApp.controller;
import java.rmi.RemoteException;
import java.util.HashSet;

import javax.swing.JPanel;

import common.IChatServer;
import common.IChatroom;
import jw84_ym12_chatApp.model.ChatServer;
import jw84_ym12_chatApp.model.IMiniMVCAdapter;
import jw84_ym12_chatApp.model.IUser2ModelAdapter;
import jw84_ym12_chatApp.model.IViewAdapter;
import jw84_ym12_chatApp.model.MainModel;
import jw84_ym12_chatApp.view.IModelAdapter;
import jw84_ym12_chatApp.view.MainGUI;

/**
 * Controller for the Client MVC system.
 * 
 */
public class MainController {

	/**
	 * The view of the MVC
	 */
	private MainGUI<IChatroom> view;  

	/**
	 * The model of the MVC
	 */
	private MainModel model;

	/**
	 * Constructor of the class. Instantiates and connects the model and the
	 * view.
	 */
	public MainController() {


		model = new MainModel(new IViewAdapter() {
			/**
			 * Send the string to the view
			 * 
			 * @param s
			 *            The string to display in the view
			 */
			@Override
			public void append(String s) {
				view.append(s);
			}

			/**
			 * Sets the displayed remote host on the view
			 * 
			 * @param hostAddress
			 *            The address of the host to display
			 */
			@Override
			public void setRemoteHost(String hostAddress) {
				view.setRemoteHost(hostAddress);
			}

			@Override
			public IMiniMVCAdapter makeMiniMVC(ChatServer chatServer, IChatServer stub,HashSet<IChatServer> chatServerProxys) {
				
				MiniController miniController;
				IMiniMVCAdapter miniMVCAdpt = null;
				try {
					miniController = new MiniController(chatServer.getChatroom(),model);
					miniMVCAdpt = new IMiniMVCAdapter(){

						@Override
						public void refresh() {
							// TODO Auto-generated method stub
							miniController.refresh();
						}
						@Override
						public void setchatServers(HashSet<IChatServer> chatServerProxys){
							miniController.setchatServers(chatServerProxys);
						}
						
					};
					try {
						view.install(chatServer.getChatroom().getName(), miniController.getMiniView());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					miniController.start(chatServer,stub, chatServerProxys);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				return miniMVCAdpt;
			}

			@Override
			public void closeRoom(JPanel miniView) {
				view.closeRoom(miniView);
				
			}

			@Override
			public void setChatroomList(HashSet<IChatroom> chatrooms) {
				view.setChatroomList(chatrooms);
				
			}

			@Override
			public void warning(String context) {
				view.warning(context);
			}

			@Override
			public void removeChatroomList() {
				view.removeChatroomList();
			}

			@Override
			public void addFriend(String newFriend) {
				// TODO Auto-generated method stub
				view.addUsertoList(newFriend);
			}
		},		new IUser2ModelAdapter(){

			@Override
			public MainModel getModel() {
				// TODO Auto-generated method stub
				return model;
			}
			
		});

		view = new MainGUI<IChatroom>(new IModelAdapter<IChatroom>() {
			/**
			 * Connect to the given remote host
			 * 
			 * @param the
			 *            remote host to connect to.
			 */
			@Override
			public String connectTo(String remoteHost) {
				return model.connectTo(remoteHost);
			}

			/**
			 * Quits the current connection and closes the application. Causes
			 * the model to stop and thus end the application.
			 */
			@Override
			public void quit() {
				model.stop();
			}

			@Override
			public void createRoom() {
				
				model.createRoom();
			}

			@Override
			public void joinRoom(IChatroom chatroom) {
				model.joinRoom(chatroom);
			}

			@Override
			public void setUsername(String username) {
				// TODO Auto-generated method stub
				model.setUsername(username);
			}

		});

	}

	/**
	 * Starts the view then the model. The view needs to be started first so
	 * that it can display the model status updates as it starts.
	 */
	public void start(String args) {
		view.start();
		model.start(args);
	}

	/**
	 * Main() method of the client application. Instantiates and then starts the
	 * controller.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		MainController controller = new MainController();
		controller.start(args[0]);
	}
}
