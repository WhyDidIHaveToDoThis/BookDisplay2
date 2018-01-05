package astavie.bookdisplay.wrapper.bibliocraft;

import jds.bibliocraft.gui.GuiClipboard;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClipboardWrapper extends BiblioCraftWrapper<GuiClipboard> {

	private static final Method PREV = ReflectionHelper.findMethod(GuiClipboard.class, "prevPage", null);
	private static final Method NEXT = ReflectionHelper.findMethod(GuiClipboard.class, "nextPage", null);

	public ClipboardWrapper(ItemStack item) {
		super(new GuiClipboard(item, true, 0, 0, 0));
	}

	@Override
	public void left() {
		int page = ReflectionHelper.getPrivateValue(GuiClipboard.class, book, "currentPage");
		if (page > 0) {
			try {
				PREV.invoke(book);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			book.buttonList.clear();
		}
	}

	@Override
	public void right() {
		int page = ReflectionHelper.getPrivateValue(GuiClipboard.class, book, "currentPage");
		if (page < (int) ReflectionHelper.getPrivateValue(GuiClipboard.class, book, "totalPages") - 1) {
			try {
				NEXT.invoke(book);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			book.buttonList.clear();
		}
	}

}
