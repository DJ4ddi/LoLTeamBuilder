package addi.dj.teambuilder.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import addi.dj.teambuilder.Initialiser;
import addi.dj.teambuilder.Strategy;
import addi.dj.teambuilder.events.EventHandler;
import addi.dj.teambuilder.events.ResetListener;
import addi.dj.teambuilder.events.SlotLockListener;
import addi.dj.teambuilder.panels.components.FlippedProgressBar;

public class TeamInfoPanel extends JPanel {
	
	private Strategy[] strategies = Strategy.values();
	private JProgressBar[] strategyBars = new JProgressBar [strategies.length];
	
	public TeamInfoPanel (final boolean isEnemy) {
		super (new GridBagLayout());
		setPreferredSize (new Dimension (210, strategies.length * 24));
		setMaximumSize (new Dimension (210, strategies.length * 24));
		setBorder (new EmptyBorder (5, 5, 5, 5));
		
		UIManager.getLookAndFeelDefaults().put("progressBarTrackInterior", Color.BLACK);
		UIManager.getLookAndFeelDefaults().put("progressBarTrackBase", Color.DARK_GRAY);
		
		GridBagConstraints gbc = new GridBagConstraints();
		for (int i = 0; i < strategies.length; i++) {
			gbc.weightx = 0.0;
			gbc.gridy = i;
			gbc.gridx = (isEnemy) ? 1 : 0;
			add (new JLabel (strategies [i].getIcon (true)), gbc);
			
			gbc.weightx = 0.5;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = (isEnemy) ? 0 : 1;
			strategyBars [i] = (isEnemy) ? new FlippedProgressBar (0, 50) : new JProgressBar (0, 50);
			add (strategyBars [i], gbc);
		}
		
		EventHandler.addSlotLockListener (new SlotLockListener() {
			@Override
			public void onSlotLock() {
				float[] scores = Initialiser.getMainFrame().getChampionSelect().getScores();
				for (int i = 0; i < strategyBars.length; i++) {
					if (Initialiser.getMainFrame().getButtonPanel().isTacticEnabled (i) || isEnemy)
						strategyBars [i].setValue ((int) scores [(isEnemy) ? i + strategies.length : i]);
					else strategyBars [i].setValue (0);
				}
			}
		});
		
		EventHandler.addResetListener (new ResetListener() {
			@Override
			public void onReset() {
				for (JProgressBar p : strategyBars)
					p.setValue (0);
			}
		});
	}
}
