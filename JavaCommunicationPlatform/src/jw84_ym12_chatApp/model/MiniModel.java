package jw84_ym12_chatApp.model;

import java.awt.Container;
import java.io.FileInputStream;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.IChatroom;
import common.ICmd2ModelAdapter;
import common.IComponentFactory;
import common.IUser;
import common.OurDataPacket;
import common.msg.IChatMsg;
import common.msg.chat.IAddCmdMsg;
import common.msg.chat.IAddMeMsg;
import common.msg.chat.ILeaveMsg;
import common.msg.chat.INewCmdReqMsg;
import common.msg.chat.ITextMsg;
import jw84_pc30_chatApp.util.AddCmdMsg;
import jw84_pc30_chatApp.util.LeaveMsg;
import jw84_pc30_chatApp.util.NewCmdReqMsg;
import jw84_pc30_chatApp.util.TextMsg;
import jw84_ym12_chatApp.view.gameFrame;
import pc30_jw84_gmap.utils.ICmd2GameModelAdapter;
import pc30_jw84_gmap.utils.ILeaveGameMsg;
import pc30_jw84_gmap.utils.SetFlag;
import pc30_jw84_gmap.utils.SetFlagCmd;
import pc30_jw84_gmap.utils.StartGame;
import pc30_jw84_gmap.utils.StartGameCmd;
import pc30_jw84_gmap.utils.mapFac;
import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
/**
 * A mini-Model
 * @author Weili
 *
 */
public class MiniModel {
	/**
	 * Adapter to the mini-View and main Model
	 */


	private IMiniViewAdapter miniViewAdpt;
	/**
	 * The data packet algorithm
	 */
	private DataPacketAlgo<Void, Object> dataPacketAlgo = new DataPacketAlgo<>(null);
	private MixedDataDictionary dict = new MixedDataDictionary();
	private IChatServer serverStub;
	private ChatServer chatServer;
	protected ICmd2ModelAdapter cmd2ModelAdpt ;
	private UUID uuid_unknownDataList;
	protected MixedDataKey<HashMap> mddKey;
	
	/**
	 * Private class to decorate an IUser to override the equals() and hashCode() 
	 * methods so that a dictionary, e.g. Hashtable, can properly compare IUsers.
	 * @author swong
	 *
	 */
	private class ProxyChatServer implements IChatServer {
		@Override
		public IChatroom getChatroom() throws RemoteException {
			return stub.getChatroom();
		}
		/**
		 * Required for proper serialization
		 */
		@SuppressWarnings("unused")
		private static final long serialVersionUID = 5682755540794448769L; // regenerate this!

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
				String usernameToPrint = stub.getUser().getName();
				String ipToPrint = stub.getUser().getIP();
				return usernameToPrint + "@" + ipToPrint;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}



		@Override
		public IUser getUser() throws RemoteException {
			// TODO Auto-generated method stub
			return stub.getUser();
		}

		@Override
		public <S> void receive(OurDataPacket<? extends IChatMsg, S> dp) throws RemoteException {
			// TODO Auto-generated method stub
			stub.receive(dp);
		}


	}

	/**
	 * Initialize data packet algorithm
	 */
	public void initDataPacketAlgo() {
		dataPacketAlgo.setDefaultCmd(new AOurDataPacketAlgoCmd<Object,IChatServer>() {

			/**
			 * The generated serial version UID
			 */
			private static final long serialVersionUID = -6066750783627231641L;

			@Override
			public Void apply(Class<?> index, OurDataPacket<Object,IChatServer> host, Object... params) {
				// TODO Auto-generated method stub
				System.out.println("params.length"+params.length);
				
				try {
					UUID id = UUID.randomUUID();//UUID for this unknown data packet 
					 // Java will only allow us to specify the raw List type.				     
					// get list if already in MDD
					HashMap<UUID,OurDataPacket<Object,IChatServer>> dpList = cmd2ModelAdpt.getFromLocalDict(mddKey); // may generate a type-cast warning here
					     
					if(null == dpList) { // in case there's nothing in the MDD yet
					    dpList = new HashMap<UUID,OurDataPacket<Object,IChatServer>>();
					}
					     
					dpList.put(id,host);
					cmd2ModelAdpt.putIntoLocalDict(mddKey, dpList); // put the list back into the MDD
					host.getSender().receive(new OurDataPacket<INewCmdReqMsg,IChatServer>(INewCmdReqMsg.class,new NewCmdReqMsg(index,id), serverStub) );					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					miniViewAdpt.append("System:","We don't know the message type and remote user cannot provide us with the right command");
					e.printStackTrace();
				}
//				return host.execute(dataPacketAlgo, params);
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}



		});

		dataPacketAlgo.setCmd(IAddCmdMsg.class,new AOurDataPacketAlgoCmd<IAddCmdMsg,IChatServer>(){

			@Override
			public Void apply(Class<?> index, OurDataPacket<IAddCmdMsg,IChatServer> host, Object... params) {
				// TODO Auto-generated method stub
				AOurDataPacketAlgoCmd<?,IChatServer> cmd = host.getData().getCmd();
				UUID id = host.getData().getUUID();			     
				// get list if already in MDD
				HashMap<UUID,OurDataPacket<Object,IChatServer>> dpList = cmd2ModelAdpt.getFromLocalDict(mddKey); // may generate a type-cast warning here
				OurDataPacket<Object,IChatServer> unknownDataPacket = dpList.get(id);
				cmd.setCmd2ModelAdpt(cmd2ModelAdpt);
				dataPacketAlgo.setCmd(host.getData().getClassIdx(), cmd);
				//dataPacketAlgo.setCmd(host.getData().getClass(), cmd);
				return unknownDataPacket.execute(dataPacketAlgo,params);
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}
			
		});

		dataPacketAlgo.setCmd(INewCmdReqMsg.class, new AOurDataPacketAlgoCmd<INewCmdReqMsg,IChatServer>(){

			@Override
			public Void apply(Class<?> index, OurDataPacket<INewCmdReqMsg,IChatServer> host, Object... params) {
				// TODO Auto-generated method stub
				Class<?> ind = host.getData().getReqClassIdx();
				AOurDataPacketAlgoCmd<?,IChatServer> cmd  = (AOurDataPacketAlgoCmd<?,IChatServer>) dataPacketAlgo.getCmd(ind);
				UUID id = host.getData().getUUID();
				try {
					host.getSender().receive(new OurDataPacket<IAddCmdMsg,IChatServer>(IAddCmdMsg.class,new AddCmdMsg(id,cmd,ind),serverStub));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}
			
			
			
		});

		dataPacketAlgo.setCmd(IAddMeMsg.class,new AOurDataPacketAlgoCmd<IAddMeMsg,IChatServer>() {

			@Override
			public Void apply(Class<?> index, OurDataPacket<IAddMeMsg, IChatServer> host, Object... params) {
				// TODO Auto-generated method stub
				IChatServer chatstub = host.getSender();
				try {
					IChatroom room = chatServer.getChatroom();
					room.addChatServer(chatstub);
					refresh();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		dataPacketAlgo.setCmd(ILeaveMsg.class,new AOurDataPacketAlgoCmd<ILeaveMsg,IChatServer>(){

			@Override
			public Void apply(Class<?> index, OurDataPacket<ILeaveMsg, IChatServer> host, Object... params) {
				// TODO Auto-generated method stub
				IChatServer removed_server = host.getSender();
				try {
					System.out.println("");
					chatServer.getChatroom().removeChatServer(removed_server);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		StartGameCmd startGameCmd = new StartGameCmd();
		startGameCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
		
		dataPacketAlgo.setCmd(StartGame.class, startGameCmd);
		
		SetFlagCmd setFlagCmd = new SetFlagCmd(startGameCmd.getUUID());
		setFlagCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
		dataPacketAlgo.setCmd(SetFlag.class, setFlagCmd);
		
		dataPacketAlgo.setCmd(ILeaveGameMsg.class, new AOurDataPacketAlgoCmd<ILeaveGameMsg,IChatServer>(){

			@Override
			public Void apply(Class<?> index, OurDataPacket<ILeaveGameMsg, IChatServer> host, Object... params) {
				// TODO Auto-generated method stub
				Integer PlayersCount = cmd2ModelAdpt.getFromLocalDict(new MixedDataKey<Integer>(startGameCmd.getUUID(), "PlayersCount", Integer.class));
				if (PlayersCount == null) {
					IChatroom room = null;
					try {
						room = chatServer.getChatroom();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					HashSet<IChatServer> chatServers = room.getChatServers();
					
					MixedDataKey<Integer> key = new MixedDataKey<Integer>(startGameCmd.getUUID(), "PlayersCount", Integer.class);
					PlayersCount = new Integer(chatServers.size() - 1);
					cmd2ModelAdpt.putIntoLocalDict(key, PlayersCount);
				}
				
				MixedDataKey<Integer> leavedCountKey = new MixedDataKey<Integer>(startGameCmd.getUUID(), "LeavedCount", Integer.class);
				Integer LeavedCount = cmd2ModelAdpt.getFromLocalDict(leavedCountKey);
				if (LeavedCount == null) {
					LeavedCount = new Integer(1);
					cmd2ModelAdpt.putIntoLocalDict(leavedCountKey, LeavedCount);
				} else {
					cmd2ModelAdpt.putIntoLocalDict(leavedCountKey, new Integer(LeavedCount.intValue() + 1));
				}
				
				Integer currentLeavedCount = cmd2ModelAdpt.getFromLocalDict(leavedCountKey);
				
				System.out.println("currentLeavedCount is: " + currentLeavedCount);
				System.out.println("PlayersCount is: " + PlayersCount);
				System.out.println("**********************************************");
				
				if (currentLeavedCount.intValue() == PlayersCount.intValue()) {
					List<Integer> scoreHistory = cmd2ModelAdpt.getFromLocalDict(new MixedDataKey<List>(startGameCmd.getUUID(), "ScoreHistory", List.class));
					if (scoreHistory == null) {
						scoreHistory = new ArrayList<Integer>();
						cmd2ModelAdpt.putIntoLocalDict(new MixedDataKey<List>(startGameCmd.getUUID(), "ScoreHistory", List.class), scoreHistory);
					}
					
					MixedDataKey<ICmd2GameModelAdapter> c2GadptKey = new MixedDataKey<ICmd2GameModelAdapter>(startGameCmd.getUUID(), "Cmd2GameModelAdapter", ICmd2GameModelAdapter.class);
					ICmd2GameModelAdapter cmd2GameModelAdpt = cmd2ModelAdpt.getFromLocalDict(c2GadptKey);
					Integer revealedMinesCount = cmd2GameModelAdpt.getRevealedMinesCount();
					if (revealedMinesCount == null) {
						System.out.println("Error here");
					}
					scoreHistory.add(revealedMinesCount);
					
					int rank = 1;
					for (Integer score: scoreHistory) {
						if (score.intValue() > revealedMinesCount.intValue()) {
							rank++;
						}
					}
					
					Integer rankResult = new Integer(rank);
					cmd2ModelAdpt.sendMsg2LocalChatroom(ITextMsg.class, new TextMsg("Your team's rank is " + rankResult.toString()));
				}
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub
				
			}
			
		});
		dataPacketAlgo.setCmd(ITextMsg.class, new AOurDataPacketAlgoCmd<ITextMsg,IChatServer>() {

			/**
			 * The generated serial version UID
			 */
			private static final long serialVersionUID = -2319394090197028702L;


			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				
			}

			@Override
			public Void apply(Class<?> index, OurDataPacket<ITextMsg,IChatServer> host, Object... params) {
				// TODO Auto-generated method stub
				try {
					miniViewAdpt.append(host.getSender().getUser().getName(), host.getData().getContent());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				return null;
			}
		});
	}		
	/**
	 * Constructor for the class
	 * @param miniViewAdpt - The adapter to the view.
	 */
	public MiniModel(IMiniViewAdapter miniViewAdpt) {
		this.miniViewAdpt = miniViewAdpt;
	}

	/**
	 * Start the mini-Model
	 */
	public void start(ChatServer chatServer,IChatServer stub) {
		this.chatServer = chatServer;
		this.serverStub = stub;
		this.uuid_unknownDataList = UUID.randomUUID();
		mddKey = new MixedDataKey<HashMap>(uuid_unknownDataList, uuid_unknownDataList.toString(), HashMap.class);
		cmd2ModelAdpt = new ICmd2ModelAdapter(){

			@Override
			public void buildComponentInScrollable(IComponentFactory fac) {
				// TODO Auto-generated method stub
				JComponent comp = fac.make();
				Container game = new gameFrame();
				game.add(comp);
				game.revalidate();
			}

			@Override
			public void buildComponentInNonScrollable(IComponentFactory fac) {
				// TODO Auto-generated method stub
				JComponent comp = fac.make();
				Container game = new gameFrame();
				game.add(comp);
				game.revalidate();
				
			}

			@Override
			public <T> boolean putIntoLocalDict(MixedDataKey<T> key, T value) {
				// TODO Auto-generated method stub
				if(dict==null)return false;
				dict.put(key, value);
				return true;
			}

			@Override
			public <T> T getFromLocalDict(MixedDataKey<T> key) {
				// TODO Auto-generated method stub
				return dict.get(key);
			}

			@Override
			public <T extends IChatMsg> void sendMsg2LocalChatroom(Class<T> index, T msg) {
				// TODO Auto-generated method stub
				sendMsg(index,msg);
			}

			@Override
			public IChatServer getChatServer() {
				// TODO Auto-generated method stub
				return serverStub;
			}
			
		};
		initDataPacketAlgo();
		chatServer.setDataPacketAlgo(dataPacketAlgo);
	}
	
/**
 * 
 * @return the current chatserver list in the specified chatroom
 */
	
	public HashSet<IChatServer> getChatStubs(){
		HashSet<IChatServer> chatServerProxys = new HashSet<IChatServer>();
		try {
			HashSet<IChatServer> chatStubs = chatServer.getChatroom().getChatServers();
			System.out.println("chatStubs: " + chatStubs.size());
			for (IChatServer stub: chatStubs){
				ProxyChatServer proxyChatServer = new ProxyChatServer(stub,this.miniViewAdpt.getModel().getModelAdpt());
				chatServerProxys.add(proxyChatServer);
			}
			System.out.println("chatServerProxys: " + chatServerProxys.size());
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return chatServerProxys;
	}
	
/**
 * close the according chatroom
 * @param miniView
 */
	public void closeRoom(JPanel miniView) {
		miniViewAdpt.closeRoom(miniView);
		
		try {
			chatServer.getChatroom().removeChatServer(serverStub);
			sendMsg(ILeaveMsg.class,new LeaveMsg(serverStub));
			chatServer.getUser().getChatrooms().remove(chatServer.getChatroom());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public <T extends IChatMsg> void sendMsg(Class<T> index, T msg){
		try {
			IChatroom room = chatServer.getChatroom();
			room.send(new OurDataPacket<T,IChatServer>(index,msg,serverStub));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Send the message
	 * @param text - the data content
	 */
	public void sendTextMsg(String text) {
		sendMsg(ITextMsg.class,new TextMsg(text));
		
	}
	
	public void sendMap(){
		IChatroom room = null;
		try {
			room = chatServer.getChatroom();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HashSet<IChatServer> chatServers = room.getChatServers();
		(new Thread() {

            @Override
            public void run() {
                super.run();
                chatServers.iterator().forEachRemaining(
        				(chatServer) -> { 
        					try{
        						chatServer.receive(new OurDataPacket<StartGame,IChatServer>(StartGame.class,new StartGame(new mapFac(serverStub,chatServer)),serverStub));
        					}
        					catch(RemoteException e)
        					{
        						e.printStackTrace();
        					}
        				});
            }
            
        }).start();		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendMsg(SetFlag.class, new SetFlag(0.0, 0.0));
	}

	/**
	 * ask view to refresh the current user list of the chatroom
	 */
	public void refresh() {
		// TODO Auto-generated method stub
		miniViewAdpt.refresh();
	}
	/**
	 * send a selected image to the people in chatroom
	 * @param imgPath
	 */
	public IChatServer getServerStub() {
		// TODO Auto-generated method stub
		return this.serverStub;
	}

	
}
