package thefloydman.moremystcraft.network;

import java.util.UUID;
import java.util.logging.Logger;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import thefloydman.moremystcraft.network.packets.PacketSneakPressed;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftPacketHandler {

	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

	private static int index = 0;

	public MoreMystcraftPacketHandler() {

	}

	public static void register() {
		CHANNEL.registerMessage(PacketSneakPressed.Handler.class, PacketSneakPressed.class, index++, Side.SERVER);
	}

	public static void sneakPressed(UUID uuid) {
		CHANNEL.sendToServer(new PacketSneakPressed(uuid));
	}

}