package addi.dj.teambuilder.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import addi.dj.teambuilder.Champion;
import addi.dj.teambuilder.Initialiser;
import addi.dj.teambuilder.Strategy;
import addi.dj.teambuilder.events.EventHandler;
import addi.dj.teambuilder.events.ResetListener;
import addi.dj.teambuilder.events.SlotLockListener;

public class ChampionSelect {

	private PositionBox[] positions = new PositionBox[10];
	
	private float[] scores = new float[Strategy.values().length << 1];

	public ChampionSelect (Container mainPanel) {
		SelectListener selectListener = new SelectListener();
		HoverListener hoverListener = new HoverListener();
		
		Dimension spacer = new Dimension (0, 12);

		Box allyPositions = new Box (BoxLayout.PAGE_AXIS);
		allyPositions.setBorder (new EmptyBorder (0, 6, 6, 6));
		
		allyPositions.add (Box.createRigidArea (spacer));
		JLabel allyLabel = new JLabel (new ImageIcon ("LoLTeamBuilder" + File.separator + "images" + File.separator + "label_allies.png"));
		allyLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
		allyPositions.add (allyLabel);
		allyPositions.add (Box.createRigidArea (spacer));
		
		Box enemyPositions = new Box (BoxLayout.PAGE_AXIS);
		enemyPositions.setBorder (new EmptyBorder (0, 6, 6, 6));
		
		enemyPositions.add (Box.createRigidArea (spacer));
		JLabel enemyLabel = new JLabel (new ImageIcon ("LoLTeamBuilder" + File.separator + "images" + File.separator + "label_enemies.png"));
		enemyLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
		enemyPositions.add (enemyLabel);
		enemyPositions.add (Box.createRigidArea (spacer));
		
		for (int i = 0; i < 10; i++) {
			boolean isEnemy = i < 5 ? false : true;
			positions[i] = new PositionBox (isEnemy, selectListener, hoverListener);
			if (isEnemy) {
				enemyPositions.add (Box.createRigidArea (spacer));
				enemyPositions.add (positions[i]);
			}
			else {
				allyPositions.add (Box.createRigidArea (spacer));
				allyPositions.add (positions[i]);
			}
		}
		
		allyPositions.add (Box.createVerticalGlue());
		enemyPositions.add (Box.createVerticalGlue());
		
		mainPanel.add (allyPositions, BorderLayout.LINE_START);
		mainPanel.add (enemyPositions, BorderLayout.LINE_END);
		
		EventHandler.addSlotLockListener (new SlotLockListener() {
			@Override
			public void onSlotLock() {
				calculateScores();
			}
		});
		
		EventHandler.addResetListener (new ResetListener() {
			@Override
			public void onReset() {
				for (PositionBox p : positions) p.reset (true);
				resetScores();
			}
		});
	}

	public void setSelectedPosition (PositionBox position) {
		PositionBox selectedPosition = getSelectedPosition();
		if (!position.equals (selectedPosition)) {
			if (selectedPosition != null) selectedPosition.setSelected (false);
			position.setSelected (true);
		}

		Champion c = Initialiser.getMainFrame().getMultiList().getSelectedValue();
		if (c != null) {
			position.updateChampion (c);
			ButtonPanel.lockButton.setEnabled (true);
		}
	}

	public PositionBox getSelectedPosition () {
		for (PositionBox tmp : positions) if (tmp.isSelected()) return tmp;
		return null;
	}
	
	private void resetScores () {
		for (int i = 0; i < scores.length; i++)
			scores [i] = 0;
	}
	
	public Champion[] getChampions () {
		Champion[] rv = new Champion [10];
		for (int i = 0; i < 10; i++)
			rv [i] = positions [i].getChampion();
		return rv;
	}
	
	private void calculateScores () {
		resetScores();
		
		Champion[] champions = getChampions();
		Strategy[] strategies = Strategy.values();
		for (int i = 0; i < strategies.length; i++) {
			for (int j = 0; j < 10; j++) {
				if (champions [j] != null && (Initialiser.getMainFrame().getButtonPanel().isTacticEnabled (i) || j > 4)) {
					float score = champions [j].getScoreForStrategy (strategies [i]);
					if (score > 0)
						scores [(j < 5) ? i : i + 5] += score;
				}
			}
		}
	}
	
	public float[] getScores () {
		return scores;
	}
	
	protected class HoverListener extends MouseAdapter {		
		@Override
		public void mouseEntered (MouseEvent e) {
			((PositionBox) ((JLabel) e.getSource()).getParent()).setHover (true);
		}
		
		@Override
		public void mouseExited (MouseEvent e) {
			((PositionBox) ((JLabel) e.getSource()).getParent()).setHover (false);
		}
		
		@Override
		public void mouseClicked (MouseEvent e) {
			PositionBox source = (PositionBox) ((JLabel) e.getSource()).getParent();
			Champion c = source.getChampion();
			if (c == null) {
				if (!source.isSelected())
					setSelectedPosition (source);
			} else {
				EventHandler.fireInfoUpdateEvent (c);
			}
		}
	}
	
	protected class SelectListener extends MouseAdapter {
		@Override
		public void mouseClicked (MouseEvent e) {
			setSelectedPosition ((PositionBox) e.getSource());
		}
	}
}
