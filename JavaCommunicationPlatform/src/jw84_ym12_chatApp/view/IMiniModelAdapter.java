package jw84_ym12_chatApp.view;

import java.util.HashSet;

/**
 * Adapter the mini-View uses to communicate to the mini-Model
 * @author Jiawei Wu
 *
 */
public interface IMiniModelAdapter<TList> {

	/**
	 * Close the chat room
	 * @param miniView - a chat room entity
	 */
	public void closeRoom();
	/**
	 * get the newest chatroom stubs from the mini model
	 * @return the set of chatserver stubs in the chatroom
	 */
	public HashSet<TList> getChatStubs();

	/**
	 * Remove selected item
	 * @param selectedValue - A TList type
	 */
	public void removeUser(TList selectedValue);
	/**
	 * invite someone to the room
	 * @param remoteHost IP of the remote user
	 */
	public void addUser(String remoteHost);
	/**
	 * @param text - Input text
	 */
	public void sendMsg(String text);
	/**
	 * the button used to send a selected image
	 * @param imgPath 
	 */
	public void sendimage(String imgPath);

}
