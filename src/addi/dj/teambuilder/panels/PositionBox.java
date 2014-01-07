package addi.dj.teambuilder.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import addi.dj.teambuilder.Champion;
import addi.dj.teambuilder.Initialiser;
import addi.dj.teambuilder.events.EventHandler;
import addi.dj.teambuilder.panels.ChampionSelect.HoverListener;
import addi.dj.teambuilder.panels.ChampionSelect.SelectListener;

public class PositionBox extends JPanel {
	
	private boolean isSelected = false;
	private boolean isLocked = false;
	
	private Champion champion = null;
	private Champion oldChampion = null;
	
	private final static String imagePath = "LoLTeamBuilder" + File.separator + "images" + File.separator;
	
	private static BufferedImage OPEN = null;
	private static BufferedImage OPEN_BG = null;
	private static BufferedImage LOCKED = null;
	private static BufferedImage PICKING = null;
	private static BufferedImage PICKING_BG = null;
	private static BufferedImage EPICKING = null;
	private static BufferedImage EPICKING_BG = null;
	static Icon HOVER = null;
	
	private final boolean isEnemy;
	
	private JLabel championIcon;
	private JLabel championName;
	
	public PositionBox (boolean isEnemy, SelectListener selectListener, HoverListener hoverListener) {
		super (null);
		
		if (HOVER == null) {
			try {
				OPEN = ImageIO.read (new File (imagePath + "select_open.png"));
				OPEN_BG = ImageIO.read (new File (imagePath + "select_open.png"));
				LOCKED = ImageIO.read (new File (imagePath + "select_locked.png"));
				PICKING = ImageIO.read (new File (imagePath + "select_picking.png"));
				PICKING_BG = ImageIO.read (new File (imagePath + "select_picking_bg.png"));
				EPICKING = ImageIO.read (new File (imagePath + "select_picking_enemy.png"));
				EPICKING_BG = ImageIO.read (new File (imagePath + "select_picking_enemy_bg.png"));
				HOVER = new ImageIcon (imagePath + "select_hover.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.isEnemy = isEnemy;
		addMouseListener (selectListener);
		
		championIcon = new JLabel ();
		championIcon.setBounds (60, 6, 62, 62);
		championIcon.addMouseListener (hoverListener);
		add (championIcon);
		
		championName = new JLabel();
		championName.setBounds (0, 67, 210, 20);
		championName.setVerticalAlignment (SwingConstants.TOP);
		championName.setHorizontalAlignment (SwingConstants.CENTER);
		championName.setFont (new Font (Font.SANS_SERIF, Font.PLAIN, 11));
		championName.setForeground (Color.WHITE);
		add (championName);
		
		setPreferredSize (new Dimension (210, 88));
		setMaximumSize (new Dimension (420, 88));
	}
	
	public boolean isSelected () {
		return isSelected;
	}
	
	public void setSelected (boolean selected) {
		isSelected = selected;
		if (!(isSelected || champion == null)) {
			reset (false);
			if (oldChampion != null)
				updateChampion (oldChampion);
		}
		repaint();
	}
	
	public void setHover (boolean visible) {
		if (champion != null) {
			if (visible)
				championIcon.setIcon (HOVER);
			else championIcon.setIcon (null);
		}
	}
	
	public Champion getChampion () {
		return champion;
	}
	
	public void updateChampion (Champion c) {
		if (champion != c) {
			if (isLocked)
				oldChampion = champion;
			champion = c;
			championName.setText (champion.getName());
			repaint();
		}
	}
	
	public void lockChampion () {
		isLocked = true;
		
		if (oldChampion != null) {
			Initialiser.getChampionList().add (oldChampion);
			oldChampion = null;
		}
		Initialiser.getChampionList().remove (champion);
		
		setSelected (false);
		EventHandler.fireSlotLockEvent();
	}
	
	public void reset (boolean force) {
		if (force || !isLocked) {
			champion = null;
			championName.setText ("");
			setSelected (false);
		}
	}
	
	@Override
	public void paintComponent (Graphics g) {
		if (champion == null) {
			if (isSelected) {
				if (isEnemy) {
					g.drawImage (EPICKING_BG, 0, 0, null);
					g.drawImage (EPICKING, 0, 0, null);
				} else {
					g.drawImage (PICKING_BG, 0, 0, null);
					g.drawImage (PICKING, 0, 0, null);
				}
			} else {
				g.drawImage (OPEN_BG, 0, 0, null);
				g.drawImage (OPEN, 0, 0, null);
			}
		} else if (isSelected) {
			champion.getIcon().paintIcon (this, g, 61, 7);
			g.drawImage ((isEnemy) ? EPICKING : PICKING, 0, 0, null);
		} else {
			champion.getIcon().paintIcon (this, g, 61, 7);
			g.drawImage (LOCKED, 0, 0, null);
		}
	}
}
