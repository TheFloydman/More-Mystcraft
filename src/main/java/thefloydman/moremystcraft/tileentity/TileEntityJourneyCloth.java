package thefloydman.moremystcraft.tileentity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityJourneyCloth extends TileEntity {

	protected UUID uuid;

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		UUID uuidIn = nbt.getUniqueId("uuid");
		if (uuidIn != null) {
			this.setUUID(uuidIn);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setUniqueId("uuid", this.getUUID());
		return nbt;
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public void setUUID(UUID uuidIn) {
		this.uuid = uuidIn;
		this.markDirty();
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), 1, this.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

}