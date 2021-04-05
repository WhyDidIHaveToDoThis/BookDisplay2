package astavie.bookdisplay.wrapper;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.HandSide;

public class BookWrapper<T extends Screen> implements IBookWrapper {

	private static BookWrapper<?> drawing = null;

	protected final T book;
	private final boolean drawsBackground;

	protected int width;
	protected int height;

	public BookWrapper(T book, boolean drawsBackground) {
		this.book = book;
		this.drawsBackground = drawsBackground;
	}

	public BookWrapper(T book) {
		this(book, false);
	}

	public static void onDrawBackground(MatrixStack stack) {
		if (drawing != null) {
			stack.translate(0, -drawing.height, 0);
		}
	}

	@Override
	public void draw(MatrixStack stack, HandSide side, float partialTicks) {
		stack.translate(width / (side == HandSide.RIGHT ? 4 : -4), 0, 0);

		if (drawsBackground) {
			// We translate down so the grey background gets lost
			drawing = this;

			stack.translate(0, height, 0);
			book.render(stack, 0, 0, partialTicks);

			drawing = null;
		} else {
			// We don't do anything special
			book.render(stack, 0, 0, partialTicks);
		}
	}

	@Override
	public void left() {
	}

	@Override
	public void right() {
	}

	@Override
	public void onOpen() {
	}

	@Override
	public void onTick() {
		book.tick();
	}

	@Override
	public void onClose() {
		book.charTyped('\033', 1);
		book.onClose();
	}

	@Override
	public void setSize(int width, int height, HandSide side) {
		this.width = width;
		this.height = height;

		book.init(Minecraft.getInstance(), width, height);
		for (Widget button : book.buttons)
			if (makeButtonInvisible(button))
				button.visible = false;
	}

	protected boolean makeButtonInvisible(Widget button) {
		return false;
	}

}
