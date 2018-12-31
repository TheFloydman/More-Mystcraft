package thefloydman.moremystcraft.tileentity;

import net.minecraft.item.ItemStack;
import com.xcompwiz.mystcraft.api.item.IItemPortalActivator;
import com.xcompwiz.mystcraft.tileentity.TileEntityBookRotateable;
import thefloydman.moremystcraft.portal.MoreMystcraftPortalUtils;

public class TileEntityUnstableBookReceptacle extends TileEntityBookRotateable {

	@Override
	public void handleItemChange(final int slot) {
		super.handleItemChange(slot);
		if (this.world == null || this.world.isRemote) {
			return;
		}
		MoreMystcraftPortalUtils.shutdownPortal(this.world, this.pos);
		final ItemStack book = this.getBook();
		if (!book.isEmpty() && book.getItem() instanceof IItemPortalActivator) {
			MoreMystcraftPortalUtils.firePortal(this.world, this.pos);
		}
	}
}