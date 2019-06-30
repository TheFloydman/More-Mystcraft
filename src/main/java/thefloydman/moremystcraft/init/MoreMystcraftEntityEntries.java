package thefloydman.moremystcraft.init;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import thefloydman.moremystcraft.entity.EntityMaintainerSuit;
import thefloydman.moremystcraft.entity.EntityPotionDummy;
import thefloydman.moremystcraft.util.Reference;

public class MoreMystcraftEntityEntries {

	public static final EntityEntry MAINTAINER_SUIT = EntityEntryBuilder.create().entity(EntityMaintainerSuit.class)
			.id(Reference.forMoreMystcraft("maintainer_suit"), 0).name(Reference.MOD_ID + ".maintainer_suit")
			.tracker(/* render distance */128, /* ticks between updates */1, true).build();
	
	public static final EntityEntry POTION_DUMMY = EntityEntryBuilder.create().entity(EntityPotionDummy.class)
			.id(Reference.forMoreMystcraft("potion_dummy"), 1).name(Reference.MOD_ID + ".potion_dummy")
			.tracker(/* render distance */128, /* ticks between updates */1, true).build();

}
