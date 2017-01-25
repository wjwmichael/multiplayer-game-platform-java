package jw84_pc30_chatApp.util;

import java.util.UUID;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.msg.chat.IAddCmdMsg;

public class AddCmdMsg implements IAddCmdMsg{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2933511255809449771L;
	UUID uuid;
	AOurDataPacketAlgoCmd<?,IChatServer> cmd;
	Class<?> index;
	
	public AddCmdMsg(UUID uuid,AOurDataPacketAlgoCmd<?,IChatServer> cmd,Class<?> index){
		this.uuid = uuid;
		this.cmd = cmd;
		this.index = index;
	}
	
	
	@Override
	public AOurDataPacketAlgoCmd<?,IChatServer> getCmd() {
		// TODO Auto-generated method stub
		return cmd;
	}

	@Override
	public Class<?> getClassIdx() {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return uuid;
	}

}
