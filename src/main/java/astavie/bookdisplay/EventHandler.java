package astavie.bookdisplay;

import astavie.bookdisplay.wrapper.IBookWrapper;
import astavie.bookdisplay.wrapper.mantle.MantleWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Pair;

public class EventHandler {

	private boolean enabled = false;
	private Pair<ItemStack, IBookWrapper> mainhand;
	private Pair<ItemStack, IBookWrapper> offhand;

	private int cachedWidth;
	private int cachedHeight;

	private void mainhand() {
		if (mainhand != null)
			mainhand.getRight().close();

		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItemMainhand();
		IBookWrapper wrapper = BookDisplay.find(stack);

		if (wrapper != null) {
			ScaledResolution size = new ScaledResolution(Minecraft.getMinecraft());
			wrapper.setSize(size.getScaledWidth(), size.getScaledHeight(), player.getPrimaryHand());
			mainhand = Pair.of(stack, wrapper);
		} else {
			mainhand = null;
		}
	}

	private void offhand() {
		if (offhand != null)
			offhand.getRight().close();

		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItemOffhand();
		IBookWrapper wrapper = BookDisplay.find(stack);

		if (wrapper != null) {
			ScaledResolution size = new ScaledResolution(Minecraft.getMinecraft());
			wrapper.setSize(size.getScaledWidth(), size.getScaledHeight(), player.getPrimaryHand().opposite());
			offhand = Pair.of(stack, wrapper);
		} else {
			offhand = null;
		}
	}

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent event) {
		if (BookDisplay.key.isPressed()) {
			if (enabled) {
				if (mainhand != null)
					mainhand.getRight().close();
				if (offhand != null)
					offhand.getRight().close();

				mainhand = null;
				offhand = null;
			} else {
				mainhand();
				offhand();
			}
			enabled = !enabled;
		}
		if (enabled) {
			if (BookDisplay.left.isPressed()) {
				if (mainhand != null)
					mainhand.getRight().left();
				if (offhand != null)
					offhand.getRight().left();
			}
			if (BookDisplay.right.isPressed()) {
				if (mainhand != null)
					mainhand.getRight().right();
				if (offhand != null)
					offhand.getRight().right();
			}
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event) {
		if (enabled) {
			if ((mainhand == null && !event.player.getHeldItemMainhand().isEmpty()) || (mainhand != null && !ItemStack.areItemStacksEqual(mainhand.getLeft(), event.player.getHeldItemMainhand())))
				mainhand();
			if ((offhand == null && !event.player.getHeldItemOffhand().isEmpty()) || (offhand != null && !ItemStack.areItemStacksEqual(offhand.getLeft(), event.player.getHeldItemOffhand())))
				offhand();
		}
	}

	@SubscribeEvent
	public void onOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && enabled) {
			ScaledResolution size = new ScaledResolution(Minecraft.getMinecraft());
			EntityPlayer player = Minecraft.getMinecraft().player;
			if (mainhand != null) {
				GlStateManager.pushMatrix();
				if (cachedWidth != size.getScaledWidth() || cachedHeight != size.getScaledHeight())
					mainhand.getRight().setSize(size.getScaledWidth(), size.getScaledHeight(), player.getPrimaryHand());
				mainhand.getRight().draw(player.getPrimaryHand(), event.getPartialTicks());
				GlStateManager.popMatrix();
			}
			if (offhand != null) {
				GlStateManager.pushMatrix();
				if (cachedWidth != size.getScaledWidth() || cachedHeight != size.getScaledHeight())
					offhand.getRight().setSize(size.getScaledWidth(), size.getScaledHeight(), player.getPrimaryHand().opposite());
				offhand.getRight().draw(player.getPrimaryHand().opposite(), event.getPartialTicks());
				GlStateManager.popMatrix();
			}
			cachedWidth = size.getScaledWidth();
			cachedHeight = size.getScaledHeight();
		}
	}

	@SubscribeEvent
	public void onGui(GuiOpenEvent event) {
		if (event.getGui() != null) {
			// Register mantle book
			if (Loader.isModLoaded("mantle"))
				MantleWrapper.register(event.getGui());

			if (enabled && BookDisplay.contains(event.getGui())) {
				if (mainhand != null)
					mainhand.getRight().close();
				if (offhand != null)
					offhand.getRight().close();

				mainhand = null;
				offhand = null;

				enabled = false;
			}
		}
	}

}
