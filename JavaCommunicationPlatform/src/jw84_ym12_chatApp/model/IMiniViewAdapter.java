package jw84_ym12_chatApp.model;

import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Adapter the mini-Model uses to communicate to the mini-View and Main Model
 * @author JiaweiWu
 *
 */
public interface IMiniViewAdapter {

	/**
	 * Close the chat room
	 * @param miniView - a chat room
	 */
	public void closeRoom(JPanel miniView);
	/**
	 * Let view to get the chatstubs and display the
	 */
	public void refresh();
	/**
	 * 
	 * @param username append the username to the GUI
	 * @param text append the message text to the GUI
	 */
	public void append(String username, String text);
	/**
	 * 
	 * @param name the username of sender
	 * @param data the sended image data
	 */
	public void append(String name, ImageIcon data);
	/**
	 * 
	 * @return the main model give miniview access to comm with main model
	 */
	public MainModel getModel();
	/**
	 * 
	 * @return a image view provided for the unknown datapacket
	 */
	public Container createImageView();
	public void add2nonscrollable(JComponent comp);

}
