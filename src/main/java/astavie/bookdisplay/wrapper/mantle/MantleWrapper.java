package astavie.bookdisplay.wrapper.mantle;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.gui.book.GuiBook;

import java.util.HashMap;
import java.util.Map;

public class MantleWrapper extends BookWrapper<GuiMantleBook> {

	private static final Map<ItemStack, BookData> registry = new HashMap<>();

	private MantleWrapper(ItemStack stack) {
		super(new GuiMantleBook(get(stack), stack), false);
	}

	private static BookData get(ItemStack stack) {
		for (Map.Entry<ItemStack, BookData> entry : registry.entrySet())
			if (entry.getKey().getItem() == stack.getItem() && entry.getKey().getMetadata() == stack.getMetadata())
				return entry.getValue();
		return null;
	}

	public static void register() {
		BookDisplay.register(GuiBook.class, item -> get(item) != null, MantleWrapper::new);
	}

	public static void register(GuiScreen screen) {
		if (screen instanceof GuiBook)
			registry.put(ReflectionHelper.getPrivateValue(GuiBook.class, (GuiBook) screen, "item"), ((GuiBook) screen).book);
	}

	@Override
	public void left() {
		book.left();
	}

	@Override
	public void right() {
		book.right();
	}

}
