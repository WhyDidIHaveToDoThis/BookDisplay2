package astavie.bookdisplay.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BookWrapper<T extends GuiScreen> implements IBookWrapper {

	private static final Method keyTyped = ReflectionHelper.findMethod(GuiScreen.class, "keyTyped", "func_73869_a", char.class, int.class);

	protected final T book;
	private final boolean init;

	protected int width;
	protected int height;

	public BookWrapper(T book, boolean init) {
		this.book = book;
		this.book.mc = Minecraft.getMinecraft();
		this.book.itemRender = Minecraft.getMinecraft().getRenderItem();
		this.book.fontRenderer = Minecraft.getMinecraft().fontRenderer;
		this.init = init;
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		if (side == EnumHandSide.RIGHT)
			GlStateManager.translate(width, 0, 0);
		book.drawScreen(0, 0, partialTicks);
	}

	@Override
	public void left() {
	}

	@Override
	public void right() {
	}

	@Override
	public void close() {
		try {
			keyTyped.invoke(book, '\033', 1);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		book.onGuiClosed();
	}

	@Override
	public void setSize(int width, int height, EnumHandSide side) {
		this.width = width / 2;
		this.height = height;
		book.setGuiSize(this.width, this.height);
		if (init) {
			book.initGui();
			for (GuiButton button : book.buttonList)
				button.visible = false;
		}
	}

}
