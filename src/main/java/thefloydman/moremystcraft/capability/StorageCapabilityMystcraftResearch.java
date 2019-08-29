package thefloydman.moremystcraft.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thefloydman.moremystcraft.research.Knowledge;

public class StorageCapabilityMystcraftResearch implements IStorage<ICapabilityMystcraftResearch> {

	@Override
	public NBTBase writeNBT(Capability<ICapabilityMystcraftResearch> capability, ICapabilityMystcraftResearch instance,
			EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList biomes = new NBTTagList();
		NBTTagList blockstates = new NBTTagList();
		if (instance != null) {
			for (Knowledge knowledge : instance.getPlayerKnowledge()) {
				if (knowledge.asBiome() != null) {
					biomes.appendTag(new NBTTagString(knowledge.asBiome().getRegistryName().toString()));
				} else if (knowledge.asBlockState() != null) {

				}
			}
		}
		nbt.setTag("biomes", biomes);
		nbt.setTag("blocksstates", blockstates);
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICapabilityMystcraftResearch> capability, ICapabilityMystcraftResearch instance,
			EnumFacing side, NBTBase nbt) {
		if (instance != null) {
			if (nbt != null) {
				NBTTagCompound compound = (NBTTagCompound) nbt;
				if (compound.hasKey("biomes")) {
					NBTTagList biomes = compound.getTagList("biomes", 8);
					for (NBTBase base : biomes) {
						ResourceLocation loc = new ResourceLocation(((NBTTagString) base).getString());
						if (ForgeRegistries.BIOMES.getValue(loc) != null) {
							instance.learnKnowledge(new Knowledge(ForgeRegistries.BIOMES.getValue(loc)), null);
						}
					}
				} else if (compound.hasKey("blockstates")) {

				}
			}
		}
	}

}