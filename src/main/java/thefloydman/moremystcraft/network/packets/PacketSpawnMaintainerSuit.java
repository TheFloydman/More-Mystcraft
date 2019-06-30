package thefloydman.moremystcraft.network.packets;

import java.nio.charset.Charset;
import java.util.function.Supplier;

import com.xcompwiz.mystcraft.Mystcraft;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.util.handlers.PotionListHandler;

public class PacketSpawnMaintainerSuit implements IMessage {

	BlockPos blockPos;
	float rotation;

	public PacketSpawnMaintainerSuit(BlockPos pos, float yaw) {
		this.blockPos = pos;
		this.rotation = yaw;
	}

	public PacketSpawnMaintainerSuit() {
	}

	public static class Handler implements IMessageHandler<PacketSpawnMaintainerSuit, IMessage> {

		@Override
		public IMessage onMessage(PacketSpawnMaintainerSuit msg, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				World world = ctx.getServerHandler().player.getEntityWorld();
				EntityMaintainerSuit entity = new EntityMaintainerSuit(world);
				entity.setPosition(msg.blockPos.getX() + 0.5D, msg.blockPos.getY() + 1.0D, msg.blockPos.getZ() + 0.5D);
				world.spawnEntity(entity);
			});
			return null;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.rotation = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.blockPos.getX());
		buf.writeInt(this.blockPos.getY());
		buf.writeInt(this.blockPos.getZ());
		buf.writeFloat(this.rotation);
	}

}
