package astavie.bookdisplay.wrapper.immersiveengineering;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.common.items.ManualItem;
import blusunrize.lib.manual.ManualEntry;
import blusunrize.lib.manual.gui.ManualScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.HandSide;

public class IEWrapper extends BookWrapper<ManualScreen> {

	private IEWrapper() {
		super(ManualHelper.getManual().getGui(), false);
	}

	public static void register() {
		BookDisplay.register(ManualScreen.class, item -> item.getItem() instanceof ManualItem, item -> new IEWrapper());
	}

	@Override
	public void left() {
		if (book.page > 0) {
			book.page--;
			initGui();
		}
	}

	@Override
	public void right() {
		ManualEntry entry =  book.getCurrentPage();
		if (entry != null && book.page < entry.getPageCount() - 1) {
			book.page++;
			initGui();
		}
	}

	@Override
	public void setSize(int width, int height, HandSide side) {
		super.setSize(width, height, side);
		initGui();
	}

	private void initGui() {
		int scale = Minecraft.getInstance().gameSettings.guiScale;
		if (Minecraft.getInstance().gameSettings.guiScale == 1)
			Minecraft.getInstance().gameSettings.guiScale = 2;
		book.fullInit();
		Minecraft.getInstance().gameSettings.guiScale = scale;
	}

}
