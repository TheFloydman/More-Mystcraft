package thefloydman.moremystcraft.util;

import com.xcompwiz.mystcraft.item.ItemAgebook;
import com.xcompwiz.mystcraft.world.ChunkProviderMyst;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

public abstract class BookSpawner {
	
	public static ItemStack generateDescriptiveBook() {
		
		ItemStack itemStackBook = new ItemStack(Item.getByNameOrId("mystcraft:agebook"));

		NBTTagCompound compoundDesBook = new NBTTagCompound();
		compoundDesBook.setFloat("MaxHealth", 10);
		compoundDesBook.setString("DisplayName", "???");

		NBTTagCompound compoundDesBookAuthorsSub = new NBTTagCompound();
		compoundDesBookAuthorsSub.setString("", "Unknown Author");
		NBTTagList compoundDesBookAuthorsMain = new NBTTagList();
		compoundDesBookAuthorsMain.appendTag(compoundDesBookAuthorsSub);

		// Add link panel.
		NBTTagCompound compoundDesBookPagesSub0 = new NBTTagCompound();
		compoundDesBookPagesSub0.setString("id", "mystcraft:page");
		compoundDesBookPagesSub0.setInteger("Count", 1);
		NBTTagCompound compoundDesBookPagesSub0Sub0Sub0 = new NBTTagCompound();
		NBTTagCompound compoundDesBookPagesSub0Sub0 = new NBTTagCompound();
		compoundDesBookPagesSub0Sub0.setTag("linkpanel", compoundDesBookPagesSub0Sub0Sub0);
		compoundDesBookPagesSub0.setTag("tag", compoundDesBookPagesSub0Sub0);
		NBTTagList compoundDesBookPagesMain = new NBTTagList();
		compoundDesBookPagesMain.appendTag(compoundDesBookPagesSub0);

		// Add Star Fissure page.
		NBTTagCompound compoundDesBookPagesFissure = new NBTTagCompound();
		compoundDesBookPagesFissure.setString("id", "mystcraft:page");
		compoundDesBookPagesFissure.setInteger("Count", 1);
		NBTTagCompound compoundDesBookPagesFissureSub = new NBTTagCompound();
		compoundDesBookPagesFissureSub.setString("symbol", "mystcraft:starfissure");
		compoundDesBookPagesFissure.setTag("tag", compoundDesBookPagesFissureSub);
		compoundDesBookPagesMain.appendTag(compoundDesBookPagesFissure);

		// Add blank pages.
		NBTTagCompound compoundDesBookPagesBlank = new NBTTagCompound();
		compoundDesBookPagesBlank.setString("id", "mystcraft:page");
		compoundDesBookPagesBlank.setInteger("Count", 1);
		int rando = (int) (Math.random() * 100);
		if (rando <= 50) {
			for (int i = (int) (Math.random() * 10); i > 0; i--) {
				compoundDesBookPagesMain.appendTag(compoundDesBookPagesBlank);
			}
		} else if (rando >= 51 && rando <= 80) {
			for (int i = (int) ((Math.random() * 20) + 10); i > 0; i--) {
				compoundDesBookPagesMain.appendTag(compoundDesBookPagesBlank);
			}
		} else if (rando >= 81 && rando <= 95) {
			for (int i = (int) ((Math.random() * 40) + 30); i > 0; i--) {
				compoundDesBookPagesMain.appendTag(compoundDesBookPagesBlank);
			}
		} else if (rando >= 96 && rando <= 100) {
			for (int i = (int) ((Math.random() * 30) + 70); i > 0; i--) {
				compoundDesBookPagesMain.appendTag(compoundDesBookPagesBlank);
			}
		}

		compoundDesBook.setTag("Authors", compoundDesBookAuthorsMain);
		compoundDesBook.setTag("Pages", compoundDesBookPagesMain);
		itemStackBook.setTagCompound(compoundDesBook);
		
		return itemStackBook;
	}

}