package thefloydman.moremystcraft.network.packets;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;
import thefloydman.moremystcraft.util.Reference;

public class PacketSendTranslatedMessage implements IMessage {

	String key;
	int messageType;
	String username;
	int usernameLength;

	public PacketSendTranslatedMessage(String msg, int type, String name) {
		this.key = msg;
		this.messageType = type;
		this.username = name;
		this.usernameLength = this.username.length();
	}

	public PacketSendTranslatedMessage() {
		this.key = "";
		this.messageType = Reference.MessageType.GENERAL.ordinal();
		this.username = "";
		this.usernameLength = this.username.length();
	}

	public static class Handler implements IMessageHandler<PacketSendTranslatedMessage, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(PacketSendTranslatedMessage msg, MessageContext ctx) {
			if (msg.messageType == Reference.MessageType.STATUS.ordinal()) {
				EntityPlayer player = Minecraft.getMinecraft().player;
				player.sendStatusMessage(new TextComponentTranslation(msg.key), true);
			} else if (msg.messageType == Reference.MessageType.LINKING_LAG.ordinal()) {
				if (MoreMystcraftConfig.getPostMessageOnLink()) {
					EntityPlayer player = Minecraft.getMinecraft().player;
					player.sendMessage(new TextComponentString(
							TextFormatting.ITALIC + msg.username + " " + new TextComponentTranslation(msg.key).getUnformattedText()));
				}
			}
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.messageType = buf.readInt();
		this.username = buf.readCharSequence(buf.readInt(), Charset.defaultCharset()).toString();
		this.key = buf.readCharSequence(buf.readableBytes(), Charset.defaultCharset()).toString();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.messageType);
		buf.writeInt(this.usernameLength);
		buf.writeCharSequence(this.username, Charset.defaultCharset());
		buf.writeCharSequence(this.key, Charset.defaultCharset());
	}

}
