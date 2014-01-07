package addi.dj.teambuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import addi.dj.teambuilder.events.EventHandler;
import addi.dj.teambuilder.panels.ButtonPanel;
import addi.dj.teambuilder.panels.ChampionInfoPanel;
import addi.dj.teambuilder.panels.ChampionSelect;
import addi.dj.teambuilder.panels.TeamInfoPanel;
import addi.dj.teambuilder.panels.components.MultiList;

public class MainFrame {
	public static void main(String[] args) {
	}

	// Create the window and set its name
	public final JFrame frame = new JFrame ("League of Legends Team Builder");
	
	private ChampionSelect championSelect;
	private MultiList multiList;
	private ButtonPanel buttonPanel;
	
	private String invalidChampions = null;
	
	public void buildGui () {
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		
		Container mainPanel = frame.getContentPane();
		mainPanel.setLayout (new BorderLayout());
		mainPanel.setBackground (Color.BLACK);
		
		mainPanel.add (new JButton (new ResetAction()), BorderLayout.PAGE_START);
		
		championSelect = new ChampionSelect (mainPanel);

		multiList = new MultiList();
		mainPanel.add (multiList.getChampionSelect(), BorderLayout.CENTER);
		
		Box bottomBox = new Box (BoxLayout.PAGE_AXIS);
		
		buttonPanel = new ButtonPanel();
		
		Box infoBox = new Box (BoxLayout.LINE_AXIS);
		infoBox.setBorder (new EmptyBorder (5, 5, 5, 5));
		
		infoBox.add (new TeamInfoPanel (false));
		infoBox.add (Box.createHorizontalGlue());
	
		infoBox.add (new ChampionInfoPanel());
		
		infoBox.add (Box.createHorizontalGlue());
		infoBox.add (new TeamInfoPanel (true));
		
		bottomBox.add (buttonPanel);
		bottomBox.add (infoBox);
		
		mainPanel.add (bottomBox, BorderLayout.PAGE_END);

		frame.setPreferredSize (new Dimension (1215, 850));
		frame.pack();
		frame.setLocationRelativeTo (null);
		frame.setVisible (true);
		
		if (invalidChampions != null)
			JOptionPane.showMessageDialog (frame,
					"The following champions could not be loaded:" + invalidChampions,
					"Invalid Champions", JOptionPane.WARNING_MESSAGE);
	}
	
	public void setChampionWarning (String champions) {
		invalidChampions = champions;
	}
	
	public ChampionSelect getChampionSelect () {
		return championSelect;
	}
	
	public MultiList getMultiList () {
		return multiList;
	}
	
	public ButtonPanel getButtonPanel () {
		return buttonPanel;
	}
	
	private class ResetAction extends AbstractAction {
		public ResetAction () {
			super ("Reset");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Initialiser.reloadChampions();
			
			EventHandler.fireResetEvent();
		}
	}
}
