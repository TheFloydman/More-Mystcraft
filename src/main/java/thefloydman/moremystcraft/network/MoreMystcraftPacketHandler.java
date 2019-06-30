package thefloydman.moremystcraft.network;

import java.util.UUID;
import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import thefloydman.moremystcraft.network.packets.PacketPotion;
import thefloydman.moremystcraft.network.packets.PacketRequestPotionList;
import thefloydman.moremystcraft.network.packets.PacketSneakPressed;
import thefloydman.moremystcraft.network.packets.PacketSpawnMaintainerSuit;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftPacketHandler {

	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

	private static int index = 0;

	public MoreMystcraftPacketHandler() {

	}

	public static void register() {
		CHANNEL.registerMessage(PacketPotion.Handler.class, PacketPotion.class, index++, Side.CLIENT);
		CHANNEL.registerMessage(PacketRequestPotionList.Handler.class, PacketRequestPotionList.class, index++,
				Side.SERVER);
		CHANNEL.registerMessage(PacketSpawnMaintainerSuit.Handler.class, PacketSpawnMaintainerSuit.class, index++,
				Side.SERVER);
	}

	public static void sneakPressed(UUID uuid) {
		CHANNEL.sendToServer(new PacketSneakPressed(uuid));
	}

	public static void sendPotion(int dim, String str, EntityPlayerMP player) {
		CHANNEL.sendTo(new PacketPotion(dim, str), player);
	}

	public static void requestPotionList(int dim) {
		CHANNEL.sendToServer(new PacketRequestPotionList(dim));
	}

	public static void spawnMaintainerSuit(BlockPos pos, float yaw) {
		CHANNEL.sendToServer(new PacketSpawnMaintainerSuit(pos, yaw));
	}

}