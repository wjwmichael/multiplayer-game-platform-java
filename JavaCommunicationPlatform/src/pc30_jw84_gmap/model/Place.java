package pc30_jw84_gmap.model;

import java.io.Serializable;

import gov.nasa.worldwind.geom.Position;

public class Place implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5851560219109816206L;
	private String   _name;
	private Position _pos;
	
	public Place(String name, Position pos) {
		_name = name;
		_pos = pos;
	}
	
	public Position getPosition() {
		return _pos;
	}
	
	public String toString() {
		return _name;
	}
}
