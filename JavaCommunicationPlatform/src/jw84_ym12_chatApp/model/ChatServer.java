package jw84_ym12_chatApp.model;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;

import common.IChatServer;
import common.IChatroom;
import common.IUser;
import common.OurDataPacket;
import common.msg.IChatMsg;
import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacketAlgo;

/**
 * A specific implementation of IChatServer
 * @author Jiawei Wu
 */
public class ChatServer implements IChatServer,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5307704384173089257L;
    /**
     * user of this chatserver
     */
	private IUser user;
	/**
	 * the specified chatroom of this chatserver
	 */
	private IChatroom chatroom;
	/**
	 * the datapacket visitor of this chatroom
	 */
	DataPacketAlgo<Void, Object> dataPacketAlgo = new DataPacketAlgo<Void, Object> (null); 
	
	
	
	/**
	 * The constructor of ChatServer which needs its associated user and chatroom
	 * @param user - The user object that hosts the chatroom and chatServer
	 * @param chatroom - The chatroom that is associated with the chatServer we want to construct
	 */
	public ChatServer(IUser user, IChatroom chatroom) {
		this.user = user;
		this.chatroom = chatroom;
	}
	
	public void setDataPacketAlgo(DataPacketAlgo<Void, Object> algo){
		dataPacketAlgo = algo;
	}
	
	@Override
	public IUser getUser() throws RemoteException {
		return this.user;
	}

	@Override
	public IChatroom getChatroom() throws RemoteException {
		return this.chatroom;
	}


	@Override
	public <S> void receive(OurDataPacket<? extends IChatMsg, S> dp) throws RemoteException {
		// TODO Auto-generated method stub
		dp.execute(dataPacketAlgo, this);
	}

}
