package astavie.bookdisplay.wrapper;

import net.minecraft.util.HandSide;

public interface IBookWrapper {

	void draw(HandSide side, float partialTicks);

	void left();

	void right();

	void onOpen();

	void onTick();

	void onClose();

	void setSize(int width, int height, HandSide side);

}
