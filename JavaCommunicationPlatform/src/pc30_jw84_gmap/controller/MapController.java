package pc30_jw84_gmap.controller;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;

import javax.swing.SwingUtilities;

import common.ICmd2ModelAdapter;
import map.IRightClickAction;
import map.MapLayer;
import pc30_jw84_gmap.model.IModel2ViewAdapter;
import pc30_jw84_gmap.model.MapModel;
import pc30_jw84_gmap.model.Place;
import pc30_jw84_gmap.utils.ICmd2GameModelAdapter;
import pc30_jw84_gmap.utils.IGame2CmdAdpt;
import pc30_jw84_gmap.view.AppFrame;
import pc30_jw84_gmap.view.AppStartFrame;
import pc30_jw84_gmap.view.IAppStart2Controller;
import pc30_jw84_gmap.view.IView2ModelAdapter;

public class MapController implements  Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4404055835242606582L;
	private AppFrame<Place> _view;
	private MapModel _model;
	
	public MapController() {
	}
	
	public void makeMapMVC(ICmd2ModelAdapter adpt, IGame2CmdAdpt game2CmdAdpt) {
		_view  = new AppFrame<Place>(new IView2ModelAdapter<Place>() {
			public void goPlace(Place p) {
				_view.setPosition(p.getPosition());
			}
			public void goLatLong(String latitude, String longitude) {
				try {
					_view.setPosition(Position.fromDegrees(Double.parseDouble(latitude), Double.parseDouble(longitude), 4000));
				} catch (Exception e) {
					System.out.println("Improper latitude, longitude: " + latitude + ", " + longitude);
				}
			}
		}, new IRightClickAction() {
			public void apply(Position p) {
				_model.click(p);				
			}
		});
		_model = new MapModel(adpt, game2CmdAdpt, new IModel2ViewAdapter() {
			public void addPlace(Place p) {
				_view.addPlace(p);
			}
			@Override
			public void show(Layer layer) {
				_view.addMapLayer(layer);
			}
			@Override
			public void hide(Layer layer) {
				_view.removeMapLayer(layer);
			}
			@Override
			public void postBottom(String str) {
				_view.postBottom(str);	
			}
			@Override
			public void postTop(String str) {
				_view.postTop(str);	
			}
			@Override
			public void removeRightClickListener() {
				_view.removeRightClickListener();	
			}
			@Override
			public void updateCountDown(int i) {
				_view.updateCountDown(i);			
			}
		});
	}
	
	public void startMap() {
		_view.start();
		_model.start();
	}
	
	public void start() {
		//appStart.start();
	}
	
	/**
	 * Run the given Runnable job on the main thread.
	 * @param r   The Runnable job to run
	 */
	public void runJob(Runnable r) {
		try {
			bq.put(r);   // Put job into the queue, blocking if out of space
		} catch (InterruptedException e) {
			System.out.println("runJob(): Exception putting job into blocking queue = "+e);
			e.printStackTrace();
		} 
	}
	
	private BlockingQueue<Runnable> bq = new LinkedBlockingQueue<Runnable>(5);   // May want larger or different type of blocking queue
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final MapController[] c = new MapController[1];   // One-element array trick to get around the "final"
		try {
			SwingUtilities.invokeAndWait(new Runnable() {  // Must use invokeAndWait, not invokeLater so that controller will be a valid instance when the job processing loop starts below.
				public void run() {
					c[0] = new MapController();  // Controller, incl. GUI, is constructed on GUI thread
					c[0].start();  // Always show the GUI on the GUI thread.
				}
			});
		} catch (InvocationTargetException | InterruptedException e1) {
			System.err.println("main(): Exception in instantiating controller = "+e1);
			e1.printStackTrace();
		}
		
		// Go into infinite loop, waiting for Runnable jobs to perform on the main thread.
		while(true) {
			try {
				System.out.println("Waiting for main thread jobs..");
				Runnable r = c[0].bq.take();  // Pull the next available job out of the queue, otherwise block
				System.out.println("Found and now running job: " + r); 
				r.run();  // Run the job.
			} catch (InterruptedException e) {
				System.err.println("Exception in blocking queue: "+ e);
				e.printStackTrace();
			}
		}
	}

	public ICmd2GameModelAdapter getCmd2GameModelAdpt() {
		return _model.getCmd2GameModelAdapter();
	}

}