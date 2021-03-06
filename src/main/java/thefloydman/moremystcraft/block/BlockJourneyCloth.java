package thefloydman.moremystcraft.block;

import java.util.UUID;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thefloydman.moremystcraft.capability.journeyclothscollected.CapabilityJourneyClothsCollected;
import thefloydman.moremystcraft.capability.journeyclothscollected.ProviderCapabilityJourneyClothsCollected;
import thefloydman.moremystcraft.capability.journeyhub.ICapabilityHub;
import thefloydman.moremystcraft.capability.journeyhub.ProviderCapabilityHub;
import thefloydman.moremystcraft.capability.uuid.ICapabilityUUID;
import thefloydman.moremystcraft.capability.uuid.ProviderCapabilityUUID;
import thefloydman.moremystcraft.data.worldsaveddata.MoreMystcraftSavedDataPerSave;
import thefloydman.moremystcraft.item.ItemJourneyHub;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.tileentity.TileEntityJourney;
import thefloydman.moremystcraft.tileentity.TileEntitySingleItem;
import thefloydman.moremystcraft.util.JourneyUtils;
import thefloydman.moremystcraft.util.Reference;

public class BlockJourneyCloth extends BlockJourneyBase {

	public static final PropertyBool ANIMATED = PropertyBool.create("animated");

	public BlockJourneyCloth(JourneyUtils.PatternType type) {

		super(type, Material.CLOTH);
		this.setUnlocalizedName(Reference.MOD_ID + ".journey_cloth_" + type.name().toLowerCase());
		this.setRegistryName(Reference.forMoreMystcraft("journey_cloth_" + type.name().toLowerCase()));
		this.setHardness(0.5f);
		this.setSoundType(SoundType.CLOTH);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ANIMATED,
				Boolean.valueOf(false)));

	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, ANIMATED });
	}

	@Override
	public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state,
			final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY,
			final float hitZ) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntitySingleItem) {
				ICapabilityUUID capStack = ((TileEntitySingleItem) tileEntity).getItem()
						.getCapability(ProviderCapabilityUUID.UUID, facing);
				if (capStack.getUUID() == null) {
					capStack.setUUID(UUID.randomUUID());
				}
				UUID uuid = capStack.getUUID();
				if (uuid != null) {
					if (player.getHeldItem(hand).getItem() instanceof ItemJourneyHub) {
						ICapabilityHub capHub = player.getHeldItem(hand).getCapability(ProviderCapabilityHub.HUB,
								facing);
						if (!player.isSneaking()) {
							if (capHub.getUUIDs().size() < 15) {
								capHub.addUUID(uuid);
								MoreMystcraftPacketHandler.sendTranslatedMessage((EntityPlayerMP) player,
										Reference.Message.CLOTH_ADDED_TO_HUB.key, Reference.MessageType.STATUS.ordinal(), "none");
							} else {
								MoreMystcraftPacketHandler.sendTranslatedMessage((EntityPlayerMP) player,
										Reference.Message.HUB_FULL.key, Reference.MessageType.STATUS.ordinal(), "none");
							}
						} else {
							capHub.removeCloth(uuid);
							MoreMystcraftPacketHandler.sendTranslatedMessage((EntityPlayerMP) player,
									Reference.Message.CLOTH_REMOVED_FROM_HUB.key, Reference.MessageType.STATUS.ordinal(), "none");
						}
					} else {
						CapabilityJourneyClothsCollected capPlayer = player
								.getCapability(ProviderCapabilityJourneyClothsCollected.JOURNEY_CLOTH, facing);
						MoreMystcraftSavedDataPerSave data = MoreMystcraftSavedDataPerSave.get(world);
						if (player.isSneaking()) {
							capPlayer.removeCloth(uuid);
							data.deactivateJourneyCloth(uuid, player.getUniqueID());
							MoreMystcraftPacketHandler.sendTranslatedMessage((EntityPlayerMP) player,
									Reference.Message.CLOTH_DEACTIVATED.key, Reference.MessageType.STATUS.ordinal(), "none");
						} else {
							if (!capPlayer.getActivatedCloths().contains(uuid)) {
								capPlayer.addCloth(uuid);
								data.activateJourneyCloth(uuid, player.getUniqueID());
								MoreMystcraftPacketHandler.sendTranslatedMessage((EntityPlayerMP) player,
										Reference.Message.CLOTH_ACTIVATED.key, Reference.MessageType.STATUS.ordinal(), "none");
								MoreMystcraftPacketHandler.renderJourneyActivation((EntityPlayerMP) player, pos);
							}
						}
					}
				}
			}
		}

		return true;

	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntitySingleItem) {
				ItemStack newStack = stack.copy();
				newStack.setCount(1);
				TileEntitySingleItem clothEntity = (TileEntitySingleItem) tileEntity;
				clothEntity.setItem(newStack);
				ICapabilityUUID cap = clothEntity.getItem().getCapability(ProviderCapabilityUUID.UUID, null);
				if (cap.getUUID() == null) {
					cap.setUUID(UUID.randomUUID());
				}
				MoreMystcraftSavedDataPerSave data = MoreMystcraftSavedDataPerSave.get(world);
				data.addJourneyCloth(cap.getUUID());
				data.setClothPos(cap.getUUID(), world.provider.getDimension(), pos);
			}
		}
		super.onBlockPlacedBy(world, pos, state, placer, stack);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileEntitySingleItem) {
				TileEntitySingleItem clothEntity = (TileEntitySingleItem) te;
				ICapabilityUUID cap = clothEntity.getItem().getCapability(ProviderCapabilityUUID.UUID, null);
				MoreMystcraftSavedDataPerSave data = MoreMystcraftSavedDataPerSave.get(world);
				data.removeClothPos(cap.getUUID());
			}
			if (te instanceof IInventory) {
				InventoryHelper.dropInventoryItems(world, pos, (IInventory) te);
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityJourney(JourneyUtils.BlockType.CLOTH);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

}