package thefloydman.moremystcraft.tileentity;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import thefloydman.moremystcraft.util.JourneyUtils;

public class TileEntityJourney extends TileEntitySingleItem implements ITickable {

	protected JourneyUtils.BlockType type;
	protected int timer = 120;

	public TileEntityJourney(JourneyUtils.BlockType blockType) {
		this.type = blockType;
		this.markDirty();
	}

	public void timerUp() {
		this.timerUp(1);
	}

	public void timerUp(int time) {
		this.timer += time;
	}

	public int getTimer() {
		return this.timer;
	}

	public void setTimer(int time) {
		this.timer = time;
	}

	@Override
	public void update() {
		if (this.getTimer() < 120) {
			this.timerUp();
		}
	}

	public EnumFacing getFacing() {
		IBlockState state = this.getWorld().getBlockState(this.getPos());
		if (state.getPropertyKeys().contains(BlockHorizontal.FACING)) {
			return state.getValue(BlockHorizontal.FACING);
		}
		return EnumFacing.NORTH;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		this.type = JourneyUtils.stringToBlockType(nbt.getString("type"));
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setString("type", JourneyUtils.blockTypeToString(this.type));
		return super.writeToNBT(nbt);
	}
	
	public JourneyUtils.BlockType getType() {
		return this.type;
	}

}
