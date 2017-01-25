package pc30_jw84_gmap.utils;

import java.util.UUID;

import common.msg.IChatMsg;
import gov.nasa.worldwind.geom.LatLon;

public class SetFlag implements IChatMsg {

    /**
     * The generated serial version UID
     */
	private static final long serialVersionUID = 6792665386262706142L;

	/**
	 * Store a game factory
	 */
	private double lat;
	
	private double lon;
	
	/**
	 * UUID of this message
	 */
	private UUID id = UUID.randomUUID();
	
	
	/**
	 * Constructor
	 * @param gFac Game factory
	 */
	public SetFlag(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * Get the game factory
	 * @return Return a game factory
	 */
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
}
