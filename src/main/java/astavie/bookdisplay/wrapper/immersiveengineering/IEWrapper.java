package astavie.bookdisplay.wrapper.immersiveengineering;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumHandSide;

public class IEWrapper extends BookWrapper<GuiManual> {

	private IEWrapper() {
		super(ManualHelper.getManual().getGui(), false);
	}

	public static void register() {
		BookDisplay.register(GuiManual.class, item -> item.getItem() instanceof IEItemInterfaces.IGuiItem && ((IEItemInterfaces.IGuiItem) item.getItem()).getGuiID(item) == Lib.GUIID_Manual, item -> new IEWrapper());
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
		ManualInstance.ManualEntry entry = book.getManual().getEntry(book.getSelectedEntry());
		if (entry != null && book.page < entry.getPages().length - 1) {
			book.page++;
			initGui();
		}
	}

	@Override
	public void setSize(int width, int height, EnumHandSide side) {
		super.setSize(width, height, side);
		initGui();
	}

	private void initGui() {
		int scale = Minecraft.getMinecraft().gameSettings.guiScale;
		if (Minecraft.getMinecraft().gameSettings.guiScale == 1)
			Minecraft.getMinecraft().gameSettings.guiScale = 2;
		book.initGui();
		Minecraft.getMinecraft().gameSettings.guiScale = scale;
		for (GuiButton button : book.buttonList)
			button.visible = false;
	}

}
