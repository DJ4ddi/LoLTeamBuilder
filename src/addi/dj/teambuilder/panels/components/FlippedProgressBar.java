package addi.dj.teambuilder.panels.components;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JProgressBar;

public class FlippedProgressBar extends JProgressBar {
	public FlippedProgressBar(int min, int max) {
		super (min, max);
	}

	@Override
	public void paintComponent (Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.scale(-1, 1);
		g2d.translate(-getWidth(), 0);
		super.paintComponent(g2d);
	}
}
