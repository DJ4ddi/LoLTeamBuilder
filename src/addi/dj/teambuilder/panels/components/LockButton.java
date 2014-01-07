package addi.dj.teambuilder.panels.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import addi.dj.teambuilder.Initialiser;
import addi.dj.teambuilder.events.EventHandler;
import addi.dj.teambuilder.events.ResetListener;
import addi.dj.teambuilder.events.SlotLockListener;
import addi.dj.teambuilder.panels.PositionBox;

public class LockButton extends JButton {

	private final static String imagePath = "LoLTeamBuilder" + File.separator + "images" + File.separator;

	private final static ImageIcon ACTIVE = new ImageIcon (imagePath + "button_lock_active.png");
	private final static ImageIcon INACTIVE = new ImageIcon (imagePath + "button_lock_inactive.png");
	private final static ImageIcon HOVER = new ImageIcon (imagePath + "button_lock_active_hover.png");
	
	private boolean isHovered = false;
	
	public LockButton () {
		setEnabled (false);
		
		Dimension size = new Dimension (177, 48);
		setPreferredSize (size);
		setMaximumSize (size);
		
		addMouseListener (new HoverListener());
		
		EventHandler.addSlotLockListener (new SlotLockListener() {
			@Override
			public void onSlotLock() {
				setEnabled (false);
			}
		});
		
		EventHandler.addResetListener (new ResetListener() {
			@Override
			public void onReset () {
				setEnabled (false);
			}
		});
	}
	
	
	@Override
	public void paintComponent (Graphics g) {
		if (isEnabled()) {
			if (isHovered)
				HOVER.paintIcon (this, g, 0, 0);
			else ACTIVE.paintIcon (this, g, 0, 0);
		} else INACTIVE.paintIcon (this, g, 0, 0);
	}
	
	private class HoverListener extends MouseAdapter {
		@Override
		public void mouseClicked (MouseEvent e) {
			PositionBox selectedPosition = Initialiser.getMainFrame().getChampionSelect().getSelectedPosition();
			if (selectedPosition != null)
				selectedPosition.lockChampion();
		}
		
		@Override
		public void mouseEntered (MouseEvent e) {
			isHovered = true;
		}
		
		@Override
		public void mouseExited (MouseEvent e) {
			isHovered = false;
		}
	}
}
