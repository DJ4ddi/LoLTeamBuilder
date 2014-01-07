package addi.dj.teambuilder.panels.components;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import addi.dj.teambuilder.Champion;

public class CustomCellRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value == null) return null;
		return ((Champion) value).getListIcon();
	}
}