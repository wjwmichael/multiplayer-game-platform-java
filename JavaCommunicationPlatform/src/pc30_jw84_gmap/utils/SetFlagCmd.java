package pc30_jw84_gmap.utils;

import java.util.UUID;

import javax.swing.SwingUtilities;

import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.ICmd2ModelAdapter;
import common.OurDataPacket;
import gov.nasa.worldwind.geom.LatLon;
import provided.mixedData.MixedDataKey;

public class SetFlagCmd extends AOurDataPacketAlgoCmd<SetFlag,IChatServer> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6516114227739103543L;
	
	private UUID startid;
	
	private transient ICmd2ModelAdapter adpt;
	
	public SetFlagCmd(UUID startid) {
		this.startid = startid;
	}
	
	@Override
	public Void apply(Class<?> index, OurDataPacket<SetFlag, IChatServer> host, Object... params) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MixedDataKey<ICmd2GameModelAdapter> key = new MixedDataKey<ICmd2GameModelAdapter>(startid, "Cmd2GameModelAdapter", ICmd2GameModelAdapter.class);
				ICmd2GameModelAdapter cmd2GameModelAdpt = adpt.getFromLocalDict(key);
				cmd2GameModelAdpt.setFlagAndDisableMine(host.getData().getLat(), host.getData().getLon());
			}
			
		});
		
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		// TODO Auto-generated method stub
		adpt = cmd2ModelAdpt;
	}

}
