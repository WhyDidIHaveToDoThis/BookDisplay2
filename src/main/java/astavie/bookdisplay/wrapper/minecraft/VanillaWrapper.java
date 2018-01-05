package astavie.bookdisplay.wrapper.minecraft;

import astavie.bookdisplay.wrapper.BookWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public class VanillaWrapper extends BookWrapper<GuiScreenBook> {

	public VanillaWrapper(ItemStack book) {
		super(new GuiScreenBook(Minecraft.getMinecraft().player, book, book.getItem() == Items.WRITABLE_BOOK));
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		GlStateManager.translate(0, (height - 192) / 2 - 2, 0);
		super.draw(side, partialTicks);
	}

	@Override
	public void left() {
		if (book.currPage > 0)
			book.currPage--;
	}

	@Override
	public void right() {
		if (book.currPage < book.bookTotalPages - 1)
			book.currPage++;
	}

}
