package thefloydman.moremystcraft.network.packets;

import java.nio.charset.Charset;
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
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.util.handlers.PotionListHandler;

public class PacketPotion implements IMessage {

	int dimension;
	String potion;

	public PacketPotion(int dim, String str) {
		this.dimension = dim;
		this.potion = str;
	}

	public PacketPotion() {
	}

	public static class Handler implements IMessageHandler<PacketPotion, IMessage> {

		@Override
		public IMessage onMessage(PacketPotion msg, MessageContext ctx) {
			PotionListHandler.receivePotion(msg.dimension, msg.potion);
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.dimension = buf.readInt();
		this.potion = buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset()).toString();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.dimension);
		buf.writeCharSequence(this.potion, Charset.defaultCharset());
	}

}
