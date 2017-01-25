package jw84_ym12_chatApp.model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JPanel;

import common.IChatServer;
import common.IChatroom;
import common.IUser;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.chat.IAddMeMsg;
import common.msg.user.IInvite2RoomMsg;
import jw84_pc30_chatApp.util.AddMeMsg;
import jw84_pc30_chatApp.util.Invite2RoomMsg;
import jw84_ym12_chatApp.model.ChatServer;
import jw84_ym12_chatApp.model.Chatroom;
import jw84_ym12_chatApp.model.User;
import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.rmiUtils.*;
import provided.util.IVoidLambda;

/**
 * The model of the client system.
 * author: Jiawei Wu
 */
public class MainModel {

	/**
	 * output command used to put multiple strings up onto the view.
	 */




	private IVoidLambda<String> outputCmd = new IVoidLambda<String>() {

		@Override
		public void apply(String... params) {
			for (String s : params) {
				view.append(s + "\n");
			}
		}
	};

	
	/**
	 * The RMI Registry
	 */
	private Registry registry;
	private IUser userStub;
	/**
	 * Factory for the Registry and other uses.
	 */
	IRMIUtils rmiUtils = new RMIUtils(outputCmd);

	/**
	 * A counting number to set the name of each chat room
	 */
	private int cnt = 1;
	private int maxNumOfChatApps = 2;
	private int cntRoom = 1;
	private int cntPort = 0;
	private int user_port = 0;
	/**
	 * The user in this MainModel
	 */
	private User user;
	
	/**
	 * User name
	 */
	private String username;

	/**
	 * The chatServer stubs list 
	 */
	private HashSet<IChatServer> chatServers = new HashSet<IChatServer>();
	
	private HashSet<IChatServer> chatServerStubs = new HashSet<IChatServer>();

	/**
	 * Adapter to the view
	 */
	private IViewAdapter view;

	/**
	 * Adapter to the mini-View and main Model
	 */
	private HashMap<IChatroom,IMiniMVCAdapter> miniMVCAdpts = new HashMap<IChatroom, IMiniMVCAdapter>();

	private IUser2ModelAdapter iUser2ModelAdapter;


	/**
	 * Private class to decorate an IUser to override the equals() and hashCode() 
	 * methods so that a dictionary, e.g. Hashtable, can properly compare IUsers.
	 * @author swong
	 *
	 */
	private class ProxyChatServer implements IChatServer, Serializable {

		/**
		 * Required for proper serialization
		 */
		private static final long serialVersionUID = 5682755540794448769L; // regenerate this!
		@Override
		public IUser getUser() throws RemoteException {
			return stub.getUser();
		}
		/**
		 * The decoree
		 */
		private IChatServer stub;
		private IUser2ModelAdapter iUser2ModelAdapter;
		/**
		 * Constructor for the class
		 * @param stub The decoree
		 */
		public ProxyChatServer(IChatServer stub,IUser2ModelAdapter iUser2ModelAdapter){
			this.stub = stub;
			this.iUser2ModelAdapter = iUser2ModelAdapter;
		}

		/**
		 * Get the decoree
		 * @return the decoree
		 */
		@SuppressWarnings("unused")
		public IChatServer getStub() {
			return stub;
		}


		/**
		 * Overriden equals() method to simply compare hashCodes.
		 * @return  Equal if the hashCodes are the same.  False otherwise.
		 */
		@Override
		public boolean equals(Object other){
			return hashCode() == other.hashCode();
		}


		/**
		 * Overriden hashCode() method to create a hashcode from all the accessible values in IUser.
		 * @return a hashCode tied to the values of this IUser.	
		 */
		@Override
		public int hashCode(){
			try {
				// Only name is available for now.
				return stub.getUser().getId().hashCode();
			} catch (RemoteException e) {
				// Deal with the exception without throwing a RemoteException.
				System.err.println("ProxyStub.hashCode(): Error calling remote method on IUser stub: "+e);
				e.printStackTrace();
				return super.hashCode();
			}
		}

		/**
		 * Show the name and IP address of the user
		 */
		@Override
		public String toString() {
			try {
				return stub.getUser().getName() + "@" + stub.getUser().getIP();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public IChatroom getChatroom() throws RemoteException {
			return stub.getChatroom();
		}

		@Override
		public <S> void receive(OurDataPacket<? extends IChatMsg, S> dp) throws RemoteException {
			// TODO Auto-generated method stub
			stub.receive(dp);
		}

		
	}

	/**
	 * A ProxyChatServer object
	 */
	//private ProxyChatServer proxyChatServer;

	/**
	 * Constructor for the class
	 * 
	 * @param view
	 *            The adapter to the view.
	 * @param iUser2ModelAdapter 
	 */
	public MainModel(IViewAdapter view, IUser2ModelAdapter iUser2ModelAdapter) {
		this.view = view;
		this.iUser2ModelAdapter = iUser2ModelAdapter;
	}
	/**
	 * 
	 * @return the current num of rooms
	 */
	public int getcntRoom()
	{
		return this.cntRoom;
	}
	/**
	 * 
	 * @return the current num of used ports
	 */
	public int getcntPort()
	{
		return this.cntPort;
	}
	/**
	 * 
	 * @param cnt the new room num,increase when create new room
	 */
	public void setcntRoom(int cnt)
	{
		cntRoom = cnt;
	}
	/**
	 * 
	 * @param cnt the new port num,increase when create new room
	 */
	public void setcntPort(int cnt)
	{
		cntPort = cnt;
	}
	/**
	 * 
	 * @param room get the miniMVC adapter for the specified chat room in order to modify the GUI
	 * @return the mini MVC adapter
	 */
	public  IMiniMVCAdapter getMiniMVCAdpt(IChatroom room)
	{
		return this.miniMVCAdpts.get(room);
	}
	/**
	 * Starts the model by setting all the required RMI system properties,
	 * starts up the class server and installs the security manager.
	 */
	public void start(String args) {
			
		while (cntPort < maxNumOfChatApps) {
			if(args=="client") user_port = IUser.BOUND_PORT_CLIENT;
			if(args=="server") user_port = IUser.BOUND_PORT_SERVER;
			rmiUtils.startRMI(IRMI_Defs.CLASS_SERVER_PORT_SERVER + cntPort);

			try {
				view.setRemoteHost(rmiUtils.getLocalAddress()); //TODO Is this stored somewhere?z
				user = new User(username, rmiUtils.getLocalAddress(),iUser2ModelAdapter,user_port);

				registry = rmiUtils.getLocalRegistry();
				IUser stub = (IUser) UnicastRemoteObject.exportObject(user, user_port);
				this.userStub = stub;
				user.setUserStub(stub);
				view.append("Found registry: "+ registry + "\n");
				registry.rebind(IUser.BOUND_NAME, stub);
				System.out.println("MainModel.start(): " + user.getName() + "@" +user.getIP());
				IChatroom room = new Chatroom("Chatroom" + cntRoom);
				makeMiniMVC(room,new ChatServer(userStub,room),null);

				break;
			} catch (Exception e) {
				System.err.println(" Error getting local address: " + e+"In the port " + cntPort );
			}
			cntPort++;
		}

	}

	/**
	 * Connects to the given remote host and retrieves the stub to the ICompute object bound 
	 * to the ICompute.BOUND_NAME name in the remote Registry on port 
	 * IRMI_Defs.REGISTRY_PORT.  
	 * 
	 * @param remoteHost The IP address or host name of the remote server.
	 * @return  A status string on the connection.
	 */
	public String connectTo(String remoteHost) {
			try {
				view.append("Locating registry at " + remoteHost + "...\n");
				
				Registry remoteregistry = rmiUtils.getRemoteRegistry(remoteHost);
				view.append("Found registry: " + registry + "\n");
				IUser remoteUserStub = (IUser) remoteregistry.lookup(IUser.BOUND_NAME);
				remoteUserStub.connectBack(this.userStub);
				String friendInfo = remoteUserStub.getName()+"@"+remoteUserStub.getIP();
				view.addFriend(friendInfo);
				view.warning("Hello from"+remoteUserStub.getName());
				view.append("Found remote Compute object: " + userStub + "\n");
				setChatroomList(remoteUserStub.getChatrooms());
			} catch (Exception e) {
				view.append("Exception connecting to " + remoteHost + ": " + e
						+ "\n");
				e.printStackTrace();
				return "No connection established!";
			}
			return "Connection to " + remoteHost + " established!";

	}

	/**
	 * set the chatroom list displayed on the GUI
	 * @param chatrooms the chatroom list passed to the GUI
	 */
	private void setChatroomList(HashSet<IChatroom> chatrooms) {
		view.setChatroomList(chatrooms);
	}

	/**
	 * Stops the client by shutting down the class server.
	 */ 
	public void stop() {
		System.out.println("ClientModel.stop(): client is quitting.");
		try {
			rmiUtils.stopRMI();

		} catch (Exception e) {
			System.err
			.println("ClientModel.stop(): Error stopping class server: "
					+ e);
		}
		System.exit(0);
	}


	/**
	 * Make mini-MVC
	 * @param stub - A user stub
	 */
	public void makeMiniMVC(IChatroom chatroom,ChatServer chatServer,IChatServer chatStub) {
		
		try {
			view.append("MainModel.makeMiniMVC(): " + chatServer.getUser().getName() + "@" 
					+ chatServer.getUser().getIP() + "\n");
			System.out.println(cntPort);
			if(chatStub==null){
				 chatStub = (IChatServer) UnicastRemoteObject.exportObject(chatServer, 
							this.user_port);
				 chatroom.addChatServer(chatStub);
				 cntRoom++;
				 user.getChatrooms().add(chatroom);
			}
			
			
			HashSet<IChatServer> chatServerProxys = new HashSet<IChatServer>();
			for(IChatServer chatserver: chatServer.getChatroom().getChatServers()){
				IChatServer proxyChatServer = new ProxyChatServer(chatserver,iUser2ModelAdapter);
				chatServerProxys.add(proxyChatServer);
			}
			IMiniMVCAdapter miniMVCAdpt = view.makeMiniMVC(chatServer, chatStub,chatServerProxys);
			miniMVCAdpts.put(chatroom,miniMVCAdpt);



		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Close a chat room,if the cnt of chatroom reduced  to zero, just stop the model and close the GUI
	 */
	public void closeRoom(JPanel miniView) {
		view.closeRoom(miniView);
		cnt--;
		if (cnt == 1){
			stop();
		}
	}
	
	
	/**
	 * Join the selected remote chatroom
	 * @param remoteChatroom - a remote chatroom we would like to join
	 */
	public void joinRoom(IChatroom remoteChatroom) {
		(new Thread(() -> {
		if (remoteChatroom == null) {
			view.warning("Please first connect and select a chatroom!");
			return;
		}
		HashSet<IChatServer> remoteChatServers = remoteChatroom.getChatServers();
		
		//Show if the user has already been in the chatroom
		for (IChatServer localChatStub:chatServerStubs) {
			ProxyChatServer chatStubProxy = new ProxyChatServer(localChatStub,iUser2ModelAdapter);
			for (IChatServer remoteChatStub:remoteChatServers) {
				ProxyChatServer chatServerProxy = new ProxyChatServer(remoteChatStub,iUser2ModelAdapter);
				if (chatStubProxy.equals(chatServerProxy)){
					view.warning("You are already in this chatroom!");
				return;
				}
			}
		}
		
		
		IChatroom chatroom = new Chatroom("Chatroom" + cntRoom);

		ChatServer chatServer = new ChatServer(this.userStub, chatroom);
		chatServers.add(chatServer); //Store every local chatServer in the private list of MainModel

		try {
			view.append("MainModel.makeMiniMVC(): " + chatServer.getUser().getName() + "@" 
					+ chatServer.getUser().getIP() + "\n");
			IChatServer chatStub = (IChatServer) UnicastRemoteObject.exportObject(chatServer, 
					this.user_port);
			chatServerStubs.add(chatStub);
			for (IChatServer remoteChatServer: remoteChatServers){
				System.out.println(remoteChatServer.toString());
				chatroom.addChatServer(remoteChatServer);
				view.append(remoteChatServer.getChatroom().getName()+ "Size:"+
						remoteChatServer.getChatroom().getChatServers().size() +"\n");
			}
			OurDataPacket<IAddMeMsg,IChatServer> dp = new OurDataPacket<IAddMeMsg,IChatServer>(IAddMeMsg.class,new AddMeMsg(),chatStub);

			System.out.println(dp.getData().getClass());
			chatroom.addChatServer(chatStub);
			//Add the local chatStub into the chatroom
			user.getChatrooms().add(chatroom);
			HashSet<IChatServer> chatServerProxys = new HashSet<IChatServer>();
			for (IChatServer stub: chatroom.getChatServers()){
				ProxyChatServer proxyChatServer = new ProxyChatServer(stub,iUser2ModelAdapter);
				chatServerProxys.add(proxyChatServer);
			}
			
			IMiniMVCAdapter miniMVCAdpt = view.makeMiniMVC(chatServer, chatStub,chatServerProxys);
			miniMVCAdpts.put(chatroom,miniMVCAdpt);
			cntRoom++;
			view.removeChatroomList();
			chatroom.send(new OurDataPacket<IAddMeMsg,IChatServer>(IAddMeMsg.class,new AddMeMsg(),chatStub));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		})).start(); // start the new thread
	}
	/**
	 * 
	 * @return the model adapter used to give user ability to comm with model
	 */
	public IUser2ModelAdapter getModelAdpt(){
		return iUser2ModelAdapter;
	}
	/**
	 * 
	 * @param room the local room which the remote user will be invited into 
	 * @param remoteHost the IP of the people you want to invite 
	 * @param chatstub 
	 */
	public void invite(IChatroom room,String remoteHost, IChatServer chatstub){
		(new Thread(() -> {

		view.append("Locating registry at " + remoteHost + "...\n");
		Registry registry = rmiUtils.getRemoteRegistry(remoteHost);
		view.append("Found registry: " + registry + "\n");
		try {
			for(IChatServer chatserver:room.getChatServers()){
				String currentIP = chatserver.getUser().getIP();
				if(currentIP.equals(remoteHost))
					{
						view.warning("This user is already in the room");
						return;
					}
			}
			
			IUser userStub = (IUser) registry.lookup(IUser.BOUND_NAME);
			System.out.println(chatstub.toString());
			OurDataPacket<IInvite2RoomMsg,IChatServer> dp = new OurDataPacket<IInvite2RoomMsg,IChatServer>(IInvite2RoomMsg.class,new Invite2RoomMsg(room),chatstub);
			System.out.println(dp.getData().getClass());
			
			userStub.receive(new OurDataPacket<IInvite2RoomMsg,IChatServer>(IInvite2RoomMsg.class,new Invite2RoomMsg(room),chatstub));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		})).start(); // start the new thread
		
	}

	/*
	 * create new room in the GUI, response to the adapter call
	 */
	public void createRoom() {
		// TODO Auto-generated method stub
		IChatroom room = new Chatroom("Chatroom" + cntRoom);
		makeMiniMVC(room,new ChatServer(this.userStub,room),null);
	}
	/**
	 * set the user name when the program started 
	 * @param username name choice
	 */
	public void setUsername(String username) {
		// TODO Auto-generated method stub
		this.username = username;		
	}


}
