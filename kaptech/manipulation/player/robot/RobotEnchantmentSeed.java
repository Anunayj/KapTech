package kaptainwutax.kaptech.manipulation.player.robot;

import kaptainwutax.kaptech.manipulation.player.PlayerData;
import kaptainwutax.kaptech.manipulation.player.PlayerManipulation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class RobotEnchantmentSeed {
	/**
	 * Implemented in <strong>client.entity.EntityPlayerSP#onUpdate()</strong> as :
	 * 
	 * if(Keyboard.isKeyDown(Keyboard.KEY_C)) {
     *		if(this.ticksExisted % 20 == 0)RobotEnchantmentSeed.startRobot();
     * }
     * 
     * RobotEnchantmentSeed.onUpdate();
	 * */
	static int[] inventorySlots = {-1, -1, -1};
	public static boolean isRobotRunning = false;
	
	public static void startRobot() {
		if(Minecraft.getMinecraft() == null)return;
		isRobotRunning = true;
		
		PlayerManipulation manip = PlayerManipulation.getPlayerManip();
		PlayerData data = manip.getData();
		Minecraft mc = data.getMinecraft();
		
		EntityPlayer player = data.player;
		PlayerControllerMP playerController = data.playerController;
		InventoryPlayer inventory = data.playerInventory;
		ContainerEnchantment enchContainer = null;
		
		/*Does a bunch of checks to see if the player CAN activate the robot.*/
		if(!player.isCreative() && player.experienceLevel < 2) {
			System.out.println("Not enough experience to perform the enchanting.");
			return;
		}
		
		for(int s = 0; s < 9; ++s) {
			ItemStack stack = inventory.getStackInSlot(s);
			
			if(stack.getItem() == Item.getItemFromBlock(Blocks.ENCHANTING_TABLE)) {
				inventorySlots[0] = s;
			} else if(stack.isItemEnchantable()) {
				inventorySlots[1] = s; 
			} else if(stack.getItem() == Items.DYE) {
				inventorySlots[2] = s;
			}
		}
		
		String[] ingredients = new String[] {"an Enchantment Table", "an Enchantable Dummy Item", "atleast one piece of lapis"};
		for(int i = 0; i < 3; ++i) {
			if(inventorySlots[i] == -1) {
				if(i == 2 && player.isCreative())continue;
				System.out.println("Missing " + ingredients[i] + ". Remember to put it in your hotbar inventory.");
				return;
			}
		}
		
		/*Switches the current item the player is holding to the enchantment table.*/
		mc.player.inventory.currentItem = inventorySlots[0];
		mc.getConnection().sendPacket(new CPacketHeldItemChange(inventorySlots[0]));
		
		/*Place a enchantment table towards the east and opens it.*/
		BlockPos pos = mc.player.getPosition().east(2);
		mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));		
		mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
		
		/*We don't do anything yet because the client still doesn't know that the enchantment container is open.*/
	}
	
	public static void onUpdate() {
		if(!isRobotRunning)return;

		PlayerData data = PlayerManipulation.getPlayerManip().getData();
		Minecraft mc = data.getMinecraft();

		Container container = data.playerOpenContainer;
        
        /*Checks if the player got the right-click packet and asked the client to open the enchantment window.*/
        if(data.playerScreen == null || !(container instanceof ContainerEnchantment))return;
		
		/*Some useful objects...*/
		EntityPlayer player = data.player;
		PlayerControllerMP playerController = data.playerController;
		
		/*Puts the item in the enchant item slot.*/
		playerController.windowClick(player.openContainer.windowId, inventorySlots[1] + 29, 0, ClickType.QUICK_MOVE, player);	
	
		/*If the player is in creative mode, add some lapis.*/
		if(!player.isCreative()) {
			playerController.windowClick(player.openContainer.windowId, inventorySlots[2] + 29, 0, ClickType.QUICK_MOVE, player);	
		}
		
		/*Enchant the item.*/
		mc.getConnection().sendPacket(new CPacketEnchantItem(player.openContainer.windowId, 0));	
		
		/*Take the enchanted item and lapis back.*/
		playerController.windowClick(player.openContainer.windowId, 0, 0, ClickType.QUICK_MOVE, player);	
		playerController.windowClick(player.openContainer.windowId, 1, 0, ClickType.QUICK_MOVE, player);
		
		/*Tell the server to close the window.*/
		mc.getConnection().sendPacket(new CPacketCloseWindow(player.openContainer.windowId));	
		
		/*Close the window on the client.*/
		mc.displayGuiScreen(null);
		
		/*Sync the data to PlayerData and reset the robot.*/
		data.syncData();
		isRobotRunning = false;
		inventorySlots = new int[] {-1, -1, -1};
	}
	
}
