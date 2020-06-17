package astavie.bookdisplay;

import astavie.bookdisplay.wrapper.BookWrapper;
import astavie.bookdisplay.wrapper.IBookWrapper;
import astavie.bookdisplay.wrapper.mantle.MantleWrapper;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.tuple.Pair;

public class EventHandler {

	private boolean enabled = false;
	private Pair<ItemStack, IBookWrapper> mainhand;
	private Pair<ItemStack, IBookWrapper> offhand;

	private int cachedWidth;
	private int cachedHeight;

	private void mainhand() {
		if (mainhand != null)
			mainhand.getRight().onClose();

		PlayerEntity player = Minecraft.getInstance().player;
		ItemStack stack = player.getHeldItemMainhand();
		IBookWrapper wrapper = BookDisplay.find(stack);

		if (wrapper != null) {
			MainWindow size = Minecraft.getInstance().getMainWindow();
			wrapper.setSize(size.getScaledWidth(), size.getScaledHeight(), player.getPrimaryHand());
			wrapper.onOpen();
			mainhand = Pair.of(stack, wrapper);
		} else {
			mainhand = null;
		}
	}

	private void offhand() {
		if (offhand != null)
			offhand.getRight().onClose();

		PlayerEntity player = Minecraft.getInstance().player;
		ItemStack stack = player.getHeldItemOffhand();
		IBookWrapper wrapper = BookDisplay.find(stack);

		if (wrapper != null) {
			MainWindow size = Minecraft.getInstance().getMainWindow();
			wrapper.setSize(size.getScaledWidth(), size.getScaledHeight(), player.getPrimaryHand().opposite());
			wrapper.onOpen();
			offhand = Pair.of(stack, wrapper);
		} else {
			offhand = null;
		}
	}

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent event) {
		if (BookDisplay.key.isPressed()) {
			if (shouldDisplay()) {
				disable();
			} else {
				enable();
			}
		}
		if (shouldDisplay()) {
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

	private void disable() {
		if (mainhand != null)
			mainhand.getRight().onClose();
		if (offhand != null)
			offhand.getRight().onClose();

		mainhand = null;
		offhand = null;

		enabled = false;
	}

	private void enable() {
		mainhand();
		offhand();

		enabled = true;
	}

	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event) {
		if (shouldDisplay()) {
			if ((mainhand == null && !event.player.getHeldItemMainhand().isEmpty()) || (mainhand != null && !ItemStack.areItemStacksEqual(mainhand.getLeft(), event.player.getHeldItemMainhand())))
				Minecraft.getInstance().enqueue(this::mainhand);
			if ((offhand == null && !event.player.getHeldItemOffhand().isEmpty()) || (offhand != null && !ItemStack.areItemStacksEqual(offhand.getLeft(), event.player.getHeldItemOffhand())))
				Minecraft.getInstance().enqueue(this::offhand);

			if (mainhand != null)
				mainhand.getRight().onTick();
			if (offhand != null)
				offhand.getRight().onTick();
		}
	}

	@SubscribeEvent
	public void onOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && shouldDisplay()) {
			MainWindow size = Minecraft.getInstance().getMainWindow();
			PlayerEntity player = Minecraft.getInstance().player;
			if (mainhand != null) {
				RenderSystem.pushMatrix();
				if (cachedWidth != size.getScaledWidth() || cachedHeight != size.getScaledHeight())
					mainhand.getRight().setSize(size.getScaledWidth(), size.getScaledHeight(), player.getPrimaryHand());
				mainhand.getRight().draw(player.getPrimaryHand(), event.getPartialTicks());
				RenderSystem.popMatrix();
			}
			if (offhand != null) {
				RenderSystem.pushMatrix();
				if (cachedWidth != size.getScaledWidth() || cachedHeight != size.getScaledHeight())
					offhand.getRight().setSize(size.getScaledWidth(), size.getScaledHeight(), player.getPrimaryHand().opposite());
				offhand.getRight().draw(player.getPrimaryHand().opposite(), event.getPartialTicks());
				RenderSystem.popMatrix();
			}
			cachedWidth = size.getScaledWidth();
			cachedHeight = size.getScaledHeight();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDrawBackground(GuiScreenEvent.BackgroundDrawnEvent event) {
		BookWrapper.onDrawBackground();
	}

	private boolean shouldDisplay() {
		return enabled;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onUseItem(PlayerInteractEvent.RightClickItem event) {
		if (shouldDisplay() && BookDisplay.has(event.getItemStack())) {
			disable();
		}
	}

	@SubscribeEvent
	public void onGui(GuiOpenEvent event) {
		if (event.getGui() != null && ModList.get().isLoaded("mantle"))
			MantleWrapper.register(event.getGui());
	}

}
