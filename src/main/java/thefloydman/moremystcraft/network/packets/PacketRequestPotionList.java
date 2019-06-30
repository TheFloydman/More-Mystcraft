package thefloydman.moremystcraft.network.packets;

import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Supplier;

import com.xcompwiz.mystcraft.Mystcraft;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.util.handlers.MaintainerSuitEventHandler;
import thefloydman.moremystcraft.util.handlers.PotionListHandler;

public class PacketRequestPotionList implements IMessage {

	int dimension;

	public PacketRequestPotionList(int dim) {
		this.dimension = dim;
	}

	public PacketRequestPotionList() {
	}

	public static class Handler implements IMessageHandler<PacketRequestPotionList, IMessage> {

		@Override
		public IMessage onMessage(PacketRequestPotionList msg, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				List<String> potions = MaintainerSuitEventHandler.getPotionsList(msg.dimension);
				if (potions != null) {

					for (int i = 0; i < potions.size(); i++) {
						MoreMystcraftPacketHandler.sendPotion(msg.dimension, potions.get(i),
								(EntityPlayerMP) ctx.getServerHandler().player);
					}
				}
			});
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.dimension = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.dimension);
	}

}
