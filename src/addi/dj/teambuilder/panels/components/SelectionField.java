package addi.dj.teambuilder.panels.components;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.LinkedList;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import addi.dj.teambuilder.panels.components.FilterListModel.Filter;

public class SelectionField extends JTextField {
	
	public final static String SEARCHTERM = "Search";
	
	private LinkedList<FilterListModel> connectedLists = new LinkedList<FilterListModel>();

	public SelectionField () {
		super (SEARCHTERM, 12);
		setMaximumSize (getPreferredSize());
		setMinimumSize (getPreferredSize());
		setHorizontalAlignment (JTextField.RIGHT);

		addFocusListener (new FocusListener () {
			public void focusGained (FocusEvent e) {
				if (getText().equals (SEARCHTERM)) setText ("");
			}

			public void focusLost(FocusEvent e) {
				if (getText().isEmpty()) setText (SEARCHTERM);
			}
		});

		getDocument().addDocumentListener (new DocumentListener () {
			public void changedUpdate(DocumentEvent e) {
				if (!getText().equals (SEARCHTERM)) {
					if (!getText().isEmpty()) {
						for (FilterListModel l : connectedLists)
							l.setFilter (new Filter (getText()));
					} else {
						for (FilterListModel l : connectedLists)
							l.setFilter (null);
					}
				}
			}

			public void insertUpdate(DocumentEvent e) {
				changedUpdate (e);
			}

			public void removeUpdate(DocumentEvent e) {
				changedUpdate (e);
			}
		});
	}
	
	public void addConnectedList (FilterListModel l) {
		connectedLists.add (l);
	}
}
