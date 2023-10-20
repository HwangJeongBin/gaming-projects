package shadow;

import java.awt.Font;
import java.awt.Image;
import java.util.Arrays;

import javax.swing.ImageIcon;

public class MainCharacter {
	private int x,y,w,h,lv,Width,Height,roomN,floor,attack,defense,hp,skillN,coolDown=0;	// floor 위에서 부터 0,1,2층
	private Image img;
	int skillX[] = new int[4];
	int skillY[] = new int[4];
	int skillDamage[] = new int[4];
	int skillTurn[] = new int[4];
	int skillTurnN[] = new int[4];	// 각 스킬의 대기 턴 수를 저장하는 변수
	boolean skill[] = new boolean[4];
	boolean usingSkill[] = new boolean[4];
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
        attack=1;
        defense=0;
        hp=18;
        skillN=1;
        for(int i=0;i<4;i++) {
        	skillTurn[i] = 0;
        }
        skillDamage[0] = 1;
        skillDamage[1] = 2;
        skillDamage[2] = attack*2;
        skillDamage[3] = attack*3;
        skillTurnN[0]=1;
        skillTurnN[1]=2;
        skillTurnN[2]=3;
        skillTurnN[3]=4;
        // for(int i=0;i<4;i++) skillTurn[i] = skillTurnN[i];
        Arrays.fill(skill,false);
        skill[0]=true;
        Arrays.fill(usingSkill,false);
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
	public void incAttack() {	// 스킬 데미지도 업데이트 해줘야됨
		this.attack++;
        skillDamage[2] = attack*2;
        skillDamage[3] = attack*3;
	}
	public int getDefense() {
		return defense;
	}
	public void incDefense() {
		this.defense++;
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
		skillN++;
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
	public boolean isUsingSkill(int i) {
		return usingSkill[i];
	}
	public void setUsingSkill(boolean usingSkill, int i) {
		if(usingSkill) {
			if(skillTurn[i]<=0) {
				skillTurn[i]=skillTurnN[i];
				this.usingSkill[i] = usingSkill;
			}
		}
		else this.usingSkill[i] = usingSkill;
	}
	public int getSkillDamgae(int n) {
		return skillDamage[n];
	}
	public void setSkillDamage(int d, int n) {
		this.skillX[n] = d;
	}
	public int getSkillTurn(int i) {
		return skillTurn[i];
	}
	public void decSkillTurn() {
		for(int i=0;i<4;i++) {
			this.skillTurn[i]--;
		}
	}
	public void incHp() {
		hp++;
	}
	public int getCoolDown() {
		return coolDown;
	}
	public void incCoolDown() {
		this.coolDown++;
	}
	public void useCoolDown() {
		if(coolDown<1) return;
		int z=0;
		for(int i=0;i<4;i++) {
			if(skillTurn[i]<=0) z++;
		}
		if(z>=4) return;
		this.coolDown--;
        for(int i=0;i<4;i++) {
        	skillTurn[i] = 0;
        }
	}
}