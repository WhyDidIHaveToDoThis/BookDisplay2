package astavie.bookdisplay.wrapper.tis3d;

import li.cil.tis3d.client.gui.GuiManual;
import li.cil.tis3d.client.manual.Document;
import li.cil.tis3d.common.api.ManualAPIImpl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ManualWrapper extends TIS3DWrapper<GuiManual> {

	private static final Method scrollTo = ReflectionHelper.findMethod(GuiManual.class, "scrollTo", null, int.class);

	ManualWrapper() {
		super(new GuiManual(), false);
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		book.drawScreen(0, 0, partialTicks);
	}

	@Override
	public void left() {
		try {
			scrollTo.invoke(book, ManualAPIImpl.peekOffset() - Document.lineHeight(book.fontRenderer) * 3);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void right() {
		try {
			scrollTo.invoke(book, ManualAPIImpl.peekOffset() + Document.lineHeight(book.fontRenderer) * 3);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setSize(int width, int height, EnumHandSide side) {
		super.setSize(width, height, side);
		book.initGui();

		int left = ReflectionHelper.getPrivateValue(GuiManual.class, book, "guiLeft");
		int offset = side == EnumHandSide.LEFT ? -width / 4 : width / 4;

		ReflectionHelper.setPrivateValue(GuiManual.class, book, left + offset, "guiLeft");
		for (GuiButton button : book.buttonList)
			button.x += offset;
	}

}
