package pc30_jw84_gmap.utils;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.UUID;

import common.IChatServer;
import common.ICmd2ModelAdapter;
import common.OurDataPacket;
import pc30_jw84_gmap.controller.MapController;

public class mapFac implements Serializable{

    /**
     * The generated serial version UID
     */
	private static final long serialVersionUID = 2679156251583164401L;
	
	/**
	 * Stub connected to server
	 */
	private IChatServer sStub;

	
	/**
	 * Player's IChatUser stub
	 */
	private IChatServer pStub;
	
	private transient ICmd2ModelAdapter adpt;
	
	public void setCmd2ModelAdapter(ICmd2ModelAdapter adpt) {
		this.adpt = adpt;
	}
	
	/**
	 * Constructor
	 * @param sStub Server's IChatUser stub
	 * @param pStub Player's IChatUser stub
	 * @return 
	 */
	public mapFac(IChatServer sStub, IChatServer pStub) {
		this.sStub = sStub;
		this.pStub = pStub;
	}
	
	/**
	 * Adapter to the game model
	 * @return Return an adapter to the current running game
	 */
	public ICmd2GameModelAdapter makeGame() {
		MapController ctrl = new MapController();
		IGame2CmdAdpt Game2CmdAdpt  = ()->{
			try {
				sStub.receive(new OurDataPacket<ILeaveGameMsg,IChatServer>(ILeaveGameMsg.class,new LeaveGameMsg(),pStub));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		ctrl.makeMapMVC(adpt,Game2CmdAdpt);
		ctrl.startMap();
		ICmd2GameModelAdapter cmd2GameModelAdpt = ctrl.getCmd2GameModelAdpt();
		return cmd2GameModelAdpt;
	}

	public IChatServer getServer() {
		// TODO Auto-generated method stub
		return sStub;
	}
}

