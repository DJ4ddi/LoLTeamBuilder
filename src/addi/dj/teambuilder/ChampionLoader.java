package addi.dj.teambuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ChampionLoader implements Callable<TreeSet<Champion>> {
	@Override
	public TreeSet<Champion> call () {
		
		TreeSet<Champion> rv = new TreeSet<Champion>();
		
		List<String> notLoaded = new ArrayList<String>();
		
		try {
			// Load the champions.xml
			Document champions = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse ("LoLTeamBuilder" + File.separator + "champions.xml");
			champions.getDocumentElement().normalize();
			
			NodeList championNodes = champions.getElementsByTagName ("champion");
			for (int i = 0; i < championNodes.getLength(); i++) {
				Element championElement = (Element) championNodes.item (i);
				
				String name = championElement.getAttribute ("name"); // Name
				try {
					Style style = Style.valueOf (championElement.getElementsByTagName ("style").item (0).getTextContent()); // Playstyle
					
					Element roleElement = (Element) championElement.getElementsByTagName ("roles").item (0);
					Role primaryRole = Role.valueOf (roleElement.getAttribute ("primary")); // Primary role

					List<Role> secondaryRoleList = new ArrayList<Role>(); // Secondary roles
					for (String s : getParsedList (roleElement.getChildNodes())) {
						Role r = Role.valueOf (s);
						if (!(r == primaryRole || secondaryRoleList.contains (r)))
							secondaryRoleList.add (Role.valueOf (s));
					}

					List<Position> positionList = new ArrayList<Position>(); // Types
					for (String s : getParsedList (championElement.getElementsByTagName("position")))
						positionList.add (Position.valueOf (s));
					
					List<String> countersList = getParsedList (championElement.getElementsByTagName ("counters")); // Counters
					
					// Create champion
					rv.add (new Champion (name, style, primaryRole, secondaryRoleList, positionList, countersList));
					
				} catch (IllegalArgumentException e) {
					notLoaded.add (name);
					continue;
				}
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}

		if (!notLoaded.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < notLoaded.size(); i++) {
				if (i % 10 == 0) sb.append ("\n");
				String s = notLoaded.get (i);
				sb.append (s).append (", ");
			}
			Initialiser.getMainFrame().setChampionWarning (sb.substring (0, sb.length() - 2));
		}

		return rv;
	}
	
	private List<String> getParsedList (NodeList n) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < n.getLength(); i++) {
			String s = n.item (i).getTextContent();
			if (!s.trim().isEmpty()) list.add (s);
		}
		return list;
	}
}
