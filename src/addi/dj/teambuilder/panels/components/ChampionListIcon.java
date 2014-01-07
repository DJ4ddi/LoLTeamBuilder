package addi.dj.teambuilder.panels.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import addi.dj.teambuilder.Champion;

public class ChampionListIcon extends JComponent {
	
	private static BufferedImage BORDER = null;
	private static BufferedImage BORDER_HIGHLIGHTED = null;
	
	private Champion champion;
	
	private boolean isHovered = false;
	
	public ChampionListIcon (Champion c) {
		try {
			BORDER = ImageIO.read (new File ("LoLTeamBuilder" + File.separator + "images" + File.separator + "border.png"));
			BORDER_HIGHLIGHTED = ImageIO.read (new File ("LoLTeamBuilder" + File.separator + "images" + File.separator + "border_highlighted.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		champion = c;
		
		setPreferredSize (new Dimension (64, 64));
	}
	
	public Champion getChampion() {
		return champion;
	}
	
	public void setHovered (boolean hovered) {
		isHovered = hovered;
	}
	
	@Override
	public void paintComponent (Graphics g) {
		champion.getIcon().paintIcon (this, g, 1, 1);
		if ((isHovered) && BORDER_HIGHLIGHTED != null)
			g.drawImage (BORDER_HIGHLIGHTED, 0, 0, null);
		else if (BORDER != null)
			g.drawImage (BORDER, 0, 0, null);
	}
}
