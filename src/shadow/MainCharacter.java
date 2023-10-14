package shadow;

import java.awt.Font;
import java.awt.Image;
import java.util.Arrays;

import javax.swing.ImageIcon;

public class MainCharacter {
	private int x,y,w,h,lv,Width,Height,roomN,floor,attack,defense,hp,skillN;	// floor 위에서 부터 0,1,2층
	private Image img;
	int skillX[] = new int[4];
	int skillY[] = new int[4];
	boolean skill[] = new boolean[4];
	MainCharacter(int W, int H) {
		this.Width = W;
		this.Height = H;
		ImageIcon icon = new ImageIcon("standingRightCharacter.png");
        img = icon.getImage();
        w=Width/25;
        h=Height*8/30;
        roomN=0;
        floor=1;
        x=Width/16;
        y=Height*2/5;
        attack=4;
        defense=0;
        hp=8;
        skillN=4;
        Arrays.fill(skill,true);
	}
	// true-걷기 false-서기
	void lookFront(boolean b) {
		if(b) {
			ImageIcon icon = new ImageIcon("walkingRightCharacter.png");
			img = icon.getImage();
		}
		else {
			ImageIcon icon = new ImageIcon("standingRightCharacter.png");
			img = icon.getImage();
		}
	}
	void lookBehind(boolean b) {
		if(b) {
			ImageIcon icon = new ImageIcon("walkingLeftCharacter.png");
			img = icon.getImage();
		}
		else {
			ImageIcon icon = new ImageIcon("standingLeftCharacter.png");
			img = icon.getImage();
		}
	}
	void isDamaged(boolean b) {
		if(b) {
			ImageIcon icon = new ImageIcon("damagedStandingRightCharacter.png");
			img = icon.getImage();
		}
		else {
			ImageIcon icon = new ImageIcon("standingRightCharacter.png");
			img = icon.getImage();
		}
	}
	void setDragonPos() {
		x=Width/40;
		y=Height-h;
	}
	void moveRight(int d) {
		x+=d;
	}
	void moveLeft(int d) {
		x-=d;
	}
	void moveUp(int d) {
		y-=d;
	}
	void moveDown(int d) {
		y+=d;
	}
	Image getImage() {
		return img;
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
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public int getWidth() {
		return Width;
	}
	public void setWidth(int width) {
		Width = width;
	}
	public int getHeight() {
		return Height;
	}
	public void setHeight(int height) {
		Height = height;
	}
	public int getRoomN() {
		return roomN;
	}
	public void setRoomN(int roomN) {
		this.roomN = roomN;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public void damaged(int hp) {
		this.hp -= hp;
	}
	public void healed(int hp) {
		this.hp += hp;
	}
	public int getSkillN() {
		return skillN;
	}
	public void setSkillN(int skillN) {
		this.skillN = skillN;
	}
	public void getSkill(int n) {
		skill[n] = true;
	}
	public boolean[] getSkill() {
		return skill;
	}
	public boolean haveSkill(int n) {
		return skill[n];
	}
	public int getSkillX(int n) {
		return skillX[n];
	}
	public void setSkillX(int pos, int n) {
		this.skillX[n] = pos;
	}
	public int getSkillY(int n) {
		return skillY[n];
	}
	public void setSkillY(int pos, int n) {
		this.skillY[n] = pos;
	}
}