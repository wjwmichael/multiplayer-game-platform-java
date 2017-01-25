package jw84_ym12_chatApp.controller;

import common.IChatServer;
import common.IChatroom;
import jw84_ym12_chatApp.model.ChatServer;
import jw84_ym12_chatApp.model.IMiniViewAdapter;
import jw84_ym12_chatApp.model.MainModel;
import jw84_ym12_chatApp.model.MiniModel;
import jw84_ym12_chatApp.view.IMiniModelAdapter;
import jw84_ym12_chatApp.view.ImageView;
import jw84_ym12_chatApp.view.MiniGUI;

import java.awt.Container;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Mini Controller used for the mini-MVC
 * @author Jiawei Wu
 *
 */
public class MiniController {
	/**
	 * The view of the MVC
	 */
	private MiniGUI<IChatServer> miniGUI;  
	@SuppressWarnings("unused")
	private HashSet<IChatServer> chatServerProxys;
	/**
	 * The model of the MVC
	 */
	private MiniModel miniModel;
	
	private IChatroom chatroom;
	/**
	 * Constructor of the class. Instantiates and connects the model and the
	 * view.
	 * @param mainModel - a MainModel type
	 * @param miniMVCAdpt 
	 */
	public MiniController(IChatroom room,MainModel mainModel) {	
		this.chatroom = room;
		miniModel = new MiniModel(new IMiniViewAdapter() {

			@Override
			public void closeRoom(JPanel miniView) {
				mainModel.closeRoom(miniView);
				
			}
			//TODO
			@Override
			public MainModel getModel(){
				return mainModel;
			}
			@Override
			public void refresh() {
				// TODO Auto-generated method stub
				miniGUI.refresh();
			}
			@Override
			public void append(String username, String text) {
				miniGUI.append(username, text);
			}
			@Override
			public void append(String username,ImageIcon image){
				miniGUI.append(username, image);
			}
			@Override
			public Container createImageView() {
				// TODO Auto-generated method stub
                ImageView imageView = new ImageView();
                imageView.start();
                return imageView.getContentPane();
			}
			@Override
			public void add2nonscrollable(JComponent comp) {
				// TODO Auto-generated method stub
				miniGUI.add2nonscrollable(comp);
			}
		});
		
		miniGUI = new MiniGUI<IChatServer>(new IMiniModelAdapter<IChatServer>() {

			@Override
			public void closeRoom() {
				miniModel.closeRoom(miniGUI);	
			}

			@Override
			public HashSet<IChatServer> getChatStubs() {
				return miniModel.getChatStubs();
			}

			@Override
			public void sendMsg(String text) {
				miniModel.sendTextMsg(text);
			}
			@Override
			public void addUser(String remoteHost){
				mainModel.invite(chatroom, remoteHost,miniModel.getServerStub());
				miniModel.refresh();
			}

			@Override
			public void removeUser(IChatServer selectedValue) {
				//miniModel.removeUser(selectedValue);
			}

			@Override
			public void sendimage(String imgPath) {
				// TODO Auto-generated method stub
				miniModel.sendMap();
			}

		});
	}
	
	/**
	 * Starts the view then the model. The view needs to be started first so
	 * that it can display the model status updates as it starts.
	 */
	public void start(ChatServer chatServer, IChatServer stub,HashSet<IChatServer> chatServerProxys) {
		miniGUI.start(chatServerProxys);
		miniModel.start(chatServer,stub);
		this.chatServerProxys = chatServerProxys;
	}
	
	/**
	 * get the MiniView so that we can install it into the Main View
	 * @return a MiniView type
	 */
	public MiniGUI<IChatServer> getMiniView() {
		return this.miniGUI;
	}
	/**
	 * set the chatserver proxy list in the miniGUI and update it to the newest version
	 * @param chatServerProxys
	 */
	public void setchatServers(HashSet<IChatServer> chatServerProxys){
		this.chatServerProxys = chatServerProxys;
		System.out.println("chatServerProxys size:  "+ chatServerProxys.size());
		miniGUI.start(chatServerProxys);
	}
	/**
	 * give the according mini view a refresh to display the correct current user list in chatroom
	 */
	public void refresh(){
		miniGUI.refresh();
	}
}
