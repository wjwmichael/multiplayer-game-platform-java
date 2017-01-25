package pc30_jw84_gmap.utils;

import java.io.Serializable;

import gov.nasa.worldwind.geom.LatLon;

public interface ICmd2GameModelAdapter extends Serializable {
	
	public void setFlagAndDisableMine(double lat, double lon);

	public Integer getRevealedMinesCount();
}
