package thefloydman.moremystcraft.tileentity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xcompwiz.mystcraft.api.word.WordData;
import com.xcompwiz.mystcraft.data.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import thefloydman.moremystcraft.MoreMystcraft;
import thefloydman.moremystcraft.inventory.ContainerBannerInscriber.Slots;
import thefloydman.moremystcraft.util.Reference;

public class TileEntityBannerInscriber extends TileEntity implements ISidedInventory {

	public NonNullList<ItemStack> itemStacks;
	private String customName;

	public static List<String> mystcraftWords = Arrays.asList(WordData.Balance, WordData.Believe, WordData.Celestial,
			WordData.Chain, WordData.Change, WordData.Chaos, WordData.Civilization, WordData.Constraint,
			WordData.Contradict, WordData.Control, WordData.Convey, WordData.Creativity, WordData.Cycle,
			WordData.Dependence, WordData.Discover, WordData.Dynamic, WordData.Elevate, WordData.Encourage,
			WordData.Energy, WordData.Entropy, WordData.Ethereal, WordData.Exist, WordData.Explore, WordData.Flow,
			WordData.Force, WordData.Form, WordData.Future, WordData.Growth, WordData.Harmony, WordData.Honor,
			WordData.Image, WordData.Infinite, WordData.Inhibit, WordData.Intelligence, WordData.Love, WordData.Machine,
			WordData.Merge, WordData.Momentum, WordData.Motion, WordData.Mutual, WordData.Nature, WordData.Nurture,
			WordData.Order, WordData.Possibility, WordData.Power, WordData.Question, WordData.Rebirth,
			WordData.Remember, WordData.Resilience, WordData.Resurrect, WordData.Sacrifice, WordData.Society,
			WordData.Spur, WordData.Static, WordData.Stimulate, WordData.Survival, WordData.Sustain, WordData.System,
			WordData.Terrain, WordData.Time, WordData.Tradition, WordData.Transform, WordData.Void, WordData.Weave,
			WordData.Wisdom);

	public static List<EnumDyeColor> inscriptionColors = Arrays
			.asList(new EnumDyeColor[] { EnumDyeColor.BLACK, EnumDyeColor.RED, EnumDyeColor.GREEN, EnumDyeColor.BROWN,
					EnumDyeColor.BLUE, EnumDyeColor.PURPLE, EnumDyeColor.CYAN, EnumDyeColor.SILVER, EnumDyeColor.GRAY,
					EnumDyeColor.PINK, EnumDyeColor.LIME, EnumDyeColor.YELLOW, EnumDyeColor.LIGHT_BLUE,
					EnumDyeColor.MAGENTA, EnumDyeColor.ORANGE, EnumDyeColor.WHITE });

	public TileEntityBannerInscriber() {
		itemStacks = NonNullList.<ItemStack>withSize(Slots.values().length, ItemStack.EMPTY);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.itemStacks = NonNullList.<ItemStack>withSize(Slots.values().length, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, this.itemStacks);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		ItemStackHelper.saveAllItems(nbt, this.itemStacks);
		return nbt;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		switch (index) {
		case 1:
			if (stack.getItem().equals(ModItems.inkvial)) {
				return true;
			}
			break;
		case 3:
			if (stack.getItem().equals(ModItems.page)) {
				if (!MoreMystcraft.proxy.pageApi.hasLinkPanel(stack)) {
					return true;
				}
			}
			break;
		case 4:
			if (stack.getItem().equals(Items.BANNER)) {
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public int getSizeInventory() {
		return this.itemStacks.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.itemStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.itemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.itemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.itemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.itemStacks.get(index);
		this.itemStacks.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.itemStacks.clear();
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container." + Reference.MOD_ID + ".banner_inscriber";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side.equals(EnumFacing.DOWN)) {
			return new int[] { 2 };
		} else if (side.equals(EnumFacing.UP)) {
			return new int[] { 0 };
		}
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction.equals(EnumFacing.DOWN) && index == 2) {
			Item item = stack.getItem();

			if (item != Items.GLASS_BOTTLE) {
				return false;
			}
		}

		return true;
	}

	public void updateSlots() {
		if (this.hasDye() && this.hasInk() && this.hasPage() && this.hasBanner() && this.hasRoomForBottles()) {
			ItemStack pageStack = this.getStackInSlot(Slots.PAGE.ordinal());
			EnumDyeColor bannerColor = ItemBanner.getBaseColor(this.getStackInSlot(Slots.BANNER.ordinal()));
			int symbolColor = inscriptionColors
					.indexOf(EnumDyeColor.byDyeDamage(this.getStackInSlot(Slots.DYE.ordinal()).getMetadata()));
			if (symbolColor == -1) {
				symbolColor = 0;
			}

			// Gets existing patterns from input banner.
			ItemStack bannerStack = this.getStackInSlot(Slots.BANNER.ordinal());
			NBTTagCompound nbttagcompound1 = bannerStack.getOrCreateSubCompound("BlockEntityTag");
			NBTTagList nbttaglist;
			if (nbttagcompound1.hasKey("Patterns", 9)) {
				nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
			} else {
				nbttaglist = new NBTTagList();
				nbttagcompound1.setTag("Patterns", nbttaglist);
			}
			Map<String, Integer> existingPatterns = new HashMap<String, Integer>();
			for (int i = 0; i < nbttaglist.tagCount(); i++) {
				NBTTagCompound compound = nbttaglist.getCompoundTagAt(i);
				existingPatterns.put(compound.getString("Pattern"), compound.getInteger("Color"));
			}

			// Displays possible banners based on input page.
			ResourceLocation symbol = MoreMystcraft.proxy.pageApi.getPageSymbol(pageStack);
			if (symbol != null) {
				String[] words = MoreMystcraft.proxy.symbolApi.getSymbol(symbol).getPoem();
				if (words != null) {
					int index = Slots.OUTPUT_0.ordinal();
					for (String str : words) {
						for (String word : mystcraftWords) {
							if (str.toLowerCase().equals(word.toLowerCase())) {
								NBTTagList list = new NBTTagList();
								existingPatterns.forEach((s, i) -> {
									NBTTagCompound existingDetails = new NBTTagCompound();
									existingDetails.setInteger("Color", i);
									existingDetails.setString("Pattern", s);
									list.appendTag(existingDetails);
								});
								NBTTagCompound newDetails = new NBTTagCompound();
								newDetails.setInteger("Color", symbolColor);
								newDetails.setString("Pattern", "mystcraft." + str.toLowerCase());
								list.appendTag(newDetails);
								ItemStack newStack = ItemBanner.makeBanner(bannerColor, list);
								this.setInventorySlotContents(index++, newStack);
								break;
							}
						}
					}
				}
			}
		} else {
			this.setInventorySlotContents(Slots.OUTPUT_0.ordinal(), ItemStack.EMPTY);
			this.setInventorySlotContents(Slots.OUTPUT_1.ordinal(), ItemStack.EMPTY);
			this.setInventorySlotContents(Slots.OUTPUT_2.ordinal(), ItemStack.EMPTY);
			this.setInventorySlotContents(Slots.OUTPUT_3.ordinal(), ItemStack.EMPTY);
		}
	}

	public boolean hasDye() {
		return hasOreDictItem(Slots.DYE.ordinal(), "dye");
	}

	public boolean hasInk() {
		return hasItem(Slots.INK.ordinal(), ModItems.inkvial);
	}

	public boolean hasPage() {
		return hasItem(Slots.PAGE.ordinal(), ModItems.page);
	}

	public boolean hasBanner() {
		return hasItem(Slots.BANNER.ordinal(), Items.BANNER);
	}

	public boolean hasRoomForBottles() {
		return this.getStackInSlot(Slots.BOTTLE.ordinal()).getCount() < 64;
	}

	public boolean hasItem(int slot, Item item) {
		ItemStack stack = this.getStackInSlot(slot);
		if (stack != null) {
			if (stack.getItem().equals(item)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasOreDictItem(int slot, String oreDictName) {
		ItemStack stack = this.getStackInSlot(slot);
		if (stack != null) {
			if (stack.getCount() > 0) {
				int[] ids = OreDictionary.getOreIDs(stack);
				for (int id : ids) {
					if (OreDictionary.getOreID(oreDictName) == id) {
						return true;
					}
				}
			}
		}
		return false;
	}
}