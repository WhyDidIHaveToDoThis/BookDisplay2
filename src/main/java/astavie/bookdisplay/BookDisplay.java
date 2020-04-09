package astavie.bookdisplay;

import astavie.bookdisplay.wrapper.IBookWrapper;
import astavie.bookdisplay.wrapper.botania.BotaniaWrapper;
import astavie.bookdisplay.wrapper.immersiveengineering.IEWrapper;
import astavie.bookdisplay.wrapper.mantle.MantleWrapper;
import astavie.bookdisplay.wrapper.minecraft.VanillaWrapper;
import astavie.bookdisplay.wrapper.patchouli.PatchouliWrapper;
import net.minecraft.client.gui.screen.EditBookScreen;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@Mod(Reference.MOD_ID)
public class BookDisplay {

	static final KeyBinding key = new KeyBinding("key.bookdisplay.display", GLFW.GLFW_KEY_R, "key.categories.book");
	static final KeyBinding left = new KeyBinding("key.bookdisplay.left", GLFW.GLFW_KEY_LEFT, "key.categories.book");
	static final KeyBinding right = new KeyBinding("key.bookdisplay.right", GLFW.GLFW_KEY_RIGHT, "key.categories.book");

	private static final Set<Class<? extends Screen>> books = new HashSet<>();
	private static final Map<Predicate<ItemStack>, Function<ItemStack, IBookWrapper>> registry = new HashMap<>();

	public static void register(Class<? extends Screen> type, Predicate<ItemStack> predicate, Function<ItemStack, IBookWrapper> factory) {
		books.add(type);
		registry.put(predicate, factory);
	}

	static IBookWrapper find(ItemStack stack) {
		if (stack.isEmpty())
			return null;
		for (Map.Entry<Predicate<ItemStack>, Function<ItemStack, IBookWrapper>> entry : registry.entrySet())
			if (entry.getKey().test(stack))
				return entry.getValue().apply(stack);
		return null;
	}

	static boolean contains(Screen book) {
		if (books.contains(book.getClass()))
			return true;
		for (Class<?> clazz : books)
			if (clazz.isInstance(book))
				return true;
		return false;
	}

	public BookDisplay() {
		// Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		// Add init method to event bus
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
	}

	public void init(final FMLClientSetupEvent event) {
		key.setKeyConflictContext(KeyConflictContext.IN_GAME);
		left.setKeyConflictContext(KeyConflictContext.IN_GAME);
		right.setKeyConflictContext(KeyConflictContext.IN_GAME);

		ClientRegistry.registerKeyBinding(key);
		ClientRegistry.registerKeyBinding(left);
		ClientRegistry.registerKeyBinding(right);

		MinecraftForge.EVENT_BUS.register(new EventHandler());

		register(ReadBookScreen.class, item -> item.getItem() == Items.WRITABLE_BOOK || item.getItem() == Items.WRITTEN_BOOK, VanillaWrapper::new);
		books.add(EditBookScreen.class);

		// if (ModList.get().isLoaded("bibliocraft"))
			// BiblioCraftWrapper.register();
		if (ModList.get().isLoaded("immersiveengineering"))
			IEWrapper.register();
		if (ModList.get().isLoaded("mantle"))
			MantleWrapper.register();
		// if (ModList.get().isLoaded("opencomputers"))
			// OCWrapper.register();
		// if (ModList.get().isLoaded("tis3d"))
			// TIS3DWrapper.register();
		if (ModList.get().isLoaded("botania"))
			BotaniaWrapper.register();
		if (ModList.get().isLoaded("patchouli"))
			PatchouliWrapper.register();
	}

}
