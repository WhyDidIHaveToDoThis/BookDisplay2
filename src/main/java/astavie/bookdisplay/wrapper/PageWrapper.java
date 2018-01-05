package astavie.bookdisplay.wrapper;

import net.minecraft.client.gui.GuiScreen;

public class PageWrapper<T extends GuiScreen> extends BookWrapper<T> {

	private final boolean init;

	public PageWrapper(T book, boolean init) {
		super(book);
		this.init = init;
	}

	@Override
	public void left() {
	}

	@Override
	public void right() {
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		if (init) {
			book.initGui();
			book.buttonList.clear();
		}
	}

}
