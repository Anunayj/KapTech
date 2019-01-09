package kaptainwutax.kaptech.manipulation.player.cracker;

import kaptainwutax.kaptech.manipulation.SeedState;

public abstract class Cracker {
	
	protected SeedState state = SeedState.UNKNOWN;
	
	public SeedState getState() {
		return state;
	}
	
	protected void resetCracker() {		
	}
	
}
