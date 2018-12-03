package astavie.bookdisplay.wrapper.bibliocraft;

import jds.bibliocraft.gui.GuiClipboard;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ClipboardWrapper extends BiblioCraftWrapper<GuiClipboard> {

	private static final Method PREV = ReflectionHelper.findMethod(GuiClipboard.class, "prevPage", null);
	private static final Method NEXT = ReflectionHelper.findMethod(GuiClipboard.class, "nextPage", null);

	private final ItemStack item;
	private final ItemStack copy;

	ClipboardWrapper(ItemStack item) {
		super(new GuiClipboard(item.copy(), true, 0, 0, 0));
		this.item = item;
		this.copy = ReflectionHelper.getPrivateValue(GuiClipboard.class, book, "clipStack");
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		GlStateManager.translate(0, (height - 192) / 2 - 2, 0);
		super.draw(side, partialTicks);
	}

	@Override
	public void left() {
		NBTTagCompound tag = Objects.requireNonNull(copy.getTagCompound());
		if (tag.getInteger("currentPage") > 1) {
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
		NBTTagCompound tag = Objects.requireNonNull(copy.getTagCompound());
		if (tag.getInteger("currentPage") < tag.getInteger("totalPages") - 1) {
			try {
				NEXT.invoke(book);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			book.buttonList.clear();
		}
	}

	@Override
	public void close() {
		super.close();
		item.setTagCompound(copy.getTagCompound());
	}

}
