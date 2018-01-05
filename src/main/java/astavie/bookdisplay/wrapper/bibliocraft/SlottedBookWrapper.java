package astavie.bookdisplay.wrapper.bibliocraft;

import jds.bibliocraft.gui.GuiSlottedBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Iterator;

public class SlottedBookWrapper extends GuiSlottedBook {

	public SlottedBookWrapper(ItemStack item) {
		super(Minecraft.getMinecraft().player.inventory, item, true, 0, 0, 0);
		ReflectionHelper.setPrivateValue(GuiSlottedBook.class, this, item, "book");
		getNBTTitleAndLines();

		InventoryPlayer inventory = Minecraft.getMinecraft().player.inventory;
		Iterator<Slot> iterator = inventorySlots.inventorySlots.iterator();
		while (iterator.hasNext())
			if (iterator.next().inventory == inventory)
				iterator.remove();
	}

	@Override
	public void drawTexturedModalRect(int x, int y, int xTex, int yTex, int width, int height) {
		GlStateManager.translate(0, 65, 0);
		super.drawTexturedModalRect(x, y, xTex, yTex, width, 126);
	}

}
