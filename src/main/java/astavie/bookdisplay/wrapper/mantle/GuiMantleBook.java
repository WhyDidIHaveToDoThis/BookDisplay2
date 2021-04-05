package astavie.bookdisplay.wrapper.mantle;

import astavie.bookdisplay.Reference;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.client.book.BookHelper;
import slimeknights.mantle.client.book.BookLoader;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.screen.book.BookScreen;
import slimeknights.mantle.client.screen.book.element.BookElement;

import java.util.ArrayList;

public class GuiMantleBook extends Screen {

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

	private final BookScreen gui;
	private final ItemStack item;

	private int page;

	public GuiMantleBook(BookData book, ItemStack item) {
		super(NarratorChatListener.EMPTY);
		this.book = book;
		this.gui = new BookScreen(NarratorChatListener.EMPTY, book, item);
		this.item = item;
		this.page = book.findPageNumber(BookHelper.getSavedPage(item), gui.advancementCache);
		if (page < 0)
			page = 0;
		reload();
	}

	@Override
	@SuppressWarnings("ForLoopReplaceableByForEach")
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		FontRenderer fontRenderer = book.fontRenderer;
		if (fontRenderer == null)
			fontRenderer = minecraft.fontRenderer;

		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();

		RenderSystem.pushMatrix();
		RenderSystem.color3f(1F, 1F, 1F);

		float coverR = ((book.appearance.coverColor >> 16) & 0xff) / 255.F;
		float coverG = ((book.appearance.coverColor >> 8) & 0xff) / 255.F;
		float coverB = (book.appearance.coverColor & 0xff) / 255.F;

		TextureManager render = minecraft.textureManager;

		render.bindTexture(TEX_BOOK);
		RenderHelper.disableStandardItemLighting();

		RenderSystem.color3f(coverR, coverG, coverB);
		blit(stack, width / 2 - PAGE_WIDTH_UNSCALED / 2, height / 2 - PAGE_HEIGHT_UNSCALED / 2, 0, 0,
				PAGE_WIDTH_UNSCALED, PAGE_HEIGHT_UNSCALED, 512, 512);

		RenderSystem.color3f(1F, 1F, 1F);

		blit(stack, width / 2 - PAGE_WIDTH_UNSCALED / 2, height / 2 - PAGE_HEIGHT_UNSCALED / 2, 0, PAGE_HEIGHT_UNSCALED,
				PAGE_WIDTH_UNSCALED, PAGE_HEIGHT_UNSCALED, 512, 512);

		RenderSystem.pushMatrix();
		RenderSystem.translatef(width / 2 - PAGE_WIDTH_UNSCALED / 2 + PAGE_MARGIN * 2,
				height / 2 - PAGE_HEIGHT_UNSCALED / 2 + PAGE_PADDING_TOP + PAGE_MARGIN, 0);

		if (book.appearance.drawPageNumbers)
			fontRenderer.drawString(stack, page + 1 + "",
					PAGE_WIDTH / 2 - fontRenderer.getStringWidth(page + 1 + "") / 2, PAGE_HEIGHT - 10, 0xFFAAAAAA);

		for (int i = 0; i < elements.size(); i++) {
			BookElement element = elements.get(i);

			RenderSystem.color4f(1F, 1F, 1F, 1F);
			element.draw(stack, 0, 0, partialTicks, fontRenderer);
		}

		for (int i = 0; i < elements.size(); i++) {
			BookElement element = elements.get(i);

			RenderSystem.color4f(1F, 1F, 1F, 1F);
			element.drawOverlay(stack, 0, 0, partialTicks, fontRenderer);
		}

		RenderSystem.popMatrix();

		super.render(stack, mouseX, mouseY, partialTicks);

		RenderSystem.popMatrix();
	}

	public void left() {
		if (page > 0) {
			page--;
			reload();
		}
	}

	public void right() {
		if (page < book.getPageCount(gui.advancementCache) - 1) {
			page++;
			reload();
		}
	}

	private void reload() {
		elements.clear();
		book.findPage(page, gui.advancementCache).content.build(book, elements, true);
		for (BookElement element : elements)
			element.parent = gui;
	}

	@Override
	public void onClose() {
		PageData page = book.findPage(this.page - 1, gui.advancementCache);
		if (page != null && page.parent != null)
			BookLoader.updateSavedPage(minecraft.player, item, page.parent.name + "." + page.name);
	}

}
