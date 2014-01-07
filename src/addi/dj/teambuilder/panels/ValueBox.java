package addi.dj.teambuilder.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class ValueBox extends Box {
	public ValueBox (String text) {
		super (BoxLayout.PAGE_AXIS);
		
		JLabel mainLabel = new JLabel (text);
		mainLabel.setForeground (Color.WHITE);
		add (mainLabel);
		
		setPreferredSize (new Dimension (80, 126));
	}
	
	public void addLabel (String text) {
		JLabel  newLabel = new JLabel (text);
		newLabel.setForeground (Color.LIGHT_GRAY);
		add (newLabel);
	}
	
	public void reset () {
		for (int i = 1; i < getComponentCount();)
			remove (i);
	}
}
