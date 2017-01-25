package jw84_ym12_chatApp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.miginfocom.swing.MigLayout;

public class TextPanel extends JPanel {
	private static final long serialVersionUID = -1644572216874815607L;

	public TextPanel() {
		initialize();
	}
	/**
	 * initialize the border and layout of textpanel
	 */
	public void initialize() {
		setBorder(new EmptyBorder(0, 10, 0, 20));
		setLayout(new MigLayout("wrap 3", "[] 16 [grow] []"));
	}
	/**
	 * append a string to the panel
	 * @param str
	 */
	public void append(String str) {
		JTextArea label = getTextArea(str);
		JTextPane textPane = getTextPane(str);
		JPanel jPanel = new JPanel(new BorderLayout(10, 10));
		jPanel.add(str.length() <= 20 ? textPane : label);
		JLabel jLabel = getImgLabel("");
		addLeft(this, jPanel, jLabel);
		this.revalidate();
	}
	/**
	 * append an image to the panel
	 * @param icon
	 */
	public void append(ImageIcon icon) {
		JLabel label = new JLabel(icon);
		JPanel jPanel = new JPanel(new BorderLayout(10, 10));
		jPanel.add(label);
		JLabel jLabel = getImgLabel("");
		addRight(this, jPanel, jLabel);
	}
	
	private JLabel getImgLabel(String img) {
		if (img.isEmpty()) {
			return new JLabel("");
		}
		return new JLabel(new ImageIcon(
				new ImageIcon(img).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
	}
	
	private void addRight(JPanel panel, JPanel jPanel, JLabel jLabel) {
		panel.add(jPanel, "span, split, right, wmax 90%");
		panel.add(jLabel, "right, wrap");
	}
	
	private void addLeft(JPanel panel, JPanel jPanel, JLabel jLabel) {
		panel.add(jLabel, "split");
		panel.add(jPanel, "span, right, wrap");
	}

	private JTextArea getTextArea(String info) {
		JTextArea label = new JTextArea(info);
		label.setColumns(info.length() % 45);
		label.setBorder(new EmptyBorder(10, 10, 10, 10));
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
		label.setEditable(false);
		//label.setBackground(Color.green); // this is the same as a
											// JLabel
		label.setBorder(null); // remove the border
		return label;
	}
	
	private JTextPane getTextPane(String info) {
		JTextPane textPane = new JTextPane();
		textPane.setOpaque(false);
		textPane.setEditable(false);
		StyledDocument doc = textPane.getStyledDocument();

		SimpleAttributeSet right = new SimpleAttributeSet();
		StyleConstants.setBackground(right, Color.white);
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setForeground(right, Color.black);
		
		try {
				doc.insertString(doc.getLength(), info, right);
			doc.setParagraphAttributes(doc.getLength(), 1, right, false);
		} catch (Exception s) {
		}
		return textPane;
	}

}

