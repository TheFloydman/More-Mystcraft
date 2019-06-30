package thefloydman.moremystcraft.network.packets;

import java.nio.charset.Charset;
import java.util.UUID;
import java.util.function.Supplier;

import com.xcompwiz.mystcraft.Mystcraft;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.world.MoreMystcraftTeleporter;
import net.minecraft.world.DimensionType;

public class PacketSneakPressed implements IMessage {

	UUID playerUUID = UUID.randomUUID();

	public PacketSneakPressed(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	public PacketSneakPressed() {
	}

	public static class Handler implements IMessageHandler<PacketSneakPressed, IMessage> {

		@Override
		public IMessage onMessage(PacketSneakPressed message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				});
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.playerUUID = UUID
				.fromString(buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset()).toString());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeCharSequence(this.playerUUID.toString(), Charset.defaultCharset());
	}

}
