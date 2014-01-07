package addi.dj.teambuilder.panels.components;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import addi.dj.teambuilder.Champion;
import addi.dj.teambuilder.Position;

@SuppressWarnings("serial")
public class FilterListModel extends AbstractListModel<Champion> {

	public static class Filter {

		private Position searchPosition = null;
		private String searchString;

		public Filter (String searchString) {
			try {
				searchPosition = Position.valueOf (searchString.toUpperCase());
			} catch (IllegalArgumentException e) {}
			this.searchString = searchString.toLowerCase();
		}

		public boolean accept (Champion c) {
			if (searchPosition != null)
				if (c.hasPosition (searchPosition)) return true;
			if (c.getName().toLowerCase().contains (searchString)) return true;
			return false;
		}

		public String getSearchString () {
			return searchString;
		}
	}

	private DefaultListModel<Champion> source;
	private Filter filter;
	private final ArrayList<Integer> displayedElements = new ArrayList<Integer>();

	public FilterListModel (DefaultListModel<Champion> source) {
		this.source = source;

		this.source.addListDataListener (new ListDataListener() {
			@Override
			public void intervalRemoved (ListDataEvent e) {
				filter();
			}

			@Override
			public void intervalAdded (ListDataEvent e) {
				filter();
			}

			@Override
			public void contentsChanged (ListDataEvent e) {
				filter();
			}
		});
	}

	public void setFilter (Filter f) {
		filter = f;
		filter();
	}

	private void filter () {
		displayedElements.clear();

		if (filter != null)
			for (int i = 0; i < source.getSize(); i++)
				if (filter.accept (source.getElementAt (i)))
					displayedElements.add (i);

		fireContentsChanged (this, 0, getSize() - 1);
	}

	@Override
	public int getSize () {
		return (filter != null) ? displayedElements.size()
				: source.getSize();
	}

	@Override
	public Champion getElementAt (int i) {
		if (getSize() == 0) return null;
		return (filter != null) ?
				source.getElementAt (displayedElements.get (i))
				: source.getElementAt (i);
	}
	
	public boolean isIndexVisible (int i) {
		return (filter != null) ? displayedElements.contains (i) : true;
	}
	
	public int indexOf (Champion c) {
		return source.indexOf (c);
	}

	public List<Champion> getVisibleElements () {
		List<Champion> rv = new LinkedList<Champion>();
		for (int i = 0; i < getSize(); i++)
			rv.add (getElementAt (i));
		return rv;
	}
}
