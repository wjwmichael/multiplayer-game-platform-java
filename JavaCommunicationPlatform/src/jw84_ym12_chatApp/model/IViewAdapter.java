package jw84_ym12_chatApp.model;

import java.util.HashSet;

import javax.swing.JPanel;

import common.IChatServer;
import common.IChatroom;

/**
 * Adapter the model uses to communicate to the view
 */
public interface IViewAdapter {
	/**
	 * Appends the string to the end of the text display on the view The view
	 * does NOT automatically add a linefeed to the end of the string!
	 * 
	 * @param s The String to append
	 *            
	 */
	public void append(String s);

	/**
	 * Sets the displayed remote host address.
	 * @param hostAddress The IP address or host name of the remote host.
	 */
	public void setRemoteHost(String hostAddress);
	/**
	 * 
	 * @param miniView the miniView used by the chatroom
	 */
	public void closeRoom(JPanel miniView);
	/**
	 * 
	 * @param chatrooms the current chatroom list
	 */
	public void setChatroomList(HashSet<IChatroom> chatrooms);
	/**
	 * 
	 * @param string the warning message to be printed on screen
	 */
	public void warning(String string);

	/**
	 * add this friend to the friend list in view
	 * @param newFriend
	 */
	public void addFriend(String newFriend);
	/**
	 * delete the whole chatroom list
	 */
	public void removeChatroomList();
	/**
	 * Make mini-MVC
	 * @param rmName - the room name
	 * @param stub - the IUser stub
	 * @return - an IMiniMVCAdapter type
	 */

	IMiniMVCAdapter makeMiniMVC(ChatServer chatServer, IChatServer stub, HashSet<IChatServer> chatServerProxys);
}

