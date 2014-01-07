package addi.dj.teambuilder.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import addi.dj.teambuilder.Strategy;
import addi.dj.teambuilder.panels.components.LockButton;
import addi.dj.teambuilder.panels.components.TacticButton;

public class ButtonPanel extends JPanel {
	
	public static LockButton lockButton;
	
	private static BufferedImage background;
	
	private static TacticButton[] tacticButtons = new TacticButton [Strategy.values().length];
	
	public ButtonPanel () {
		super (null);
		
		try {
			background = ImageIO.read (new File ("LoLTeamBuilder" + File.separator + "images" + File.separator + "button_background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Dimension size = new Dimension (1000, 94);
		setPreferredSize (size);
		setMaximumSize (size);
		
		lockButton = new LockButton();
		lockButton.setBounds (777, 24, 177, 48);
		add (lockButton);
		
		for (Strategy s : Strategy.values()) {
			TacticButton tb = new TacticButton (s.toString(), s.getIcon (false));
			tacticButtons [s.ordinal()] = tb;
			tb.setBounds (46 + 127 * s.ordinal(), 27, 120, 41);
			add (tb);
		}
		
		for (TacticButton tb : tacticButtons) {
			add (tb);
		}
		
		setBorder (new EmptyBorder (5, 5, 5, 5));
	}
	
	public boolean isTacticEnabled (int i) {
		return tacticButtons [i].isActive();
	}
	
	@Override
	public void paintComponent (Graphics g) {
		g.drawImage (background, 0, 0, null);
	}
}