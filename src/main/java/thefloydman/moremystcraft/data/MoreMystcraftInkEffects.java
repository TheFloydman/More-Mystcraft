package thefloydman.moremystcraft.data;

import com.xcompwiz.mystcraft.api.util.Color;
import com.xcompwiz.mystcraft.data.InkEffects;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thefloydman.moremystcraft.config.MoreMystcraftConfig;

public class MoreMystcraftInkEffects {

	public enum Effect {
		ADVENTURE_MODE("Adventure", Items.COOKIE, 0.25F, 153.0F, 110.0F, 64.0F);

		private String title;
		private ItemStack ingredient;
		private float probability;
		private Color color;

		Effect(String str, Item item, float chance, float red, float green, float blue) {
			this.title = str;
			this.ingredient = new ItemStack(item);
			this.probability = chance;
			this.color = new Color(red / 255.0F, green / 255.0F, blue / 255.0F);
		}

		public String getTitle() {
			return this.title;
		}

		public Color getColor() {
			return this.color;
		}

		public ItemStack getItem() {
			return this.ingredient;
		}

		public float getChance() {
			return this.probability;
		}

	}

	public static void init() {

		for (Effect effect : Effect.values()) {
			if (effect.equals(Effect.ADVENTURE_MODE)) {
				if (!MoreMystcraftConfig.getAdventureLinkPanelEnabled()) {
					continue;
				}
			}
			InkEffects.registerProperty(effect.getTitle(), effect.getColor());
			InkEffects.addPropertyToItem(effect.getItem(), effect.getTitle(), effect.getChance());
		}

	}

}
