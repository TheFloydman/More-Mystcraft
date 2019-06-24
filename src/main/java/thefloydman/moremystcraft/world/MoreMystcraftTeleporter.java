package thefloydman.moremystcraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class MoreMystcraftTeleporter implements ITeleporter {

	BlockPos pos;
	float rot;

	public MoreMystcraftTeleporter(BlockPos pos, float rot) {
		this.pos = pos;
		this.rot = rot;
	}

	@Override
	public void placeEntity(World world, Entity entity, float yaw) {
		// The "+ 0.5" here is to ensure the entity is centered on the spawn block.
		// LinkingBooksPacketHandler.sendSetEntityVelocityRequest(((EntityPlayerMP)
		// (entity)).connection.netManager, 0.0D,
		// 0.0D, 0.0D);
		entity.fallDistance = 0;
		entity.addVelocity(-entity.motionX, -entity.motionY, -entity.motionZ);
		entity.setPositionAndRotation(this.pos.getX() + 0.5, this.pos.getY(), this.pos.getZ() + 0.5, this.rot,
				entity.rotationPitch);
	}

}
