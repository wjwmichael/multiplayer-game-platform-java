package jw84_ym12_chatApp.model;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.UUID;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.IChatroom;
import common.ICmd2ModelAdapter;
import common.IUser;
import common.OurDataPacket;
import common.msg.IUserMsg;
import common.msg.chat.IAddMeMsg;
import common.msg.user.IInvite2RoomMsg;
import jw84_pc30_chatApp.util.AddMeMsg;
import jw84_ym12_chatApp.model.IUser2ModelAdapter;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacketAlgo;
import provided.extvisitor.IExtVisitorHost;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.RMIUtils;
import provided.util.IVoidLambda;

/**
 * A User class
 */
public class User implements IUser {
	/**
	 * The data packet algorithm
	 * algo in IUser do not deal with unknown data packets
	 */
	private DataPacketAlgo<Void, Object> userAlgo = new DataPacketAlgo<Void, Object>(null);
	/**
	 * output command used to put multiple strings up onto the view.
	 */
	private IVoidLambda<String> outputCmd = new IVoidLambda<String>() {

		@Override
		public void apply(String... params) {
			for (@SuppressWarnings("unused") String s : params) {
				
			}
		}
	};
	@SuppressWarnings("unused")
	private IUser remoteStub;
	
	private IUser userStub;
	/**
	 * Universally Unique IDentifier, used to create
	 * an ID value that is unique across different machines.
	 */
	private UUID uuid = UUID.randomUUID();
	/**
	 * record the binded port of this user object
	 */
	private int port;
	/**
	 * The name of the user
	 */
	private String name;
	
	/**
	 * The IP address
	 */
	private String ipAddr;
	
	/**
	 * A set of chatrooms
	 */
	private HashSet<IChatroom> chatrooms = new HashSet<IChatroom>();
	private Registry registry;
	private IRMIUtils rmiUtils;

	private IUser2ModelAdapter iUser2ModelAdapter;
	
	/**
	 * The constructor of User
	 * @param name - The user name
	 * @param ipAddr - The IP address
	 */
	public User(String name, String ipAddr,IUser2ModelAdapter iUser2ModelAdapter,int port){
		rmiUtils = new RMIUtils(outputCmd);
		registry = rmiUtils.getLocalRegistry();
		initDataPacketAlgo();
		this.name = name;
		this.ipAddr = ipAddr;
		this.iUser2ModelAdapter = iUser2ModelAdapter;
		this.port = port;
		initDataPacketAlgo();
		//chatrooms.add(new Chatroom("Default Room", null));
	}
    
    
    public void setUserStub(IUser stub){
    	this.userStub = stub;
    }
	/**
	 * initialize the datapacket algo in IUser
	 */
	public void initDataPacketAlgo() {
		userAlgo.setCmd(IInvite2RoomMsg.class,new AOurDataPacketAlgoCmd<IInvite2RoomMsg,IChatServer>(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 4858773642337113798L;



			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Void apply(Class<?> index, OurDataPacket<IInvite2RoomMsg, IChatServer> host, Object... params) {
				// TODO Auto-generated method stub
				IInvite2RoomMsg msg  = host.getData();
				IChatroom room = msg.getChatroom();
				if (chatrooms.contains(room)) {
                    return null;
                }
				chatrooms.add(room);
				//already in it,don't need to rejoin
				ChatServer server = new ChatServer(userStub,room);
				IChatServer chatStub = null;
				try {
					chatStub = (IChatServer) UnicastRemoteObject.exportObject(server, port);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iUser2ModelAdapter.getModel().setcntRoom(iUser2ModelAdapter.getModel().getcntRoom()+1);
				iUser2ModelAdapter.getModel().makeMiniMVC(room, server,chatStub);
				room.send(new OurDataPacket<IAddMeMsg,IChatServer>(IAddMeMsg.class,new AddMeMsg(),chatStub));
				room.addChatServer(chatStub);
				return null;
			}

		});
		
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getIP() {
		return this.ipAddr;
	}

	@Override
	public HashSet<IChatroom> getChatrooms() throws RemoteException {
		return this.chatrooms;
	}



	@Override
	public UUID getId() throws RemoteException {
		return this.uuid;
	}

	@Override
	public void connectBack(IUser userStub) throws RemoteException {
		// TODO Auto-generated method stub
		this.remoteStub = userStub;
	}
	@Override
	public <S> void receive(OurDataPacket<? extends IUserMsg, S> dp) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(dp.getClass());
		System.out.println(dp.getData().getClass());
		System.out.println(dp.getSender());
		dp.execute(userAlgo);
	}
}
