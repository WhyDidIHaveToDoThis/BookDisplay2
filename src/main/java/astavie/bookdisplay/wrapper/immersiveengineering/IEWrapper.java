package astavie.bookdisplay.wrapper.immersiveengineering;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.common.items.ManualItem;
import blusunrize.lib.manual.ManualEntry;
import blusunrize.lib.manual.gui.ManualScreen;

public class IEWrapper extends BookWrapper<ManualScreen> {

	private IEWrapper() {
		super(ManualHelper.getManual().getGui());
	}

	public static void register() {
		BookDisplay.register(item -> item.getItem() instanceof ManualItem, item -> new IEWrapper());
	}

	@Override
	public void left() {
		if (book.page > 0) {
			book.page--;
			book.fullInit();
		}
	}

	@Override
	public void right() {
		ManualEntry entry =  book.getCurrentPage();
		if (entry != null && book.page < entry.getPageCount() - 1) {
			book.page++;
			book.fullInit();
		}
	}

}
