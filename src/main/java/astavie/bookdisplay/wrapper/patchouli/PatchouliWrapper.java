package astavie.bookdisplay.wrapper.patchouli;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.item.ItemModBook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PatchouliWrapper extends BookWrapper<GuiBook> {

	private static final Method changePage = ObfuscationReflectionHelper.findMethod(GuiBook.class, "changePage", boolean.class, boolean.class);

	public PatchouliWrapper(GuiBook book) {
		super(book, true);
	}

	public static void register() {
		BookDisplay.register(item -> item.getItem() instanceof ItemModBook, item -> {
			Book book = ItemModBook.getBook(item);
			if (book != null) {
				if (!book.contents.getCurrentGui().canBeOpened()) {
					book.contents.currentGui = null;
					book.contents.guiStack.clear();
				}

				return new PatchouliWrapper(book.contents.getCurrentGui());
			}
			return null;
		});
	}

	@Override
	public void left() {
		try {
			changePage.invoke(book, true, true);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void right() {
		try {
			changePage.invoke(book, false, true);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
