package addi.dj.teambuilder.panels.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LabelPanel extends JPanel {
	
	private final static ImageIcon background = new ImageIcon ("LoLTeamBuilder" + File.separator + "images" + File.separator + "label_background.png");
	
	public LabelPanel (String text) {
		Dimension size = new Dimension (200, 37);
		setPreferredSize (size);
		setMaximumSize (size);
		setBorder (new EmptyBorder (5, 0, 5, 0));
		
		JLabel textLabel = new JLabel (text);
		textLabel.setForeground (Color.WHITE);
		add (textLabel);
	}
	
	@Override
	public void paintComponent (Graphics g) {
		background.paintIcon (this, g, 0, 5);
	}
}
