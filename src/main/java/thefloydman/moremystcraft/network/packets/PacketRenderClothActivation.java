package thefloydman.moremystcraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thefloydman.moremystcraft.tileentity.TileEntitySingleItem;
import thefloydman.moremystcraft.util.MoreMystcraftSoundEvents;

public class PacketRenderClothActivation implements IMessage {

	BlockPos blockPos;

	public PacketRenderClothActivation(BlockPos pos) {
		this.blockPos = pos;
	}

	public PacketRenderClothActivation() {
	}

	public static class Handler implements IMessageHandler<PacketRenderClothActivation, IMessage> {

		@Override
		public IMessage onMessage(PacketRenderClothActivation msg, MessageContext ctx) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			World world = player.world;
			((TileEntitySingleItem) world.getTileEntity(msg.blockPos)).setTimer(0);
			player.playSound(MoreMystcraftSoundEvents.JOURNEY_CLOTH_ACTIVATE, 1.0F, 1.0F);
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.blockPos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.blockPos.toLong());
	}

}
