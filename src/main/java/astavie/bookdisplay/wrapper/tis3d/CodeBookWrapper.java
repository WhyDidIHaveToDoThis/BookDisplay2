package astavie.bookdisplay.wrapper.tis3d;

import li.cil.tis3d.client.gui.GuiBookCode;
import li.cil.tis3d.common.item.ItemBookCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CodeBookWrapper extends TIS3DWrapper<GuiBookCode> {

	private static final Method changePage = ReflectionHelper.findMethod(GuiBookCode.class, "changePage", null, int.class);
	private static final Constructor<GuiBookCode> constructor = ReflectionHelper.findConstructor(GuiBookCode.class, EntityPlayer.class);

	private final ItemBookCode.Data data;

	CodeBookWrapper(ItemStack book) {
		//noinspection EntityConstructor,NullableProblems
		super(newInstance(book), true);
		this.data = ReflectionHelper.getPrivateValue(GuiBookCode.class, this.book, "data");
	}

	private static GuiBookCode newInstance(ItemStack book) {
		try {
			return constructor.newInstance(new EntityPlayer(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getGameProfile()) {
				@Override
				public boolean isSpectator() {
					return false;
				}

				@Override
				public boolean isCreative() {
					return false;
				}

				@Nonnull
				@Override
				public ItemStack getHeldItem(EnumHand p_getHeldItem_1_) {
					return book;
				}

				@Override
				public boolean isEntityAlive() {
					return true;
				}
			});
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void draw(EnumHandSide side, float partialTicks) {
		GlStateManager.translate(0, (height - 230) / 2 - 2, 0);
		super.draw(side, partialTicks);
	}

	@Override
	public void left() {
		try {
			if (data.getSelectedPage() > 0)
				changePage.invoke(book, -1);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void right() {
		try {
			if (data.getSelectedPage() < data.getPageCount() - 1)
				changePage.invoke(book, 1);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
