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
		return this.minecraft;		
	}

	public void syncData() {
		this.heldSlot = getMinecraft().player.inventory.currentItem;
		
		this.player = getMinecraft().player;
		this.playerController = getMinecraft().playerController;
		
		this.playerScreen = getMinecraft().currentScreen;
		this.playerOpenContainer = getMinecraft().player.openContainer;
		this.playerInventory = getMinecraft().player.inventory;
	}
	
}
