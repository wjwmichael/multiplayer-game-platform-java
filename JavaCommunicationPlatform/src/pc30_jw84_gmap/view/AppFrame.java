package pc30_jw84_gmap.view;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.layers.Layer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import map.IRightClickAction;
import map.MapLayer;
import map.MapPanel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;


public class AppFrame<CBType> extends JFrame {
	private static final long  serialVersionUID = -1046744381305932964L;
	private JPanel             _ctrlPanel;
	private MapPanel           _mapPanel;
	private JComboBox<CBType>          _places;
	private JButton            _gotoBtn;
	private IView2ModelAdapter<CBType> _adpt;
	private JPanel panel;
	private JTextPane gameTextMsgPane;
	private IRightClickAction _rightClick;
	private JTextArea bottomTextArea;
	private JTextArea countDownTextArea;
	//private IRightClickAction  _rightClick;


	/**
	 * Auto-generated main method to display this JFrame
	 */

	public AppFrame(IView2ModelAdapter<CBType> adpt, IRightClickAction rightClick) {
		super();
		_adpt = adpt;
		_rightClick = rightClick;
		//_rightClick = rightClick;
		initGUI(rightClick);
	}

	private void initGUI(IRightClickAction rightClick) {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
			panel = new JPanel();
			getContentPane().add(panel, BorderLayout.NORTH);
			
			gameTextMsgPane = new JTextPane();
			panel.add(gameTextMsgPane);

			_ctrlPanel = new JPanel();
			FlowLayout jPanel1Layout = new FlowLayout();
			_ctrlPanel.setLayout(jPanel1Layout);
			getContentPane().add(_ctrlPanel, BorderLayout.SOUTH);
			_ctrlPanel.setPreferredSize(new java.awt.Dimension(390, 43));
			_ctrlPanel.setBackground(new java.awt.Color(128,255,128));
			
			countDownTextArea = new JTextArea();
			_ctrlPanel.add(countDownTextArea);

			_places = new JComboBox<CBType>();
			_ctrlPanel.add(_places);
			_places.setVisible(false);
			_gotoBtn = new JButton();
			_ctrlPanel.add(_gotoBtn);
			_gotoBtn.setText("Go!");
			
			bottomTextArea = new JTextArea();
			_ctrlPanel.add(bottomTextArea);
			_gotoBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					_adpt.goPlace(_places.getItemAt(_places.getSelectedIndex()));
					_mapPanel.repaint();
				}
			});

			_mapPanel = new MapPanel(Earth.class);
			getContentPane().add(_mapPanel, BorderLayout.CENTER);
			_mapPanel.setPreferredSize(new java.awt.Dimension(600, 400));
			_mapPanel.addRightClickAction(rightClick);

			pack();
		} catch (Exception e) {
			//add your error handling code here
			e.printStackTrace();
		}
	}

	public void start() {		
		_mapPanel.start();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setPosition(Position pos)
	{
		_mapPanel.setPosition(pos, true);
	}

	public void addMapLayer(Layer layer)
	{
		_mapPanel.addLayer(layer);
	}

	public void removeMapLayer(Layer layer)
	{
		_mapPanel.removeLayer(layer);
	}

	public void addPlace(CBType p) {
		_places.insertItemAt(p, 0);
		_places.setSelectedIndex(0);
	}

	public void postBottom(String str) {
		bottomTextArea.setText(null);
		bottomTextArea.setText(str);
	}

	public void removeRightClickListener() {
		_mapPanel.removeRightClickAction(_rightClick);
		
	}

	public void postTop(String str) {
		gameTextMsgPane.setText(null);
		gameTextMsgPane.setText(str);
	}

	public void updateCountDown(int i) {
		countDownTextArea.setText("The time left: " + String.valueOf(i) + " s");
	}
}
