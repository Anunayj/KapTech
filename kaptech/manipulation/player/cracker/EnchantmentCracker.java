package kaptainwutax.kaptech.manipulation.player.cracker;

import java.util.ArrayList;

import kaptainwutax.kaptech.manipulation.SeedState;
import kaptainwutax.kaptech.manipulation.player.PlayerManipulation;

public class EnchantmentCracker extends Cracker {
	
	private long playerSeed = -1;
	private int[] partialXPseeds = new int[2];
	
	public long getPlayerSeed() {
		return this.playerSeed;
	}
	
	public void catchPacketSeed(int seed) {
		if(this.state == SeedState.UNKNOWN) {
			partialXPseeds[0] = seed;
			this.state = SeedState.LAZY;
		} else if(this.state == SeedState.LAZY) {
			if(seed == partialXPseeds[0])return;
			partialXPseeds[1] = seed;
			if(crackPlayerSeed()) {
				this.state = SeedState.ACCURATE;
			} else {
				resetCracker();
			}
		} else if(this.state == SeedState.ACCURATE) {
			resetCracker();
			catchPacketSeed(seed);
		}		
	}

	private boolean crackPlayerSeed() {
		boolean isSearchDone = false;
		long foundSeed = -1;
		for(int lb = 0; lb < (1 << 4); ++lb) {		
			int firstInt = this.partialXPseeds[0] | lb;
			
			for(int nlb = 0; nlb < (1 << 4); ++nlb) {
				int secondInt = this.partialXPseeds[1] | nlb;	
				
				for(int b = 0; b < (1 << 16); ++b) {
					long firstSeed = (Integer.toUnsignedLong(firstInt) << 16) | b;
					long secondSeed = (firstSeed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
					if((int)(secondSeed >>> 16) == secondInt) {
						foundSeed = secondSeed;
						isSearchDone = true;
						break;
					}
				}				
				if(isSearchDone)break;
			}
			if(isSearchDone)break;
		}
		
		if(!isSearchDone) {
			return false;
		}
		
		if(foundSeed == this.getPlayerSeed())return false;
		
		this.playerSeed = foundSeed;
		System.out.print("Player seed was found : [" + this.playerSeed + "].\n");	
		
		PlayerManipulation.getPlayerManip().setPlayerSeed(this.playerSeed);
		return true;
	}

	@Override
	protected void resetCracker() {
		this.playerSeed = -1;
		this.partialXPseeds = new int[2];
		this.state = SeedState.UNKNOWN;
	}
	
}
