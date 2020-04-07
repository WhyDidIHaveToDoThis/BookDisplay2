package astavie.bookdisplay.wrapper.patchouli;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Mouse;
import vazkii.botania.client.core.handler.PersistentVariableHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.patchouli.api.BookDrawScreenEvent;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.item.ItemModBook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PatchouliWrapper extends BookWrapper<GuiBook> {

	private static final Method resetTooltip = ReflectionHelper.findMethod(GuiBook.class, "resetTooltip", null);
	private static final Method drawBackgroundElements = ReflectionHelper.findMethod(GuiBook.class, "drawBackgroundElements", null, int.class, int.class, float.class);
	private static final Method drawForegroundElements = ReflectionHelper.findMethod(GuiBook.class, "drawForegroundElements", null, int.class, int.class, float.class);
	private static final Method drawTooltip = ReflectionHelper.findMethod(GuiBook.class, "drawTooltip", null, int.class, int.class);
	private static final Method changePage = ReflectionHelper.findMethod(GuiBook.class, "changePage", null, boolean.class, boolean.class);

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
	public void draw(EnumHandSide side, float partialTicks) {
		if (side == EnumHandSide.RIGHT)
			GlStateManager.translate(width, 0, 0);

		Minecraft mc = Minecraft.getMinecraft();
		int mouseX = 0;
		int mouseY = 0;

		// Copy code so we can remove the grey background
		ScaledResolution res = new ScaledResolution(mc);
		int guiScale = mc.gameSettings.guiScale;
		GlStateManager.pushMatrix();
		int persistentScale = Math.min(PersistentData.data.bookGuiScale, book.maxScale);
		if (persistentScale > 0 && persistentScale != guiScale) {
			mc.gameSettings.guiScale = persistentScale;
			float s = (float) persistentScale / (float) res.getScaleFactor();
			GlStateManager.scale(s, s, s);
			res = new ScaledResolution(mc);
			int sw = res.getScaledWidth();
			int sh = res.getScaledHeight();
			mouseX = Mouse.getX() * sw / mc.displayWidth;
			mouseY = sh - Mouse.getY() * sh / mc.displayHeight - 1;
		}

		try {
			drawScreenAfterScale(mouseX, mouseY, partialTicks);
		} catch (InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}

		mc.gameSettings.guiScale = guiScale;
		GlStateManager.popMatrix();
	}

	private void drawScreenAfterScale(int mouseX, int mouseY, float partialTicks) throws InvocationTargetException, IllegalAccessException {
		resetTooltip.invoke(book);
		// book.drawDefaultBackground();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) book.bookLeft, (float) book.bookTop, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		drawBackgroundElements.invoke(book, mouseX, mouseY, partialTicks);
		drawForegroundElements.invoke(book, mouseX, mouseY, partialTicks);
		GlStateManager.popMatrix();

		Minecraft mc = Minecraft.getMinecraft();

		for (int i = 0; i < book.buttonList.size(); ++i) {
			book.buttonList.get(i).drawButton(mc, mouseX, mouseY, partialTicks);
		}

		for (int j = 0; j < book.labelList.size(); ++j) {
			book.labelList.get(j).drawLabel(mc, mouseX, mouseY);
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
	public void setSize(int width, int height, EnumHandSide side) {
		super.setSize(width, height, side);
		int scale = Minecraft.getMinecraft().gameSettings.guiScale;
		int persistentScale = Math.min(PersistentVariableHelper.lexiconGuiScale, GuiLexicon.getMaxAllowedScale());
		if (persistentScale > 0 && persistentScale != Minecraft.getMinecraft().gameSettings.guiScale)
			Minecraft.getMinecraft().gameSettings.guiScale = persistentScale;
		book.initGui();
		Minecraft.getMinecraft().gameSettings.guiScale = scale;
	}

}
