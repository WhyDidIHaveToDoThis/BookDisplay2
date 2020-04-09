package astavie.bookdisplay.wrapper;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.HandSide;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

public class BookWrapper<T extends Screen> implements IBookWrapper {

	public static final Method initGui = ObfuscationReflectionHelper.findMethod(Screen.class, "init");

	protected final T book;
	private final boolean init;

	protected int width;
	protected int height;

	public BookWrapper(T book, boolean init) {
		this.book = book;
		this.book.minecraft = Minecraft.getInstance();
		this.book.itemRenderer = Minecraft.getInstance().getItemRenderer();
		this.book.font = Minecraft.getInstance().fontRenderer;
		this.init = init;
	}

	@Override
	public void draw(HandSide side, float partialTicks) {
		if (side == HandSide.RIGHT)
			GlStateManager.translatef(width, 0, 0);
		book.render(0, 0, partialTicks);
	}

	@Override
	public void left() {
	}

	@Override
	public void right() {
	}

	@Override
	public void close() {
		book.charTyped('\033', 1);
		book.onClose();
	}

	@Override
	public void setSize(int width, int height, HandSide side) {
		this.width = width / 2;
		this.height = height;
		book.setSize(this.width, this.height);
		if (init) {
			book.init(Minecraft.getInstance(), this.width, this.height);
			for (Widget widget : book.buttons)
				widget.visible = false;
		}
	}

}
