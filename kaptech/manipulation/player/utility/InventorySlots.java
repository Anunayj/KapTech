package kaptainwutax.kaptech.manipulation.player.utility;

public class InventorySlots {
	
    public static int toNetworkSlot(int slot) {
        if (slot <= 8)
            slot += 36;
        else if (slot == 100)
            slot = 8;
        else if (slot == 101)
            slot = 7;
        else if (slot == 102)
            slot = 6;
        else if (slot == 103)
            slot = 5;
        else if (slot >= 80 && slot <= 83)
            slot -= 79;
        else return -1;
        
        return slot;
    }
    
}
