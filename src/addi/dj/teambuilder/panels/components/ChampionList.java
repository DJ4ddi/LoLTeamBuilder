package addi.dj.teambuilder.panels.components;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import addi.dj.teambuilder.Champion;
import addi.dj.teambuilder.Initialiser;
import addi.dj.teambuilder.events.EventHandler;
import addi.dj.teambuilder.panels.ButtonPanel;
import addi.dj.teambuilder.panels.PositionBox;

public class ChampionList extends JList<Champion> {
	
	private final CustomMouseListener mouseListener = new CustomMouseListener();
	
	public ChampionList (FilterListModel listModel, CustomCellRenderer cellRenderer) {
		setModel (listModel);
		setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		setLayoutOrientation (JList.HORIZONTAL_WRAP);
		setVisibleRowCount (-1);
		setBackground (Color.BLACK);
		setCellRenderer (cellRenderer);
		
		Champion firstEntry = Initialiser.getChampionList().first();
		if (firstEntry != null)
			setPrototypeCellValue (firstEntry);
		
		for (MouseListener m : getMouseListeners()) {
			removeMouseListener (m);
		}
		for (MouseMotionListener m : getMouseMotionListeners()) {
			removeMouseMotionListener (m);
		}
		addMouseListener (mouseListener);
		addMouseMotionListener (mouseListener);
	}
	
	private void setIndexHovered (int i, boolean hovered) {
		if (i != -1) {
			getModel().getElementAt (i).getListIcon().setHovered (hovered);
			repaint (i);
		}
	}
	
	private class CustomMouseListener extends MouseAdapter {
		
		private int index = -1;
		
		@Override
		public void mouseClicked (MouseEvent e) {
			if (index != -1) {
				PositionBox selectedPosition = Initialiser.getMainFrame().getChampionSelect().getSelectedPosition();
				Champion c = getModel().getElementAt (index);
				
				if (selectedPosition != null) {
					selectedPosition.updateChampion (c);
					ButtonPanel.lockButton.setEnabled (true);
				}
				
				EventHandler.fireInfoUpdateEvent (c);
				
				Initialiser.getMainFrame().getMultiList().reset();
				setSelectedIndex (index);
			}
		}
		
		@Override
		public void mouseMoved (MouseEvent e) {
			if (index != -1) {
				int newIndex = locationToIndex (e.getPoint());
				if (newIndex != index) {
					setIndexHovered (index, false);
					index = newIndex;
					setIndexHovered (index, true);
				}
			}
		}
		
		@Override
		public void mouseEntered (MouseEvent e) {
			index = locationToIndex (e.getPoint());
			setIndexHovered (index, true);
		}
		
		@Override
		public void mouseExited (MouseEvent e) {
			setIndexHovered (index, false);
			index = -1;
		}
	}
}
