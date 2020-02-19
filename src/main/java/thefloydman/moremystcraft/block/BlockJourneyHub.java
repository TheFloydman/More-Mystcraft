package thefloydman.moremystcraft.block;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.capability.journeyhub.ICapabilityHub;
import thefloydman.moremystcraft.capability.journeyhub.ProviderCapabilityHub;
import thefloydman.moremystcraft.data.worldsaveddata.MoreMystcraftSavedDataPerSave;
import thefloydman.moremystcraft.gui.MoreMystcraftGUIs;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.tileentity.TileEntityJourney;
import thefloydman.moremystcraft.tileentity.TileEntitySingleItem;
import thefloydman.moremystcraft.util.JourneyUtils;
import thefloydman.moremystcraft.util.Reference;

public class BlockJourneyHub extends BlockJourneyBase {

	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockJourneyHub(JourneyUtils.PatternType type) {

		super(type, Material.IRON);
		this.setUnlocalizedName(Reference.MOD_ID + ".journey_hub_" + type.name().toLowerCase());
		this.setRegistryName(Reference.forMoreMystcraft("journey_hub_" + type.name().toLowerCase()));
		this.setHardness(5.0f);
		this.setSoundType(SoundType.METAL);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED,
				Boolean.valueOf(false)));

	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		int power = 0;
		if (((Boolean) blockState.getValue(POWERED)).booleanValue()) {
			if (side.equals(((EnumFacing) blockState.getValue(FACING)))) {
				TileEntity tileEntity = blockAccess.getTileEntity(pos);
				if (tileEntity instanceof TileEntitySingleItem) {
					ICapabilityHub capStack = ((TileEntitySingleItem) tileEntity).getItem()
							.getCapability(ProviderCapabilityHub.HUB, side);
					MoreMystcraftSavedDataPerSave data = MoreMystcraftSavedDataPerSave.get(tileEntity.getWorld());
					List<UUID> uuids = capStack.getUUIDs();
					for (UUID id : uuids) {
						if (capStack.getPerPlayer()) {
							power += data.journeyClothActivatedByPlayer(id, capStack.getLastActivatedBy()) ? 1 : 0;
						} else {
							power += data.journeyClothActivatedByAnyone(id) ? 1 : 0;
						}
					}
				}
			}
		}
		return MathHelper.clamp(power, 0, 15);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityJourney(JourneyUtils.BlockType.HUB);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, POWERED });
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntitySingleItem) {
				ItemStack newStack = stack.copy();
				newStack.setCount(1);
				ICapabilityHub cap = newStack.getCapability(ProviderCapabilityHub.HUB, null);
				if (cap != null) {
					cap.setOwner(placer.getUniqueID());
				}
				TileEntitySingleItem hubEntity = (TileEntitySingleItem) tileEntity;
				hubEntity.setItem(newStack);
				world.notifyBlockUpdate(pos, state, state, 3);
			}
		}
		this.notifyNeighbors(world, pos, state);
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ) {
		if (!world.isRemote) {
			if (player.isSneaking()) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity instanceof TileEntitySingleItem) {
					ICapabilityHub capStack = ((TileEntitySingleItem) tileEntity).getItem()
							.getCapability(ProviderCapabilityHub.HUB, facing);
					if (capStack != null) {
						if (!capStack.getOwner().equals(player.getUniqueID())) {
							return false;
						}
						capStack.updateClothInfo(world);
					}
					world.notifyBlockUpdate(pos, state, state, 3);
					player.openGui((Object) MoreMystcraft.instance, MoreMystcraftGUIs.JOURNEY_HUB.ordinal(), world,
							pos.getX(), pos.getY(), pos.getZ());
				}
			} else {
				if (!this.isPowered(state)) {
					world.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)));
					this.notifyNeighbors(world, pos, state);
					TileEntity tileEntity = world.getTileEntity(pos);
					if (tileEntity instanceof TileEntitySingleItem) {
						ICapabilityHub capStack = ((TileEntitySingleItem) tileEntity).getItem()
								.getCapability(ProviderCapabilityHub.HUB, facing);
						if (capStack != null) {
							capStack.setLastActivatedBy(player.getUniqueID());
								MoreMystcraftPacketHandler.renderJourneyActivation((EntityPlayerMP) player, pos);
							if (capStack.getTimeLimit() != 0) {
								world.scheduleBlockUpdate(pos, this, capStack.getTimeLimit(), 0);
							}
							world.notifyBlockUpdate(pos, state, state, 3);
						}
					}
				}
			}
		}

		return true;

	}

	public boolean isPowered(IBlockState state) {
		return ((Boolean) state.getValue(POWERED)).booleanValue();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!world.isRemote) {
			if (this.isPowered(state)) {
				world.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
				this.notifyNeighbors(world, pos, state);
				world.markBlockRangeForRenderUpdate(pos, pos);
			}
		}
	}

	@Override
	public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}
	
}