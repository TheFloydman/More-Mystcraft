package thefloydman.moremystcraft.entity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityPotionDummy extends EntityLiving {

	private static final DataParameter<Optional<UUID>> PARENT_UUID = EntityDataManager
			.<Optional<UUID>>createKey(EntityPotionDummy.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	public EntityPotionDummy(World world) {
		super(world);
		this.setSize(1.0F, 1.0F);
		this.setHealth(20.0F);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(PARENT_UUID, Optional.absent());
	}

	@Nullable
	public UUID getParent() {
		return (UUID) ((Optional) this.dataManager.get(PARENT_UUID)).orNull();
	}

	public void setParent(@Nullable UUID uniqueId) {
		this.dataManager.set(PARENT_UUID, Optional.fromNullable(uniqueId));
	}

	@Override
	public boolean canDespawn() {
		return false;
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
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void onUpdate() {
		UUID uuid = this.getParent();
		if (this.getParent() != null) {
			EntityPlayer parent = this.getEntityWorld().getPlayerEntityByUUID(uuid);
			if (parent != null) {
				this.setPosition(parent.posX, parent.posY + 300, parent.posZ);
				this.setHealth(20.0F);
				return;
			}
		}
		this.setDead();
		this.despawnEntity();
	}

	@Override
	public void onRemovedFromWorld() {
		boolean letLive = false;
		List<EntityPlayer> players = this.getEntityWorld().playerEntities;
		for (EntityPlayer player : players) {
			if (player.getUniqueID().equals(this.getParent())) {
				letLive = true;
				break;
			}
		}
		if (letLive == false) {
			this.setDead();
			this.despawnEntity();
		}
		super.onRemovedFromWorld();
	}

}
