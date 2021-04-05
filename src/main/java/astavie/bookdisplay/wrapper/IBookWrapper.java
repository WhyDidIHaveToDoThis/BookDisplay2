package astavie.bookdisplay.wrapper;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.util.HandSide;

public interface IBookWrapper {

	void draw(MatrixStack stack, HandSide side, float partialTicks);

	void left();

	void right();

	void onOpen();

	void onTick();

	void onClose();

	void setSize(int width, int height, HandSide side);

}
