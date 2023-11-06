package shadow;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ItemBox {
	int w,h,openTime=0, itemN;	//itemN 11,12,13은 빛의 조각(이용할 수 있는 스킬이 늘어남) | 2는 hp | 3은 스킬 쿨 다운 | 41/42는 공격력/방어력 증가
	boolean exist, opened=false;
	Image img, itemImg;
	ItemBox() {
		ImageIcon icon = new ImageIcon("itemBox.png");
        img = icon.getImage();
        w=70;
        h=70;
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
	public Image getItemImage() {
		return itemImg;
	}
	void itemOpen(boolean b) {
		if(b) {
			ImageIcon icon = new ImageIcon("openedItemBox.png");
	        img = icon.getImage();
		}
		else {
			ImageIcon icon = new ImageIcon("itemBox.png");
	        img = icon.getImage();
		}
		opened=b;
	}
	boolean isItemOpen() {
		return opened;
	}
	public int getOpenTime() {
		return openTime;
	}
	public void incOpenTime() {
		this.openTime++;
	}
	public int getItemN() {
		return itemN;
	}
	public void setItemN(int itemN) {
		this.itemN = itemN;
		ImageIcon icon;
		switch(itemN) {
		case 11:
			icon = new ImageIcon("patchOfLight1.gif");
			itemImg = icon.getImage();
			break;
		case 12:
			icon = new ImageIcon("patchOfLight2.gif");
	        itemImg = icon.getImage();
	        break;
		case 13:
			icon = new ImageIcon("patchOfLight3.gif");
	        itemImg = icon.getImage();
	        break;
		case 2:
			icon = new ImageIcon("heart.png");
	        itemImg = icon.getImage();
	        break;
		case 3:
			icon = new ImageIcon("sandglass.png");
	        itemImg = icon.getImage();
	        break;
		case 41:
			icon = new ImageIcon("attack.png");
	        itemImg = icon.getImage();
	        break;
		case 42:
			icon = new ImageIcon("defense.png");
	        itemImg = icon.getImage();
	        break;
		}
	}
}
