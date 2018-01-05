package astavie.bookdisplay.wrapper.mantle;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.gui.book.GuiBook;

import java.util.HashMap;
import java.util.Map;

public class MantleWrapper extends BookWrapper<GuiMantleBook> {

	private static final Map<Item, BookData> registry = new HashMap<>();

	private MantleWrapper(ItemStack stack) {
		super(new GuiMantleBook(registry.get(stack.getItem()), stack));
	}

	public static void register() {
		BookDisplay.register(GuiBook.class, item -> registry.containsKey(item.getItem()), MantleWrapper::new);
	}

	public static void register(GuiScreen screen) {
		if (screen instanceof GuiBook)
			registry.put(((ItemStack) ReflectionHelper.getPrivateValue(GuiBook.class, (GuiBook) screen, "item")).getItem(), ((GuiBook) screen).book);
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
