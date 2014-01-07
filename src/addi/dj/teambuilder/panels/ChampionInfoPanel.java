package addi.dj.teambuilder.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import addi.dj.teambuilder.Champion;
import addi.dj.teambuilder.Strategy;
import addi.dj.teambuilder.events.EventHandler;
import addi.dj.teambuilder.events.InfoUpdateListener;
import addi.dj.teambuilder.events.ResetListener;

public class ChampionInfoPanel extends JPanel {
	
	private final static ImageIcon defaultIcon = new ImageIcon ("LoLTeamBuilder" + File.separator + "images" + File.separator + "default.png");
	
	private Champion champion = null;
	
	private JLabel icon = new JLabel (defaultIcon);
	private JLabel name = new JLabel();
	private JLabel style = new JLabel();
	
	private Box tacticBox = new Box (BoxLayout.LINE_AXIS);
	
	private JLabel roles = new JLabel();
	private JLabel positions = new JLabel();
	private JLabel counters = new JLabel();
	
	public ChampionInfoPanel () {
		super (new GridBagLayout());
		
		
		icon.setPreferredSize (new Dimension (60, 60));
		
		name.setForeground (Color.WHITE);
		name.setFont (new Font (Font.SANS_SERIF, Font.BOLD, 18));
		
		style.setForeground (Color.LIGHT_GRAY);
		style.setFont (new Font (Font.SANS_SERIF, Font.PLAIN, 14));
		
		
		Box nameBox = new Box (BoxLayout.LINE_AXIS);
		nameBox.add (name);
		nameBox.add (Box.createRigidArea (new Dimension (5, 0)));
		nameBox.add (style);
		
		tacticBox.setPreferredSize (new Dimension (128, 32));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridheight = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add (icon, gbc);
		
		gbc.gridheight = 1;
		gbc.gridy = 2;
		JLabel roleLabel = new JLabel ("Roles");
		roleLabel.setForeground (Color.WHITE);
		add (roleLabel, gbc);
		
		gbc.gridy = 3;
		JLabel positionLabel = new JLabel ("Positions");
		positionLabel.setForeground (Color.WHITE);
		add (positionLabel, gbc);
		
		gbc.gridy = 4;
		JLabel counterLabel = new JLabel ("Counters");
		counterLabel.setForeground (Color.WHITE);
		add (counterLabel, gbc);
		
		gbc.insets = new Insets (0, 10, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add (nameBox, gbc);
		
		gbc.gridy = 1;
		add (tacticBox, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridy = 2;
		roles.setForeground (Color.LIGHT_GRAY);
		add (roles, gbc);
		
		gbc.gridy = 3;
		positions.setForeground (Color.LIGHT_GRAY);
		add (positions, gbc);
		
		gbc.gridy = 4;
		counters.setForeground (Color.LIGHT_GRAY);
		add (counters, gbc);
		
		EventHandler.addInfoUpdateListener (new InfoUpdateListener() {
			@Override
			public void onInfoUpdate (Champion c) {
				updateChampion (c);
			}
		});
		
		EventHandler.addResetListener (new ResetListener() {
			@Override
			public void onReset () {
				champion = null;
				icon.setIcon (defaultIcon);
				name.setText ("");
				style.setText ("");
				tacticBox.removeAll();
				roles.setText ("");
				positions.setText ("");
				counters.setText ("");
			}
		});
	}
	
	public void updateChampion (Champion c) {
		if (champion != c) {
			champion = c;
			icon.setIcon (champion.getIcon());
			name.setText (champion.getName());
			style.setText (firstLetterUppercase (champion.getPlayStyle().toString()));
			tacticBox.removeAll();
			tacticBox.add (new JLabel (c.getPrimaryStrategy().getIcon (false)));
			for (Strategy s : c.getSecondaryStrategies()) {
				tacticBox.add (new JLabel (s.getIcon (true)));
			}
			roles.setText (parseList (c.getRoles()));
			positions.setText (parseList (c.getPositions()));
			counters.setText (parseList (c.getCounters()));
		}
	}
	
	private String parseList (@SuppressWarnings("rawtypes") List l) {
		if (l.isEmpty()) {
			return "";
		} else {
			StringBuilder s = new StringBuilder();
			for (Object o : l)
				s.append (", ").append (firstLetterUppercase (o.toString()));
			return s.substring (2);
		}
	}
	
	private String firstLetterUppercase (String s) {
		return s.substring (0, 1) + s.substring (1).toLowerCase();
	}
}
