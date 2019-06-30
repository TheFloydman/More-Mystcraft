package thefloydman.moremystcraft.entity;

import java.util.Collection;
import java.util.UUID;

import com.google.common.base.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityMaintainerSuit extends EntityLiving {

	public EntityMaintainerSuit(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
		this.setHealth(100.0F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public void knockBack(Entity entity, float strength, double xRatio, double zRatio) {
	}

	@Override
	public boolean canDespawn() {
		return true;
	}

	@Override
	public boolean isNoDespawnRequired() {
		return true;
	}

	@Override
	public boolean canBeLeashedTo(EntityPlayer player) {
		return false;
	}

	@Override
	public boolean canBeSteered() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return !this.isBeingRidden();
	}

	@Override
	public void onUpdate() {
		if (this.getPosition().getY() <= 0) {
			this.setPosition(this.getPosition().getX(), 0.0D, this.getPosition().getZ());
		}
		super.onUpdate();
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return false;
	}

	@Override
	public Entity getControllingPassenger() {
		if (this.isBeingRidden()) {
			return this.getPassengers().get(0);
		} else {
			return null;
		}
	}

	public void travel(float strafe, float vertical, float forward) {
		if (this.isBeingRidden() && this.canBeSteered()) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();
			this.rotationYaw = entitylivingbase.rotationYaw;
			this.prevRotationYaw = this.rotationYaw;
			this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
			this.setRotation(this.rotationYaw, this.rotationPitch);
			this.renderYawOffset = this.rotationYaw;
			this.rotationYawHead = this.renderYawOffset;
			strafe = 0.0F;
			forward = entitylivingbase.moveForward < 0 ? 0.0F : entitylivingbase.moveForward;
			this.setAIMoveSpeed(0.025F);
		}
		super.travel(strafe, vertical, forward);
	}

	@Override
	public void updatePassenger(Entity passenger) {
		if (this.isPassenger(passenger)) {
			passenger.setPosition(this.posX, this.posY + 1, this.posZ);

			if (!(passenger instanceof EntityPlayer)) {
				this.dismountEntity(passenger);
			}
		}
	}

	@Override
	public void onRemovedFromWorld() {
		super.onRemovedFromWorld();
	}

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}

}
