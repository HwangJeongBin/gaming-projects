package shadow;

public class Room {
	private boolean monster, item, visit, upStair, downStair;
	private int floor;	// 너비랑 높이가 필요할까?
	Room() {
		monster=false;
		item=false;
		setUpStair(false);
		setDownStair(false);
	}
	public boolean isMonster() {
		return monster;
	}
	public void setMonster(boolean monster) {
		this.monster = monster;
	}
	public boolean isItem() {
		return item;
	}
	public void setItem(boolean item) {
		this.item = item;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public boolean isVisit() {
		return visit;
	}
	public void setVisit(boolean visit) {
		this.visit = visit;
	}
	public boolean isUpStair() {
		return upStair;
	}
	public void setUpStair(boolean upStair) {
		this.upStair = upStair;
	}
	public boolean isDownStair() {
		return downStair;
	}
	public void setDownStair(boolean downStair) {
		this.downStair = downStair;
	}
	
}