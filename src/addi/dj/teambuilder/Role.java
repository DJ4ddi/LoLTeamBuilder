package addi.dj.teambuilder;

public enum Role {
	MAPCONTROL (Strategy.Split_Push),
	DISENGAGE (Strategy.Jungle_Control),
	TEAMFIGHT (Strategy.Flexibility),
	ENGAGE (Strategy.Teamfight),
	BURST (Strategy.Hyper_Carry);
	
	private Strategy associatedStrategy;
	
	private Role (Strategy s) {
		associatedStrategy = s;
	}
	
	public Strategy getAssociatedStrategy () {
		return associatedStrategy;
	}
}
