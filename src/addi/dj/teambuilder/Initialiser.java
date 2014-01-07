package addi.dj.teambuilder;

import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.UIManager;

public class Initialiser {
	
	public final static ExecutorService exec = Executors.newCachedThreadPool();
	
	private static MainFrame mainFrame;
	
	private static Future<TreeSet<Champion>> championList;
	
	public static void main(String[] args) {
		
		mainFrame = new MainFrame();
		reloadChampions();
		
		// Set look and feel
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start building the UI in a new thread
		javax.swing.SwingUtilities.invokeLater (new Runnable() {
			public void run() {
				mainFrame.buildGui();
			}
		});
	}
	
	public static MainFrame getMainFrame () {
		return mainFrame;
	}
	
	public static TreeSet<Champion> getChampionList () {
		try {
			return championList.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void reloadChampions () {
		championList = exec.submit (new ChampionLoader());
	}
}
