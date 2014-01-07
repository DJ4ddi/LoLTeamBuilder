package addi.dj.teambuilder.panels.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import addi.dj.teambuilder.events.EventHandler;

public class TacticButton extends JPanel {
	
	private static ButtonListener mouseListener = new ButtonListener();

	private final static String imagePath = "LoLTeamBuilder" + File.separator + "images" + File.separator;

	private final static ImageIcon INACTIVE = new ImageIcon (imagePath + "tactic_inactive.png");
	private final static ImageIcon IHOVER = new ImageIcon (imagePath + "tactic_inactive_hover.png");
	private final static ImageIcon ACTIVE = new ImageIcon (imagePath + "tactic_secondary.png");
	private final static ImageIcon AHOVER = new ImageIcon (imagePath + "tactic_secondary_hover.png");
	
	private boolean isActive = true;
	private boolean isHovered = false;
	
	private JLabel strategyLabel;
	
	public TacticButton (String sText, ImageIcon sIcon) {
		super (null);
		
		addMouseListener (mouseListener);
		
		strategyLabel = new JLabel (sText.replace ('_', ' '), sIcon, JLabel.CENTER);
		strategyLabel.setForeground (Color.WHITE);
		strategyLabel.setBounds (0, 0, 120, 41);
		strategyLabel.setHorizontalAlignment (JLabel.CENTER);
		strategyLabel.setVerticalAlignment (JLabel.CENTER);
		add (strategyLabel);
		
		Dimension size = new Dimension (145, 49);
		setPreferredSize (size);
		setMaximumSize (size);
	}
	
	public boolean isActive () {
		return isActive;
	}
	
	public void toggleActive () {
		isActive = !isActive;
		repaint();
		EventHandler.fireSlotLockEvent();
	}
	
	public void setHovered (boolean h) {
		isHovered = h;
		repaint();
	}
	
	@Override
	public void paintComponent (Graphics g) {
		if (isActive)
			if (isHovered)
				AHOVER.paintIcon (this, g, 0, 0);
			else ACTIVE.paintIcon (this, g, 0, 0);
		else if (isHovered)
			IHOVER.paintIcon (this, g, 0, 0);
		else INACTIVE.paintIcon (this, g, 0, 0);
	}
	
	private static class ButtonListener extends MouseAdapter {
		@Override
		public void mouseClicked (MouseEvent e) {
			TacticButton source = (TacticButton) e.getSource();
			source.toggleActive();
		}
		
		@Override
		public void mouseEntered (MouseEvent e) {
			((TacticButton) e.getSource()).setHovered (true);
		}
		
		@Override
		public void mouseExited (MouseEvent e) {
			((TacticButton) e.getSource()).setHovered (false);
		}
	}
}
