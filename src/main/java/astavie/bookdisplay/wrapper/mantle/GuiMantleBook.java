package astavie.bookdisplay.wrapper.mantle;

import astavie.bookdisplay.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.client.book.BookHelper;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.gui.book.GuiBook;
import slimeknights.mantle.client.gui.book.element.BookElement;

import java.util.ArrayList;

public class GuiMantleBook extends GuiScreen {

	private static final ResourceLocation TEX_BOOK = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");
	private static final int PAGE_WIDTH_UNSCALED = 214;
	private static final int PAGE_HEIGHT_UNSCALED = 200;

	private static final int PAGE_PADDING_TOP = 4;
	private static final int PAGE_PADDING_LEFT = 8;

	private static final int PAGE_MARGIN = 8;

	private static final int PAGE_WIDTH = PAGE_WIDTH_UNSCALED - (PAGE_PADDING_LEFT + PAGE_MARGIN * 2);
	private static final int PAGE_HEIGHT = PAGE_HEIGHT_UNSCALED - (PAGE_PADDING_TOP * 2 + PAGE_MARGIN * 2);

	private final BookData book;
	private final ArrayList<BookElement> elements = new ArrayList<>();

	private final GuiBook gui;

	private int page;

	public GuiMantleBook(BookData book, ItemStack item) {
		StatisticsManager stats = Minecraft.getMinecraft().player.getStatFileWriter();
		this.book = book;
		this.gui = new GuiBook(book, stats, item);
		this.page = book.findPageNumber(BookHelper.getSavedPage(item), stats);
		if (page < 0)
			page = 0;
		reload();
	}

	@Override
	@SuppressWarnings("ForLoopReplaceableByForEach")
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		FontRenderer fontRenderer = book.fontRenderer;
		if (fontRenderer == null)
			fontRenderer = mc.fontRenderer;

		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();

		GlStateManager.pushMatrix();
		GlStateManager.color(1F, 1F, 1F);

		float coverR = ((book.appearance.coverColor >> 16) & 0xff) / 255.F;
		float coverG = ((book.appearance.coverColor >> 8) & 0xff) / 255.F;
		float coverB = (book.appearance.coverColor & 0xff) / 255.F;

		TextureManager render = this.mc.renderEngine;

		render.bindTexture(TEX_BOOK);
		RenderHelper.disableStandardItemLighting();

		GlStateManager.color(coverR, coverG, coverB);
		drawModalRectWithCustomSizedTexture(width / 2 - PAGE_WIDTH_UNSCALED / 2, height / 2 - PAGE_HEIGHT_UNSCALED / 2, 0, 0, PAGE_WIDTH_UNSCALED, PAGE_HEIGHT_UNSCALED, 512, 512);

		GlStateManager.color(1F, 1F, 1F);

		drawModalRectWithCustomSizedTexture(width / 2 - PAGE_WIDTH_UNSCALED / 2, height / 2 - PAGE_HEIGHT_UNSCALED / 2, 0, PAGE_HEIGHT_UNSCALED, PAGE_WIDTH_UNSCALED, PAGE_HEIGHT_UNSCALED, 512, 512);

		GlStateManager.pushMatrix();
		GlStateManager.translate(width / 2 - PAGE_WIDTH_UNSCALED / 2 + PAGE_MARGIN * 2, height / 2 - PAGE_HEIGHT_UNSCALED / 2 + PAGE_PADDING_TOP + PAGE_MARGIN, 0);

		if (book.appearance.drawPageNumbers)
			fontRenderer.drawString(page + 1 + "", PAGE_WIDTH / 2 - fontRenderer.getStringWidth(page + 1 + "") / 2, PAGE_HEIGHT - 10, 0xFFAAAAAA, false);

		for (int i = 0; i < elements.size(); i++) {
			BookElement element = elements.get(i);

			GlStateManager.color(1F, 1F, 1F, 1F);
			element.draw(0, 0, partialTicks, fontRenderer);
		}

		for (int i = 0; i < elements.size(); i++) {
			BookElement element = elements.get(i);

			GlStateManager.color(1F, 1F, 1F, 1F);
			element.drawOverlay(0, 0, partialTicks, fontRenderer);
		}

		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.popMatrix();
	}

	public void left() {
		if (page > 0) {
			page--;
			reload();
		}
	}

	public void right() {
		if (page < book.getPageCount(Minecraft.getMinecraft().player.getStatFileWriter()) - 1) {
			page++;
			reload();
		}
	}

	private void reload() {
		elements.clear();
		book.findPage(page, Minecraft.getMinecraft().player.getStatFileWriter()).content.build(book, elements, true);
		for (BookElement element : elements)
			element.parent = gui;
	}

}
