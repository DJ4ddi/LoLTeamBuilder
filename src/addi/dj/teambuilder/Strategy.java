package addi.dj.teambuilder;

import java.io.File;

import javax.swing.ImageIcon;

public enum Strategy {
	Split_Push,
	Jungle_Control,
	Flexibility,
	Teamfight,
	Hyper_Carry;
	
	private ImageIcon icon;
	private ImageIcon iconSmall;
	
	private Strategy () {
		String path = "LoLTeamBuilder" + File.separator + "images" + File.separator + "tactic_icons" + File.separator + name().substring (0, 1).toLowerCase();
		icon = new ImageIcon (path + ".png");
		iconSmall = new ImageIcon (path + "_small.png");
	}
	
	public ImageIcon getIcon (boolean small) {
		return (small) ? iconSmall : icon;
	}
}
