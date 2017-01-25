package jw84_pc30_chatApp.util;

import common.IChatServer;
import common.msg.chat.ILeaveMsg;

public class LeaveMsg implements ILeaveMsg{
	/**
	 * 
	 */
	private static final long serialVersionUID = -681242360052564754L;
	IChatServer chatserver;
	public LeaveMsg(IChatServer chatserver){
		this.chatserver = chatserver;
	}
	
	public IChatServer getChatServer(){
		return chatserver;
	}
}
