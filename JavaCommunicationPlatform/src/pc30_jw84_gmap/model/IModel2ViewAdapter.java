package pc30_jw84_gmap.model;

import gov.nasa.worldwind.layers.Layer;

public interface IModel2ViewAdapter {
	public void addPlace(Place p);
	public void show(Layer layer);
	public void hide(Layer layer);
	public void postBottom(String str);
	public void postTop(String str);
	public void removeRightClickListener();
	public void updateCountDown(int i);
}

