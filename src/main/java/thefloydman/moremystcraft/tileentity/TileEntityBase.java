package thefloydman.moremystcraft.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityBase extends TileEntity
{
    public final void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.readCustomNBT(compound);
    }
    
    public void readCustomNBT(final NBTTagCompound compound) {
    }
    
    public void readNetNBT(final NBTTagCompound compound) {
    }
    
    public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        this.writeCustomNBT(compound);
        return compound;
    }
    
    public void writeCustomNBT(final NBTTagCompound compound) {
    }
    
    public void writeNetNBT(final NBTTagCompound compound) {
    }
    
    public final SPacketUpdateTileEntity getUpdatePacket() {
        final NBTTagCompound compound = new NBTTagCompound();
        super.writeToNBT(compound);
        this.writeCustomNBT(compound);
        this.writeNetNBT(compound);
        return new SPacketUpdateTileEntity(this.getPos(), 255, compound);
    }
    
    public final NBTTagCompound getUpdateTag() {
        final NBTTagCompound compound = new NBTTagCompound();
        super.writeToNBT(compound);
        this.writeCustomNBT(compound);
        return compound;
    }
    
    public final void onDataPacket(final NetworkManager manager, final SPacketUpdateTileEntity packet) {
        super.onDataPacket(manager, packet);
        this.readCustomNBT(packet.getNbtCompound());
        this.readNetNBT(packet.getNbtCompound());
    }
    
    public final void markForUpdate() {
        if (this.world != null) {
            final IBlockState thisState = this.world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, thisState, thisState, 3);
        }
        this.markDirty();
    }
}
