package pc30_jw84_gmap.view;

import java.io.Serializable;

public interface IView2ModelAdapter<CBType> extends Serializable{
	public void goLatLong(String latitude, String longitude);
	public void goPlace(CBType o);
}
