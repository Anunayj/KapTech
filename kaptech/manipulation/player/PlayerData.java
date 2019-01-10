package kaptainwutax.kaptech.manipulation.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class PlayerData {
	
	private Minecraft minecraft;
	
	public int heldSlot;
	
	public EntityPlayer player;
	public PlayerControllerMP playerController;
	
	public GuiScreen playerScreen;
	public Container playerOpenContainer;
	public InventoryPlayer playerInventory;
	
	public Minecraft getMinecraft() {
		if(this.minecraft == null)this.minecraft = Minecraft.getMinecraft();
		syncData();
		return this.minecraft;		
	}

	public void syncData() {
		if(this.minecraft == null)this.minecraft = Minecraft.getMinecraft();
		
		this.heldSlot = minecraft.player.inventory.currentItem;
		
		this.player = minecraft.player;
		this.playerController = minecraft.playerController;
		
		this.playerScreen = minecraft.currentScreen;
		this.playerOpenContainer = minecraft.player.openContainer;
		this.playerInventory = minecraft.player.inventory;
	}
	
}
