package astavie.bookdisplay.wrapper.minecraft;

import astavie.bookdisplay.wrapper.BookWrapper;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.RenderComponentsUtil;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.HandSide;
import net.minecraft.util.text.ITextComponent;

public class VanillaWrapper extends BookWrapper<ReadBookScreen> {

	public VanillaWrapper(ItemStack book) {
		super(new ReadBookScreen(book.getItem() == Items.WRITABLE_BOOK ? new ReadBookScreen.UnwrittenBookInfo(book) : new ReadBookScreen.WrittenBookInfo(book)), true);
	}

	@Override
	public void draw(HandSide side, float partialTicks) {
		GlStateManager.translatef(0, (height - 192) / 2 - 2, 0);

		if (side == HandSide.RIGHT)
			GlStateManager.translatef(width, 0, 0);

		Minecraft minecraft = Minecraft.getInstance();

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(book.field_214167_b);
		int i = (this.width - 192) / 2;
		book.blit(i, 2, 0, 0, 192, 192);
		String s = I18n.format("book.pageIndicator", book.field_214169_d + 1, Math.max(book.func_214152_a(), 1));
		if (book.field_214171_f != book.field_214169_d) {
			ITextComponent itextcomponent = book.field_214168_c.func_216916_b(book.field_214169_d);
			book.field_214170_e = RenderComponentsUtil.splitText(itextcomponent, 114, book.font, true, true);
		}

		book.field_214171_f = book.field_214169_d;
		int i1 = book.func_214156_a(s);
		book.font.drawString(s, (float)(i - i1 + 192 - 44), 18.0F, 0);
		int k = Math.min(128 / 9, book.field_214170_e.size());

		for(int l = 0; l < k; ++l) {
			ITextComponent itextcomponent1 = book.field_214170_e.get(l);
			book.font.drawString(itextcomponent1.getFormattedText(), (float)(i + 36), (float)(32 + l * 9), 0);
		}

		ITextComponent itextcomponent2 = book.func_214154_c((double)0, (double)0);
		if (itextcomponent2 != null) {
			book.renderComponentHoverEffect(itextcomponent2, 0, 0);
		}

		for(int j = 0; j < book.buttons.size(); ++j) {
			book.buttons.get(j).render(0, 0, partialTicks);
		}
	}

	@Override
	public void left() {
		if (book.field_214169_d > 0)
			book.field_214169_d--;
	}

	@Override
	public void right() {
		if (book.field_214169_d < book.func_214152_a() - 1)
			book.field_214169_d++;
	}

}
