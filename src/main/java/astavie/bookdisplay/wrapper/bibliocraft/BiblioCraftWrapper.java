package astavie.bookdisplay.wrapper.bibliocraft;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import astavie.bookdisplay.wrapper.PageWrapper;
import jds.bibliocraft.gui.*;
import jds.bibliocraft.items.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public abstract class BiblioCraftWrapper<T extends GuiScreen> extends BookWrapper<T> {

	public BiblioCraftWrapper(T book) {
		super(book);
	}

	public static void register() {
		BookDisplay.register(GuiBigBook.class, item -> item.getItem() instanceof ItemBigBook, BigBookWrapper::new);
		BookDisplay.register(GuiClipboard.class, item -> item.getItem() instanceof ItemClipboard, ClipboardWrapper::new);
		BookDisplay.register(GuiRedstoneBook.class, item -> item.getItem() instanceof ItemRedstoneBook, item -> new PageWrapper<>(new GuiRedstoneBook(item), true));
		BookDisplay.register(GuiSlottedBook.class, item -> item.getItem() instanceof ItemSlottedBook, item -> new PageWrapper<>(new SlottedBookWrapper(item), true));
		BookDisplay.register(GuiRecipeBook.class, item -> item.getItem() instanceof ItemRecipeBook, item -> new PageWrapper<>(new GuiRecipeBook(item, false, Minecraft.getMinecraft().player.inventory.currentItem, 0, 0, 0, false), true));
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		book.initGui();
		book.buttonList.clear();
	}

}