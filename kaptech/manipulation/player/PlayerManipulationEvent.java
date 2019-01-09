package kaptainwutax.kaptech.manipulation.player;

import kaptainwutax.kaptech.manipulation.player.cracker.EnchantmentCracker;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.network.play.server.SPacketWindowProperty;

public class PlayerManipulationEvent {
	
	/**
	 * TODO : add information for this packet.
	 * 
	 * Caller is <strong>handleWindowProperty(SPacketWindowProperty packetIn)</strong>,
	 * in java class : <strong>net.minecraft.client.network.NetHandlerPlayerClient</strong>.
	 * 
	 * @param packet Packet received for the event.
	 **/
	public void onReceivePacketWindowProperty(SPacketWindowProperty packet) {
		if(!Thread.currentThread().getName().equals("Client thread"))return;
		PlayerManipulation manip = PlayerManipulation.getPlayerManip();
		Minecraft mc = manip.getData().getMinecraft();
		Container container = mc.player.openContainer;
		
		if(container != null && container instanceof ContainerEnchantment && container.windowId == packet.getWindowId()) {
			ContainerEnchantment enchantmentContainer = (ContainerEnchantment)container;			
			/*This packet is sharing some xpSeed bits.*/
			if(packet.getProperty() == 3) {
				EnchantmentCracker enchCracker = (EnchantmentCracker)(manip.getCracker(0));
				enchCracker.catchPacketSeed(packet.getValue());
			}
		}
	}
	
}
