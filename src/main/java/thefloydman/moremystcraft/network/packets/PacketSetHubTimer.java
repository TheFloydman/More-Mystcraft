package thefloydman.moremystcraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thefloydman.moremystcraft.capability.journeyhub.ICapabilityHub;
import thefloydman.moremystcraft.capability.journeyhub.ProviderCapabilityHub;
import thefloydman.moremystcraft.tileentity.TileEntitySingleItem;

public class PacketSetHubTimer implements IMessage {

	BlockPos blockPos;
	int ticks;

	public PacketSetHubTimer(BlockPos pos, int time) {
		this.blockPos = pos;
		this.ticks = time;
	}

	public PacketSetHubTimer() {
	}

	public static class Handler implements IMessageHandler<PacketSetHubTimer, IMessage> {

		@Override
		public IMessage onMessage(PacketSetHubTimer msg, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				World world = ctx.getServerHandler().player.getEntityWorld();
				ICapabilityHub cap = ((TileEntitySingleItem) world.getTileEntity(msg.blockPos)).getItem()
						.getCapability(ProviderCapabilityHub.HUB, null);
				if (cap != null) {
					cap.setTimeLimit(msg.ticks);
				}
			});
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.blockPos = BlockPos.fromLong(buf.readLong());
		this.ticks = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.blockPos.toLong());
		buf.writeInt(this.ticks);
	}

}
