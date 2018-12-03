package astavie.bookdisplay.wrapper.tis3d;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import li.cil.tis3d.client.gui.GuiBookCode;
import li.cil.tis3d.client.gui.GuiManual;
import li.cil.tis3d.common.init.Items;
import net.minecraft.client.gui.GuiScreen;

public abstract class TIS3DWrapper<T extends GuiScreen> extends BookWrapper<T> {

	public TIS3DWrapper(T book, boolean init) {
		super(book, init);
	}

	public static void register() {
		BookDisplay.register(GuiBookCode.class, Items::isBookCode, CodeBookWrapper::new);
		BookDisplay.register(GuiManual.class, Items::isBookManual, item -> new ManualWrapper());
	}

}
