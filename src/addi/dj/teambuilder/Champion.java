package addi.dj.teambuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import addi.dj.teambuilder.panels.components.ChampionListIcon;

public class Champion implements Comparable<Champion> {
	
	private String name;

	private Style playStyle;
	
	private Role primaryRole;
	private List<Role> secondaryRoles;
	
	private List<Position> positions;
	
	private List<String> counterTo;
	
	private ImageIcon icon;
	private final ChampionListIcon listIcon;
	
	private float roleMultiplier;
	private float strategyScore;
	
	public Champion (String name, Style play, Role primary, List<Role> secondary, List<Position> positions, List<String> counters) {
		File iconFile = new File ("LoLTeamBuilder" + File.separator + "icons" + File.separator + name.replace (" ", "") + "_Square_0.png");
		if (iconFile.exists())
			icon = new ImageIcon (iconFile.getPath());
		else icon = new ImageIcon ("LoLTeamBuilder" + File.separator + "images" + File.separator + "default.png");

		this.name = name;
		playStyle = play;
		primaryRole = primary;
		secondaryRoles = secondary;
		this.positions = positions;
		counterTo = counters;
		
		int roleCounter = 1 + secondaryRoles.size();
		
		roleMultiplier = (float) (-10.0 / Math.pow (0.5 * (double) roleCounter + 0.5, 2.0));
		
		int tmp = primaryRole.getAssociatedStrategy().ordinal() << 1;
		
		for (Role r : secondaryRoles)
			tmp += r.getAssociatedStrategy().ordinal();
		
		strategyScore = (float) tmp / (roleCounter + 1);
		
		listIcon = new ChampionListIcon (this);
	}
	
	public String getName () {
		return name;
	}
	
	public int calculatePriority () {
		float priority = 0;
		Champion[] champions = Initialiser.getMainFrame().getChampionSelect().getChampions();
		for (int i = 0; i < champions.length; i++) {
			Champion c = champions [i];
			if (c != null && c != this) {
				if (i < 5) {
					Style s = c.getPlayStyle();
					if (playStyle == s)
						priority++;
					else if (playStyle == Style.BALANCED || s == Style.BALANCED)
						priority += 0.5;
				} else {
					if (counters (c.getName()))
						priority++;
					if (c.counters (name))
						priority--;
				}
			}
		}
		
		float[] teamScores = Initialiser.getMainFrame().getChampionSelect().getScores();
		float currentMaximum = 0f;
		for (int i = 0; i < teamScores.length >> 1; i++) {
			if (teamScores [i] != 0) {
				float diff = currentMaximum - teamScores [i];
				if (diff < 10) {
					float value = getScoreForStrategy (Strategy.values() [i]);
					diff -= value;
					if (diff < 0)
						currentMaximum = teamScores [i] + value;
				}
			}
		}
		
		return (int) (priority + currentMaximum);
	}
	
	public ImageIcon getIcon () {
		return icon;
	}
	
	public ChampionListIcon getListIcon() {
		return listIcon;
	}
	
	public Style getPlayStyle () {
		return playStyle;
	}
	
	public float getScoreForStrategy (Strategy s) {
		float diff = Math.abs ((float) s.ordinal() - strategyScore);
		float finalScore = roleMultiplier * diff * diff + 10;
		
		return (finalScore >= 0) ? finalScore : 0f;
	}
	
	public Strategy getPrimaryStrategy () {
		return Strategy.values() [Math.round (strategyScore)];
	}
	
	public List<Strategy> getSecondaryStrategies () {
		List<Strategy> rv = new ArrayList<Strategy>();
		
		Strategy primaryStrategy = getPrimaryStrategy();
		for (Strategy s : Strategy.values())
			if (s != primaryStrategy && getScoreForStrategy (s) > 5)
				rv.add (s);
		
		return rv;
	}
	
	public List<Role> getRoles () {
		List<Role> rv = new ArrayList<Role>();
		rv.add (primaryRole);
		rv.addAll (secondaryRoles);
		return rv;
	}
	
	public boolean hasPosition (Position r) {
		return positions.contains (r);
	}
	
	public List<Position> getPositions () {
		return positions;
	}
	
	public boolean counters (String c) {
		return counterTo.contains (c);
	}
	
	public List<String> getCounters () {
		return counterTo;
	}
	
	// Comparable methods

	@Override
	public int compareTo (Champion c) {
		return name.compareTo (c.getName());
	}
	
	@Override
	public String toString () {
		return name;
	}
}
