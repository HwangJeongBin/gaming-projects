package shadow;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Monster {
	int x,y,w,h,attack,hp,monN;	// monN은 몬스터 식별 번호
	boolean exist, damaged;
	String []name = {"treeMonster", "snakeMonster", "spiderMonster", "blockMonster"};	// 몬스터 리스트
	Image img;
	Monster() {
		x=0;
		y=0;
        w=110;
        h=130;
        hp=4;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
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
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
	public Image getImage() {
		if(damaged) {
			ImageIcon icon = new ImageIcon("damaged"+name[monN]+".png");
	        img = icon.getImage();
		}
		else {
			ImageIcon icon = new ImageIcon(name[monN]+".png");
        	img = icon.getImage();
		}
		return img;
	}
	public int getMonN() {
		return monN;
	}
	public void setMonN(int monN) {	// 몬스터 번호를 설정해주면 공격력과 방어력이 정해짐
		this.monN = monN;
		switch(monN) {
		case 0: attack=1; hp=4;	// 나무
		break;
		case 1: attack=2; hp=4;	// 뱀
		break;
		case 2: attack=3; hp=4; w=150;	// 거미
		break;
		case 3: attack=1; hp=6;	// 돌
		break;
		}
	}
	public void moveX(int x) {
		this.x+=x;
	}
	public void moveY(int y) {
		this.y+=y;
	}
	public int getHp() {
		return this.hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public void damaged(int h) {
		hp-=h;
	}
	public boolean isDamaged() {
		return damaged;
	}
	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
	}
}