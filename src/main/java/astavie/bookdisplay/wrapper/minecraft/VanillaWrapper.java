package astavie.bookdisplay.wrapper.minecraft;

import astavie.bookdisplay.wrapper.BookWrapper;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.HandSide;

public class VanillaWrapper extends BookWrapper<ReadBookScreen> {

	public VanillaWrapper(ItemStack book) {
		super(new ReadBookScreen(book.getItem() == Items.WRITABLE_BOOK ? new ReadBookScreen.UnwrittenBookInfo(book) : new ReadBookScreen.WrittenBookInfo(book)), true);
	}

	@Override
	public void draw(HandSide side, float partialTicks) {
		RenderSystem.translatef(0, (height - 192) / 2 - 2, 0);
		super.draw(side, partialTicks);
	}

	@Override
	public void left() {
		if (book.currPage > 0)
			book.showPage(book.currPage - 1);
	}

	@Override
	public void right() {
		if (book.currPage < book.getPageCount() - 1)
			book.showPage(book.currPage + 1);
	}

	@Override
	protected boolean makeButtonInvisible(Widget button) {
		return !(button instanceof ChangePageButton);
	}

}
