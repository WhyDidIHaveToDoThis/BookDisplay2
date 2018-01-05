package astavie.bookdisplay.wrapper.bibliocraft;

import jds.bibliocraft.gui.GuiBigBook;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class BigBookWrapper extends BiblioCraftWrapper<GuiBigBook> {

	public BigBookWrapper(ItemStack stack) {
		super(new GuiBigBook(stack, true, 0, 0, 0, Minecraft.getMinecraft().player.getDisplayNameString()));
		ReflectionHelper.setPrivateValue(GuiBigBook.class, book, true, "signed");
	}

	@Override
	public void left() {
		int page = ReflectionHelper.getPrivateValue(GuiBigBook.class, book, "currentPage");
		if (page > 0) {
			ReflectionHelper.setPrivateValue(GuiBigBook.class, book, page - 1, "currentPage");
			book.loadCurrentPageLinesFromNBT();
			book.initGui();
			book.buttonList.clear();
		}
	}

	@Override
	public void right() {
		int page = ReflectionHelper.getPrivateValue(GuiBigBook.class, book, "currentPage");
		if (page < (int) ReflectionHelper.getPrivateValue(GuiBigBook.class, book, "totalPages") - 1) {
			ReflectionHelper.setPrivateValue(GuiBigBook.class, book, page + 1, "currentPage");
			book.loadCurrentPageLinesFromNBT();
			book.initGui();
			book.buttonList.clear();
		}
	}

}
