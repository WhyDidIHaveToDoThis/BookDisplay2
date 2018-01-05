package astavie.bookdisplay.wrapper;

import net.minecraft.util.EnumHandSide;

public interface IBookWrapper {

	void draw(EnumHandSide side, float partialTicks);

	void left();

	void right();

	void setSize(int width, int height);

}
