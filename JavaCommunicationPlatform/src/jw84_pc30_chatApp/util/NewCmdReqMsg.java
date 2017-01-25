package jw84_pc30_chatApp.util;

import java.util.UUID;

import common.msg.chat.INewCmdReqMsg;

public class NewCmdReqMsg implements INewCmdReqMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5067852894440963874L;

	Class<?> index;
	
	
	UUID uuid;
	
	public NewCmdReqMsg(Class<?> index, UUID id){
		this.index = index;
		uuid = id;
	}
	@Override
	public Class<?> getReqClassIdx() {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return uuid;
	}

}
