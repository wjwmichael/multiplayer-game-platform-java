package pc30_jw84_gmap.model;

import map.MapLayer;
import pc30_jw84_gmap.utils.ICmd2GameModelAdapter;
import pc30_jw84_gmap.utils.IGame2CmdAdpt;
import pc30_jw84_gmap.utils.SetFlag;
import provided.mixedData.MixedDataKey;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.GlobeAnnotation;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Pyramid;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfaceCircle;
import gov.nasa.worldwind.render.SurfaceSquare;
import gov.nasa.worldwind.util.WWUtil;
import jw84_pc30_chatApp.util.TextMsg;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.geom.LatLon;
import common.AOurDataPacketAlgoCmd;
import common.IChatServer;
import common.ICmd2ModelAdapter;
import common.IComponentFactory;
import common.msg.IChatMsg;
//import common.MarkRevealedMine;
import common.msg.chat.ITextMsg;

public class MapModel {
	IModel2ViewAdapter _adpt;
	
	MapLayer           _layer = new MapLayer();
	RenderableLayer _circleLayer = new RenderableLayer();
	RenderableLayer _markedLayer = new RenderableLayer();
	
	HashMap<LatLon, Boolean> mineLocations = new HashMap<>();
	
	Timer updateGUITimer;
	int secondsProvided = 180;
	
	
	IGame2CmdAdpt game2CmdAdpt = null;
	ICmd2GameModelAdapter cmd2GameModelAdpt = new ICmd2GameModelAdapter() {
		
		@Override
		public void setFlagAndDisableMine(double lat, double lon) {
			MapModel.this.setFlagAndDisableMine(lat, lon);
		}
		
		@Override
		public Integer getRevealedMinesCount() {
			int count = 0;
			for (Map.Entry<LatLon, Boolean> entry: mineLocations.entrySet()) {
				if (entry.getValue() == true) {
					count++;
				}
			}
			return new Integer(count);
		}
	};
	
	transient ICmd2ModelAdapter _cmd2ModelAdpt = null;
	
	int chanceCount = 5;
	
	public MapModel(ICmd2ModelAdapter cmd2modeladpt, IGame2CmdAdpt game2CmdAdpt, IModel2ViewAdapter adpt) {
		setCmd2ModelAdapter(cmd2modeladpt);
		this.game2CmdAdpt = game2CmdAdpt;
		_adpt = adpt;
		LatLon willyLatLon = new LatLon(LatLon.fromDegrees(29.718550, -95.399068));
		LatLon stadiumLatLon = new LatLon(LatLon.fromDegrees(29.716081, -95.409248));
		mineLocations.put(willyLatLon, false);
		mineLocations.put(stadiumLatLon, false);
		
		
		ActionListener updateGUIPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_adpt.updateCountDown(--secondsProvided);
				if (secondsProvided == 0) {
					updateGUITimer.stop();
					game2CmdAdpt.sendLeaveMsg();
					_adpt.removeRightClickListener();
				}
			}
		};
		updateGUITimer = new Timer(1000, updateGUIPerformer);
		updateGUITimer.start();
	}
	
	public void setCmd2ModelAdapter(ICmd2ModelAdapter _cmd2ModelAdpt) {
		this._cmd2ModelAdpt = _cmd2ModelAdpt;
	}
	
	public ICmd2ModelAdapter getCmd2ModelAdapter() {
		return _cmd2ModelAdpt;
	}
	
	public void click(Position p) {
		chanceCount--;
		
		showCirclesAroundClickPoint(p);
        
		int hitCount = 0;
		int detectedCount = 0;
		Vec4 pClicked = (new Earth()).computePointFromLocation(p);
		for (Map.Entry<LatLon, Boolean> entry: mineLocations.entrySet()) {
			if (entry.getValue()) {
				continue;
			}
			LatLon latlon = entry.getKey();
			Vec4 pMine = (new Earth()).computePointFromLocation(latlon);
			double dist = pClicked.distanceTo3(pMine);
			if (dist <= 50) {
				hitCount++;
				mineLocations.put(latlon, true);
				_cmd2ModelAdpt.sendMsg2LocalChatroom(SetFlag.class, new SetFlag(latlon.getLatitude().getDegrees(),latlon.getLongitude().getDegrees()));
				// 仅仅测试用，实际需要删除这一句：
				setFlagAndDisableMine(latlon.getLatitude().getDegrees(),latlon.getLongitude().getDegrees());
			} else if (dist <= 150) {
				detectedCount++;
			}
		}
        _adpt.postTop(hitCount + " mine(s) revealed in red circle and + " + detectedCount + " unrevealed mines(s) in yellow ring.");;
		
        if (chanceCount > 0) {
        	_adpt.postBottom(chanceCount + " chance(s) left");
        } else {
        	_adpt.postBottom(chanceCount + " chance(s) left");
        	_adpt.removeRightClickListener();
    		game2CmdAdpt.sendLeaveMsg();
        }
        
	}
	
	public void setFlagAndDisableMine(double lat, double lon) {
		LatLon p = new LatLon(LatLon.fromDegrees(lat, lon));
		_markedLayer.setName("Square Shape");
		ShapeAttributes attrs = new BasicShapeAttributes();
		attrs.setInteriorMaterial(Material.ORANGE);
		attrs.setOutlineMaterial(Material.BLACK);
		attrs.setInteriorOpacity(1);
		attrs.setOutlineOpacity(1);
		attrs.setOutlineWidth(1);
        Pyramid pyramid = new Pyramid(new Position(p.getLatitude(), p.getLongitude(), 15), 100.0, 100.0);
        pyramid.setAttributes(attrs);
        _markedLayer.addRenderable(pyramid);
        
        _adpt.show(_markedLayer);
        
		for (LatLon latlon: mineLocations.keySet()) {
			if (latlon.equals(p)) {
				mineLocations.put(latlon, true);
			}
		}
	}
	
	public int countAlreadyRevealedMines() {
		int count = 0;
		for (Map.Entry<LatLon, Boolean> entry: mineLocations.entrySet()) {
			if (entry.getValue()) {
				count++;
			}
		}
		return count;
	}
	
	public int countUnrevealedMines() {
		int count = 0;
		for (Map.Entry<LatLon, Boolean> entry: mineLocations.entrySet()) {
			if (!entry.getValue()) {
				count++;
			}
		}
		return count;
	}	
	
	
	public void showCirclesAroundClickPoint(Position p) {
		_adpt.hide(_circleLayer);
		_circleLayer.removeAllRenderables();
		_circleLayer.setName("Circle Shape");
		ShapeAttributes attrsBig = new BasicShapeAttributes();
		attrsBig.setInteriorMaterial(Material.YELLOW);
		attrsBig.setOutlineMaterial(new Material(WWUtil.makeColorBrighter(Color.YELLOW)));
		attrsBig.setInteriorOpacity(0.1);
		attrsBig.setOutlineOpacity(0.4);
		attrsBig.setOutlineWidth(3);
        SurfaceCircle shapeBig = new SurfaceCircle(p, 150);
        shapeBig.setAttributes(attrsBig);
        _circleLayer.addRenderable(shapeBig);
        
        ShapeAttributes attrsSmall = new BasicShapeAttributes();
        attrsSmall.setInteriorMaterial(Material.RED);
        attrsSmall.setOutlineMaterial(new Material(WWUtil.makeColorBrighter(Color.RED)));
        attrsSmall.setInteriorOpacity(0.4);
        attrsSmall.setOutlineOpacity(0.5);
        attrsSmall.setOutlineWidth(3);
        SurfaceCircle shapeSmall = new SurfaceCircle(p, 50);
        shapeSmall.setAttributes(attrsSmall);
        _circleLayer.addRenderable(shapeSmall);
        _adpt.show(_circleLayer);		
	}
	
	public void start() {
		Position willy = Position.fromDegrees(29.718550, -95.399068, 150);
		Position epcot = Position.fromDegrees(28.374454, -81.549363, 1000);
		Position nyc   = Position.fromDegrees(40.748974, -73.990288, 10000);
		
//		_adpt.addPlace(new Place("Greenwich", Position.fromDegrees(51.477222, 0.0, 1000)));
//		_adpt.addPlace(new Place("Louvre", Position.fromDegrees(48.860930, 2.336461, 1000)));
//		_adpt.addPlace(new Place("London Eye", Position.fromDegrees(51.503367, -0.119968, 1000)));
//		_adpt.addPlace(new Place("Acropolis", Position.fromDegrees(37.971458, 23.726647, 800)));
//		_adpt.addPlace(new Place("Colosseum", Position.fromDegrees(41.890306, 12.492354, 500)));
//		_adpt.addPlace(new Place("Taj Mahal", Position.fromDegrees(27.174932, 78.042144, 1000)));
//		_adpt.addPlace(new Place("Pyramids", Position.fromDegrees(29.976788, 31.134001, 1500)));
//		_adpt.addPlace(new Place("Statue of Liberty", Position.fromDegrees(40.68925, -74.044493, 500)));
//		_adpt.addPlace(new Place("NYC", nyc));
//		_adpt.addPlace(new Place("Luxor", Position.fromDegrees(36.095568, -115.176033, 1500)));
//		_adpt.addPlace(new Place("Grand Canyon", Position.fromDegrees(36.108091, -113.214912, 90000)));
//		_adpt.addPlace(new Place("Golden Gate", Position.fromDegrees(37.82035, -122.4778804, 5000)));
//		_adpt.addPlace(new Place("Epcot Center", epcot));
//		_adpt.addPlace(new Place("Willy", willy));
//		_adpt.addPlace(new Place("Rice", Position.fromDegrees(29.71724, -95.40150, 1000)));
		_adpt.addPlace(new Place("RiceForMineSweeper", Position.fromDegrees(29.71724, -95.40150, 3000)));
//		
//		_layer.addToggleAnnotation("Willy", "Willy Selected!", willy);
//		_layer.addToggleAnnotation("Epcot Center", "Epcot Selected!", epcot, 5000, 1000000);
//		_layer.addAnnotation(new GlobeAnnotation("NYC", nyc));
		_adpt.show(_layer);
		
		_adpt.postBottom(chanceCount + " chance(s) left");
	}

	public ICmd2GameModelAdapter getCmd2GameModelAdapter() {
		return cmd2GameModelAdpt;
	}
}
