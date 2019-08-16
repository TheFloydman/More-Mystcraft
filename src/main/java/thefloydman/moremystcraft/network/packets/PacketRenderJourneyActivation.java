package thefloydman.moremystcraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thefloydman.moremystcraft.tileentity.TileEntityJourney;
import thefloydman.moremystcraft.util.JourneyUtils;
import thefloydman.moremystcraft.util.MoreMystcraftSoundEvents;

public class PacketRenderJourneyActivation implements IMessage {

	BlockPos blockPos;

	public PacketRenderJourneyActivation(BlockPos pos) {
		this.blockPos = pos;
	}

	public PacketRenderJourneyActivation() {
	}

	public static class Handler implements IMessageHandler<PacketRenderJourneyActivation, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(PacketRenderJourneyActivation msg, MessageContext ctx) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			World world = player.world;
			TileEntityJourney te = (TileEntityJourney) world.getTileEntity(msg.blockPos);
			te.setTimer(0);
			if (te.getType().equals(JourneyUtils.BlockType.CLOTH)) {
				player.playSound(MoreMystcraftSoundEvents.JOURNEY_CLOTH_ACTIVATE, 1.0F, 1.0F);
			}
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
