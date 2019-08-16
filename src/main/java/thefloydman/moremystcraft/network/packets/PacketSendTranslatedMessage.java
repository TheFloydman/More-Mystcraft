package thefloydman.moremystcraft.network.packets;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSendTranslatedMessage implements IMessage {

	String key;

	public PacketSendTranslatedMessage(String msg) {
		this.key = msg;
	}

	public PacketSendTranslatedMessage() {
	}

	public static class Handler implements IMessageHandler<PacketSendTranslatedMessage, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(PacketSendTranslatedMessage msg, MessageContext ctx) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			player.sendMessage(new TextComponentTranslation(msg.key));
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.key = buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset()).toString();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeCharSequence(this.key, Charset.defaultCharset());
	}

}
