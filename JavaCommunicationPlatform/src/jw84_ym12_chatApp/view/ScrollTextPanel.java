package jw84_ym12_chatApp.view;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class ScrollTextPanel extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4340295317427502313L;
	public TextPanel textPanel = new TextPanel();
	public ScrollTextPanel() {
		TitledBorder title = BorderFactory.createTitledBorder("Message List");
		setBorder(title);
//		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		TextPanel textPanel = new TextPanel();
		this.textPanel = textPanel;
		setViewportView(textPanel);
	}
}
