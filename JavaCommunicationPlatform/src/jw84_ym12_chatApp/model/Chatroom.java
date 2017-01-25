package jw84_ym12_chatApp.model;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.UUID;

import common.IChatServer;
import common.IChatroom;
import common.OurDataPacket;
import common.msg.IChatMsg;
import provided.datapacket.ADataPacket;

/**
 * Chatroom implementation of IChatRoom
 * @author Jiawei Wu
 */
public class Chatroom implements IChatroom {

	/**
	 * A serial versionUID
	 */
	private static final long serialVersionUID = -752478915258830536L;


	
	
	/**
	 * Universally Unique IDentifier, used to create
	 * an ID value that is unique across different machines.
	 */
	private UUID uuid = UUID.randomUUID();
	
	/**
	 * The name of the Chatroom
	 */
	private String name;
	
	/**
	 * A set of chatServer stubs in the chatroom
	 */
	private HashSet<IChatServer> chatServers = new HashSet<IChatServer>();
	
	/**
	 * The constructor of the Chatroom with a name and its creator (the default user)
	 * @param name - Chatroom name
	 * @param chatServer - The IChatServer associated with this chatroom
	 */
	public Chatroom(String name){
		this.name = name;
		//chatServers.add(chatServer);
	}
	
	public String toString() {
		return this.name;
	}
	
	/**
	 * Insures that unmarshalled copies of this object will 
	 * still be equal to the original.
	 * Two rooms with the same values may still not be equal.
	 */
	public boolean equals(Object o){
	  if(null != o) {
	    if( o instanceof Chatroom) {
	      return uuid.equals(((Chatroom)o).uuid);
	    }
	  }  
	  return false;
	}
	   
	/**
	 * Insures that unmarshalled copies still have the 
	 * same hashcode as the original
	 * Two rooms with the same values may still 
	 * not have the same hashcode.
	 */
	public int hashCode() {
	  return uuid.hashCode();
	}
	

	@Override
	public UUID getId() {
		return this.uuid;
	}

	@Override
	public String getName() {
		return this.name;
	}



	@Override
	public HashSet<IChatServer> getChatServers() {
		return this.chatServers;
	}

	@Override
	public boolean addChatServer(IChatServer chatServer) {
		
		return chatServers.add(chatServer);
	}

	@Override
	public boolean removeChatServer(IChatServer chatServer) {
		return chatServers.remove(chatServer);
	}

	@Override
	public <S> void send(OurDataPacket<? extends IChatMsg, S> dp) {
		if(chatServers.size()==0)return;
		(new Thread() {

            @Override
            public void run() {
                super.run();
                chatServers.iterator().forEachRemaining(
        				(chatServer) -> { 
        					try{
        						chatServer.receive(dp);
        					}
        					catch(RemoteException e)
        					{
        						e.printStackTrace();
        					}
        				});
            }
            
        }).start();
		
	}

}
