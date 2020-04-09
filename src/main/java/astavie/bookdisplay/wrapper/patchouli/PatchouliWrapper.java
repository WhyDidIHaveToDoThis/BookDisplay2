package astavie.bookdisplay.wrapper.patchouli;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.util.HandSide;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.patchouli.api.BookDrawScreenEvent;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.item.ItemModBook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PatchouliWrapper extends BookWrapper<GuiBook> {

	private static final Method resetTooltip = ObfuscationReflectionHelper.findMethod(GuiBook.class, "resetTooltip");
	private static final Method drawBackgroundElements = ObfuscationReflectionHelper.findMethod(GuiBook.class, "drawBackgroundElements", int.class, int.class, float.class);
	private static final Method drawForegroundElements = ObfuscationReflectionHelper.findMethod(GuiBook.class, "drawForegroundElements", int.class, int.class, float.class);
	private static final Method drawTooltip = ObfuscationReflectionHelper.findMethod(GuiBook.class, "drawTooltip", int.class, int.class);
	private static final Method changePage = ObfuscationReflectionHelper.findMethod(GuiBook.class, "changePage", boolean.class, boolean.class);

	public PatchouliWrapper(GuiBook book) {
		super(book, false);
	}

	public static void register() {
		BookDisplay.register(GuiBook.class, item -> item.getItem() instanceof ItemModBook, item -> {
			Book book = ItemModBook.getBook(item);
			if (book != null) {
				if (!book.contents.getCurrentGui().canBeOpened()) {
					book.contents.currentGui = null;
					book.contents.guiStack.clear();
				}

				return new PatchouliWrapper(book.contents.getCurrentGui());
			}
			return null;
		});
	}

	@Override
	public void draw(HandSide side, float partialTicks) {
		if (side == HandSide.RIGHT)
			GlStateManager.translatef(width, 0, 0);

		Minecraft mc = Minecraft.getInstance();
		int mouseX = 0;
		int mouseY = 0;

		// Copy code so we can remove the grey background
		MainWindow res = mc.mainWindow;
		double oldGuiScale = (double)res.calcGuiScale(mc.gameSettings.guiScale, mc.getForceUnicodeFont());
		int maxScale = mc.mainWindow.calcGuiScale(0, mc.getForceUnicodeFont());
		int persistentScale = Math.min(PersistentData.data.bookGuiScale, maxScale);
		double newGuiScale = (double)res.calcGuiScale(persistentScale, mc.getForceUnicodeFont());

		float scaleFactor;
		if (persistentScale > 0 && newGuiScale != oldGuiScale) {
			scaleFactor = (float)newGuiScale / (float)res.getGuiScaleFactor();
			res.setGuiScale(newGuiScale);
			this.width = res.getScaledWidth();
			this.height = res.getScaledHeight();
			res.setGuiScale(oldGuiScale);
		} else {
			scaleFactor = 1.0F;
		}

		GlStateManager.pushMatrix();
		if (scaleFactor != 1.0F) {
			GlStateManager.scalef(scaleFactor, scaleFactor, scaleFactor);
			mouseX = (int)((float)mouseX / scaleFactor);
			mouseY = (int)((float)mouseY / scaleFactor);
		}

		try {
			drawScreenAfterScale(mouseX, mouseY, partialTicks);
		} catch (InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
		GlStateManager.popMatrix();
	}

	private void drawScreenAfterScale(int mouseX, int mouseY, float partialTicks) throws InvocationTargetException, IllegalAccessException {
		resetTooltip.invoke(book);
		// book.drawBackground();
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) book.bookLeft, (float) book.bookTop, 0.0F);
		GlStateManager.color3f(1.0F, 1.0F, 1.0F);
		drawBackgroundElements.invoke(book, mouseX, mouseY, partialTicks);
		drawForegroundElements.invoke(book, mouseX, mouseY, partialTicks);
		GlStateManager.popMatrix();

		for(int i = 0; i < book.buttons.size(); ++i) {
			book.buttons.get(i).render(mouseX, mouseY, partialTicks);
		}

		MinecraftForge.EVENT_BUS.post(new BookDrawScreenEvent(book, book.book.resourceLoc, mouseX, mouseY, partialTicks));
		drawTooltip.invoke(book, mouseX, mouseY);
	}

	@Override
	public void left() {
		try {
			changePage.invoke(book, true, true);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void right() {
		try {
			changePage.invoke(book, false, true);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setSize(int width, int height, HandSide side) {
		super.setSize(width, height, side);
		int scale = Minecraft.getInstance().gameSettings.guiScale;
		int persistentScale = Math.min(PersistentData.data.bookGuiScale, book.maxScale);
		if (persistentScale > 0 && persistentScale != Minecraft.getInstance().gameSettings.guiScale)
			Minecraft.getInstance().gameSettings.guiScale = persistentScale;
		book.init(Minecraft.getInstance(), this.width, this.height);
		Minecraft.getInstance().gameSettings.guiScale = scale;
	}

}
