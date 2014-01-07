package addi.dj.teambuilder.events;

import java.util.LinkedList;
import java.util.List;

import addi.dj.teambuilder.Champion;

public class EventHandler {
	
	private static List<InfoUpdateListener> infoListeners = new LinkedList<InfoUpdateListener>();
	private static List<SlotLockListener> lockListeners = new LinkedList<SlotLockListener>();
	private static List<ResetListener> resetListeners = new LinkedList<ResetListener>();
	
	public static void addInfoUpdateListener (InfoUpdateListener l) {
		infoListeners.add (l);
	}
	
	public static void addSlotLockListener (SlotLockListener l) {
		lockListeners.add (l);
	}
	
	public static void addResetListener (ResetListener l) {
		resetListeners.add (l);
	}
	
	public static void fireInfoUpdateEvent (Champion c) {
		for (InfoUpdateListener l : infoListeners)
			l.onInfoUpdate (c);
	}
	
	public static void fireSlotLockEvent () {
		for (SlotLockListener l : lockListeners)
			l.onSlotLock();
	}
	
	public static void fireResetEvent () {
		for (ResetListener l : resetListeners)
			l.onReset();
	}
}
