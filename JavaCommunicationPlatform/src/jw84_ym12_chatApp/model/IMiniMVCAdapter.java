package jw84_ym12_chatApp.model;

import java.util.HashSet;

import common.IChatServer;

/**
 * Model use this adapter to talk with Mini MVC Controller 
 * @author Jiawei
 *
 */
public interface IMiniMVCAdapter {
	


	/**
	 * set the proxyChatServer list used to display in the miniView, needed when do refresh
	 * @param chatServerProxys the updated chat server list
	 */
	void setchatServers(HashSet<IChatServer> chatServerProxys);
	/**
	 * This method is used to refresh the GUI and update the user list in it
	 */
	public void refresh();
}
