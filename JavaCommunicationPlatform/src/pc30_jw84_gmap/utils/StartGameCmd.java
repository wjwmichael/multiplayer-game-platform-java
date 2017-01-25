package pc30_jw84_gmap.utils;

import java.util.HashMap;
import java.util.UUID;

import javax.swing.SwingUtilities;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.ICmd2ModelAdapter;
import common.OurDataPacket;
import common.msg.chat.ITextMsg;
import jw84_pc30_chatApp.util.TextMsg;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.mixedData.MixedDataKey;

public class StartGameCmd extends AOurDataPacketAlgoCmd<StartGame,IChatServer> {

    /**
     * The generated serial version UID
     */
	private static final long serialVersionUID = -2177811586536199119L;

	/**
	 * Command to chat model adapter
	 */
	private transient ICmd2ModelAdapter adpt;
	
	private UUID id = UUID.randomUUID();
	/**
	 * Constructor
	 * @param id UUID
	 */
	public StartGameCmd() {
	}
	
	/**
     * The apply method
     * @param index The index
     * @param host The data packet
     * @param params The sender
     */


	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		// TODO Auto-generated method stub
		adpt = cmd2ModelAdpt;
		
	}
	@Override
	public Void apply(Class<?> index, OurDataPacket<StartGame, IChatServer> host, Object... params) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				host.getData().getGameFac().setCmd2ModelAdapter(adpt);
				ICmd2GameModelAdapter cmd2GameModelAdpt = host.getData().getGameFac().makeGame();
				MixedDataKey<ICmd2GameModelAdapter> key = new MixedDataKey<ICmd2GameModelAdapter>(id, "Cmd2GameModelAdapter", ICmd2GameModelAdapter.class);
				adpt.putIntoLocalDict(key, cmd2GameModelAdpt);

				MixedDataKey<Integer> leavedCountKey = new MixedDataKey<Integer>(id, "LeavedCount", Integer.class);
				Integer LeavedCount = adpt.getFromLocalDict(leavedCountKey);
				if (LeavedCount == null) {
					LeavedCount = new Integer(0);
					adpt.putIntoLocalDict(leavedCountKey, LeavedCount);
				} else {
					adpt.putIntoLocalDict(leavedCountKey, new Integer(0));
				}
				
				
			}
			
		});
		return null;
	}
	
	public UUID getUUID() {
		return id;
	}
}


