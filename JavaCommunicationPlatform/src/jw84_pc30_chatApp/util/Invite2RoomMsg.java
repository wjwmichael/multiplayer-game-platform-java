package jw84_pc30_chatApp.util;

import common.IChatroom;
import common.msg.user.IInvite2RoomMsg;

public class Invite2RoomMsg implements IInvite2RoomMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7505837338712740480L;
	IChatroom chatroom;
	public Invite2RoomMsg(IChatroom chatroom){
		this.chatroom =chatroom;
	}
	@Override
	public IChatroom getChatroom() {
		// TODO Auto-generated method stub
		return chatroom;
	}

}
