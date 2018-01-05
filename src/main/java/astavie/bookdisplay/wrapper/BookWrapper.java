package astavie.bookdisplay.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public abstract class BookWrapper<T extends GuiScreen> implements IBookWrapper {

	protected final T book;

	protected int width;
	protected int height;

	public BookWrapper(T book) {
		this.book = book;
		this.book.mc = Minecraft.getMinecraft();
		this.book.itemRender = Minecraft.getMinecraft().getRenderItem();
		this.book.fontRenderer = Minecraft.getMinecraft().fontRenderer;
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		if (side == EnumHandSide.RIGHT)
			GlStateManager.translate(width, 0, 0);
		book.drawScreen(0, 0, partialTicks);
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width / 2;
		this.height = height;
		book.setGuiSize(this.width, this.height);
	}

}
