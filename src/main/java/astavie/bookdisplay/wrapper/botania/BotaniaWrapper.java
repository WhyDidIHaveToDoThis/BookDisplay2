package astavie.bookdisplay.wrapper.botania;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import net.minecraft.client.Minecraft;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconEntry;
import vazkii.botania.common.item.ItemLexicon;

public class BotaniaWrapper extends BookWrapper<GuiLexicon> {

	private BotaniaWrapper() {
		super(GuiLexicon.currentOpenLexicon);
	}

	public static void register() {
		BookDisplay.register(GuiLexicon.class, item -> item.getItem() instanceof ItemLexicon, item -> new BotaniaWrapper());
	}

	@Override
	public void left() {
		if (book instanceof GuiLexiconEntry) {
			GuiLexiconEntry entry = (GuiLexiconEntry) book;
			if (entry.page > 0) {
				entry.getEntry().pages.get(entry.page).onClosed(entry);
				entry.page--;
				entry.getEntry().pages.get(entry.page).onOpened(entry);
			}
		}
	}

	@Override
	public void right() {
		if (book instanceof GuiLexiconEntry) {
			GuiLexiconEntry entry = (GuiLexiconEntry) book;
			if (entry.page < entry.getEntry().pages.size() - 1) {
				entry.getEntry().pages.get(entry.page).onClosed(entry);
				entry.page++;
				entry.getEntry().pages.get(entry.page).onOpened(entry);
			}
		}
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		int scale = Minecraft.getMinecraft().gameSettings.guiScale;
		int persistentScale = Math.min(PersistentVariableHelper.lexiconGuiScale, GuiLexicon.getMaxAllowedScale());
		if (persistentScale > 0 && persistentScale != Minecraft.getMinecraft().gameSettings.guiScale)
			Minecraft.getMinecraft().gameSettings.guiScale = persistentScale;
		book.initGui();
		Minecraft.getMinecraft().gameSettings.guiScale = scale;
	}

}
