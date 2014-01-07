package addi.dj.teambuilder.panels.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import addi.dj.teambuilder.Champion;
import addi.dj.teambuilder.Initialiser;
import addi.dj.teambuilder.events.EventHandler;
import addi.dj.teambuilder.events.ResetListener;
import addi.dj.teambuilder.events.SlotLockListener;

public class MultiList implements Runnable {
	
	SelectionField searchField = new SelectionField();
	
	CustomCellRenderer cellRenderer = new CustomCellRenderer();
	
	private DefaultListModel<Champion> recommendedModel;
	private DefaultListModel<Champion> goodModel;
	private DefaultListModel<Champion> viableModel;
	private DefaultListModel<Champion> allModel;
	
	private ChampionList recommendedList;
	private ChampionList goodList;
	private ChampionList viableList;
	private ChampionList allList;
	
	private MultiList instance;
	
	public MultiList () {

		recommendedModel = new DefaultListModel<Champion> ();
		goodModel = new DefaultListModel<Champion> ();
		viableModel = new DefaultListModel<Champion> ();
		allModel = new DefaultListModel<Champion> ();
		
		instance = this;
		submit();
		
		EventHandler.addSlotLockListener (new SlotLockListener() {
			@Override
			public void onSlotLock() {
				submit();
			}
		});
		
		EventHandler.addResetListener (new ResetListener() {
			@Override
			public void onReset() {
				reset();
				submit();
			}
		});
	}
	
	private void submit () {
		Initialiser.exec.submit (instance);
	}
	
	public void run () {
		searchField.setText ("");
		searchField.setText (SelectionField.SEARCHTERM);
		
		TreeMap<Integer, List<Champion>> priorities = new TreeMap<Integer, List<Champion>>();
		for (Champion c : Initialiser.getChampionList()) {
			int priority = c.calculatePriority();
			List<Champion> priorityList = priorities.get (priority);
			if (priorityList == null) {
				priorityList = new LinkedList<Champion>();
				priorities.put (priority, priorityList);
			}
			priorityList.add (c);
		}

		Entry<Integer, List<Champion>> e = priorities.pollLastEntry();
		recommendedModel.clear();
		goodModel.clear();
		viableModel.clear();
		allModel.clear();
		if (e != null) {
			for (Champion c : e.getValue())
				recommendedModel.addElement (c);

			e = priorities.pollLastEntry();
			if (e != null) {
				for (Champion c : e.getValue())
					goodModel.addElement (c);

				e = priorities.pollLastEntry();
				if (e != null) {
					for (Champion c : e.getValue())
						viableModel.addElement (c);
					
					e = priorities.pollLastEntry();
					while (e != null) {
						for (Champion c : e.getValue())
							allModel.addElement (c);
						e = priorities.pollLastEntry();
					}
				}
			}
		}
		
		reset();
		scroller.revalidate();
	}
	
	public void reset () {
		recommendedList.clearSelection();
		goodList.clearSelection();
		viableList.clearSelection();
		allList.clearSelection();
	}
	
	public Champion getSelectedValue () {
		Champion rv = recommendedList.getSelectedValue();
		if (rv == null) rv = goodList.getSelectedValue();
		else return rv;
		if (rv == null) rv = viableList.getSelectedValue();
		else return rv;
		if (rv == null) rv = allList.getSelectedValue();
		return rv;
	}
	
	private JScrollPane scroller;
	
	public JPanel getChampionSelect () {
		JPanel rv = new JPanel (new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		String imagePath = "LoLTeamBuilder" + File.separator + "images" + File.separator;

		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		rv.add (searchField, gbc);
		
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		rv.add (new JLabel (new ImageIcon (imagePath + "border_top_label.png")), gbc);
		
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		rv.add (new JLabel (new ImageIcon (imagePath + "border_top.png")), gbc);
		
		gbc.gridy = 3;
		rv.add (new JLabel (new ImageIcon (imagePath + "border_bottom.png")), gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 2;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		
		ScrollableBox championListBox = new ScrollableBox();
		
		Dimension spacer = new Dimension (0, 5);

		LabelPanel recommended = new LabelPanel ("Recommended");
		championListBox.add (recommended);
		
		FilterListModel model = new FilterListModel (recommendedModel);
		searchField.addConnectedList (model);
		recommendedList = new ChampionList (model, cellRenderer);
		scroller = new JScrollPane (recommendedList);
		scroller.getViewport().setBackground (Color.BLACK);
		scroller.setBorder (null);
		championListBox.add (scroller);
		
		championListBox.add (Box.createRigidArea (spacer));

		LabelPanel good = new LabelPanel ("Good");
		championListBox.add (good);
		
		model = new FilterListModel (goodModel);
		searchField.addConnectedList (model);
		goodList = new ChampionList (model, cellRenderer);
		scroller = new JScrollPane (goodList);
		scroller.getViewport().setBackground (Color.BLACK);
		scroller.setBorder (null);
		championListBox.add (scroller);
		
		championListBox.add (Box.createRigidArea (spacer));

		LabelPanel viable = new LabelPanel ("Viable");
		championListBox.add (viable);
		
		model = new FilterListModel (viableModel);
		searchField.addConnectedList (model);
		viableList = new ChampionList (model, cellRenderer);
		scroller = new JScrollPane (viableList);
		scroller.getViewport().setBackground (Color.BLACK);
		scroller.setBorder (null);
		championListBox.add (scroller);
		
		championListBox.add (Box.createRigidArea (spacer));

		LabelPanel all = new LabelPanel ("All");
		championListBox.add (all);
		
		model = new FilterListModel (allModel);
		searchField.addConnectedList (model);
		allList = new ChampionList (model, cellRenderer);
		scroller = new JScrollPane (allList);
		scroller.getViewport().setBackground (Color.BLACK);
		scroller.setBorder (null);
		championListBox.add (scroller);
		
		scroller = new JScrollPane (championListBox);
		scroller.getViewport().setBackground (Color.BLACK);
		scroller.setViewportBorder (new EmptyBorder (5, 9, 5, 9));
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setBorder (BorderFactory.createMatteBorder (0, 1, 0, 1, Color.WHITE));
		rv.add (scroller, gbc);
		rv.setBorder (new EmptyBorder (5, 15, 10, 15));
		
		return rv;
	}
}
