package thefloydman.moremystcraft.util;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public abstract class BookSpawner {

	public static ItemStack generateBlankDescriptiveBook(final Random rand) {

		ItemStack itemStackBook = new ItemStack(Item.getByNameOrId("mystcraft:agebook"));
		itemStackBook.setItemDamage(0);

		NBTTagCompound compoundDesBook = new NBTTagCompound();
		compoundDesBook.setFloat("MaxHealth", 10);
		compoundDesBook.setFloat("damage", 0);
		compoundDesBook.setString("DisplayName", "Unexplored Age");

		NBTTagString stringAuthor = new NBTTagString("Unknown Author");
		NBTTagList listAuthors = new NBTTagList();
		listAuthors.appendTag(stringAuthor);
		compoundDesBook.setTag("Authors", listAuthors);

		NBTTagList listPages = new NBTTagList();

		// Add link panel.
		NBTTagCompound compoundLinkPanel = generateLinkPanelPage();
		listPages.appendTag(compoundLinkPanel);

		// Add Star Fissure page.
		NBTTagCompound compoundStarFissurePage = generateSymbolPage("mystcraft:starfissure");
		listPages.appendTag(compoundStarFissurePage);

		// Add blank pages.
		NBTTagCompound compoundBlankPage = generateBlankPage();
		int randMain = rand.nextInt(100);
		if (randMain <= 50) {
			int randSub = rand.nextInt(10);
			for (int i = randSub; i > 0; i--) {
				listPages.appendTag(compoundBlankPage);
			}
		} else if (randMain >= 51 && randMain <= 80) {
			int randSub = rand.nextInt(20) + 10;
			for (int i = randSub; i > 0; i--) {
				listPages.appendTag(compoundBlankPage);
			}
		} else if (randMain >= 81 && randMain <= 95) {
			int randSub = rand.nextInt(40) + 30;
			for (int i = randSub; i > 0; i--) {
				listPages.appendTag(compoundBlankPage);
			}
		} else if (randMain >= 96 && randMain <= 100) {
			int randSub = rand.nextInt(30) + 70;
			for (int i = randSub; i > 0; i--) {
				listPages.appendTag(compoundBlankPage);
			}
		}

		compoundDesBook.setTag("Pages", listPages);
		itemStackBook.setTagCompound(compoundDesBook);

		return itemStackBook;
	}

	public static NBTTagCompound generateLinkPanelPage() {
		NBTTagCompound compoundPage = new NBTTagCompound();
		compoundPage.setByte("Count", (byte) 1);
		compoundPage.setShort("Damage", (short) 0);
		compoundPage.setString("id", "mystcraft:page");
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("linkpanel", new NBTTagCompound());
		compoundPage.setTag("tag", tag);
		return compoundPage;
	}

	public static NBTTagCompound generateSymbolPage(String id) {
		NBTTagCompound compoundPage = new NBTTagCompound();
		compoundPage.setByte("Count", (byte) 1);
		compoundPage.setShort("Damage", (short) 0);
		compoundPage.setString("id", "mystcraft:page");
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("symbol", id);
		compoundPage.setTag("tag", tag);
		return compoundPage;
	}

	public static NBTTagCompound generateBlankPage() {
		NBTTagCompound compoundPage = new NBTTagCompound();
		compoundPage.setByte("Count", (byte) 1);
		compoundPage.setShort("Damage", (short) 0);
		compoundPage.setString("id", "mystcraft:page");
		NBTTagCompound tag = new NBTTagCompound();
		compoundPage.setTag("tag", tag);
		return compoundPage;
	}

}