package kaptainwutax.kaptech.manipulation.player;

import kaptainwutax.kaptech.manipulation.SeedState;
import kaptainwutax.kaptech.manipulation.player.cracker.Cracker;
import kaptainwutax.kaptech.manipulation.player.cracker.EnchantmentCracker;

public class PlayerManipulation {
	
	/*Singleton storing a global instance of the object.*/
	private static PlayerManipulation instance = null;
	
	/*The player RNG seed.*/
	private long seed = -1;
	
	/*The state of the player RNG seed. Is it cracked? Is is wrongly calculated?*/
	private SeedState state = SeedState.UNKNOWN;
	
	/*PlayerData stores a bunch of useful fields to be used in the cracking and PlayerManipulationEvent notifies the cracker when needed.*/
	private PlayerData data = new PlayerData();
	private PlayerManipulationEvent notifier;
	
	/*Those are the different crackers.*/
	private Cracker[] crackers;
				
	public PlayerManipulation() {
		this.data = new PlayerData();
		this.notifier = new PlayerManipulationEvent();
		
		this.crackers = new Cracker[] {
			new EnchantmentCracker()
		};
	}
	
	public static PlayerManipulation getPlayerManip() {
		if(instance == null)instance = new PlayerManipulation();
		return instance;
	}
	
	public PlayerData getData() {
		if(this.data == null)this.data = new PlayerData();
		this.data.syncData();
		return this.data;	
	}
	
	public PlayerManipulationEvent getNotifier() {
		if(this.notifier == null)this.notifier = new PlayerManipulationEvent();
		return this.notifier;	
	}
	
	public Cracker getCracker(int id) {
		if(id >= crackers.length)return null;
		return crackers[id];
	}

	public void setPlayerSeed(long playerSeed) {
		this.seed = playerSeed;
	}
	
}
