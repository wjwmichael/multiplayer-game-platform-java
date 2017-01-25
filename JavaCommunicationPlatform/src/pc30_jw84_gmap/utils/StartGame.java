package pc30_jw84_gmap.utils;

import java.util.UUID;

import common.IChatServer;
import common.msg.IChatMsg;
import provided.datapacket.DataPacket;

public class StartGame implements IChatMsg {

    /**
     * The generated serial version UID
     */
	private static final long serialVersionUID = 6792665386262706142L;

	/**
	 * Store a game factory
	 */
	private mapFac gameFac;
	
	/**
	 * UUID of this message
	 */
	private UUID id = UUID.randomUUID();
	
	
	/**
	 * Constructor
	 * @param gFac Game factory
	 */
	public StartGame(mapFac gFac) {
		gameFac = gFac;
	}

	/**
	 * Get the game factory
	 * @return Return a game factory
	 */
	public mapFac getGameFac() {
		return gameFac; 
	}
}