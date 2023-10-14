package shadow;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ItemBox {
	int w,h,attack,defense;
	boolean exist;
	Image img;
	ItemBox() {
		ImageIcon icon = new ImageIcon("itemBox.png");
        img = icon.getImage();
        w=70;
        h=50;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
	public Image getImage() {
		return img;
	}
}
