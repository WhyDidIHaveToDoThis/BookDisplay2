package astavie.bookdisplay.wrapper.opencomputers;

import astavie.bookdisplay.BookDisplay;
import astavie.bookdisplay.wrapper.BookWrapper;
import li.cil.oc.client.gui.Manual;
import li.cil.oc.client.renderer.markdown.Document;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OCWrapper extends BookWrapper<Manual> {

	private static final Method scrollTo = ReflectionHelper.findMethod(Manual.class, "scrollTo", null, int.class);

	private OCWrapper() {
		super(new Manual(), true);
	}

	@SuppressWarnings("ConstantConditions")
	public static void register() {
		BookDisplay.register(Manual.class, item -> ArrayUtils.contains(OreDictionary.getOreIDs(item), OreDictionary.getOreID("oc:manual")), item -> new OCWrapper());
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		GlStateManager.translate(-width / 2, 0, 0);
		super.draw(side, partialTicks);
	}

	@Override
	public void left() {
		try {
			scrollTo.invoke(book, book.offset() - Document.lineHeight(book.fontRenderer) * 3);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void right() {
		try {
			scrollTo.invoke(book, book.offset() + Document.lineHeight(book.fontRenderer) * 3);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
