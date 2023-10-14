package shadow;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageIO;
import javax.swing.*;

public class TempleFrame {
	private JFrame frame;
	private MyPanel panel;	// 층은 0층부터 시작하므로 총 탑의 층 수는 top+1
	private int y=0, W,H, moveX, moveY, cnt=0, floor=1, top=15, moveFloor, sleeptime=70, damageY=0, damagedHP=0, gameOverH=0, toGoF=0, toGoR=0;	// floor은 화면의 층이지 캐릭터의 현재 층이 아님
	private int stair[] = new int[top];
	private Monster mon[][] = new Monster[top+1][3];
	private ItemBox[][] item = new ItemBox[top+1][3];
	private Image leftRoom[] = new Image[5];
	private Image rightRoom[] = new Image[5];
	private Image middleRoom[] = new Image[5];
	private MainCharacter m;
	private Room room[][] = new Room[top+1][3];
	private MyThread t = new MyThread();
	private boolean moveXFlag = false, moveYFlag = false, bottomFlag=false, moveBack=false, monAttack=false, snakeBite=false, spiderBite=false, mainDamage=false, inviHeart=false, mainTurn=false, mainAttack=false;
	Container contentPane;
	private Image darkLeftRoom[] = new Image[5];
	private Image darkRightRoom[] = new Image[5];
	private Image darkMiddleRoom[] = new Image[5];
    private Image snakeBiteIcon;
    private Image spiderBiteIcon;
	private Image ivy;
	private Image portal;
	private Image attackIcon;
	private Image defenseIcon;
	private Image heart;
	private Image gameOver;
	private Image skill[] = new Image[4];
	private Image skillFrame;
	
	TempleFrame() {
		frame = new JFrame();
		frame.setTitle("Shadow");
		frame.setSize(1150,650);
		frame.setVisible(true);
		contentPane = frame.getContentPane();
		panel = new MyPanel();
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		W = contentPane.getWidth();
		H = contentPane.getHeight();
		frame.setVisible(true);
        panel.addMouseListener(new MyMouseListener());
        m = new MainCharacter(W,H);
        for(int i=0;i<top+1;i++) {
        	for(int j=0;j<3;j++) {
        		room[i][j] = new Room();
        		mon[i][j] = new Monster();
        		item[i][j] = new ItemBox();
        	}
        }
        // 정수 배열만들어서 몇번째 칸에서 위아래로 움직일 수 있을지 설정
        for(int i=0;i<top-1;i++) {
        	stair[i] = (int)(Math.random()*3);
        	room[i+1][stair[i]].setDownStair(true);
            room[i][stair[i]].setUpStair(true);
        }
        for(int i=0;i<top*4/3;i++) {
        	int mf = (int)(Math.random()*top+1);
        	int mr = (int)(Math.random()*3);
        	if(mon[mf][mr].isExist()||room[mf][mr].isDownStair()||(mf==top&&mr==2)||(mf==1&&mr==0)) i--;	// 몬스터 세팅이 중복되지 않고 덩쿨이나 포탈 및 스폰 위치에 몬스터가 있지 않게 처리
        	else {
        		mon[mf][mr].setExist(true);
        		mon[mf][mr].setMonN(i%4);
        	}
        }
        for(int i=0;i<top;i++) {
        	int f = (int)(Math.random()*top+1);
        	int r = (int)(Math.random()*3);
        	if(f==1&&r==0) {
        		i--;
        		continue;
        	}
        	item[f][r].setExist(true);
        }
        room[top-1][0].setUpStair(true);	// 포탈 위치이 있는 꼭대기 층은 항상 맨 왼쪽에 계단 생성 (포탈의 위치는 오른쪽방)
        room[1][0].setVisit(true);	// 캐릭터의 첫 생성 위치는 이미 방문했다는 것을 표시
        t.start();
        
        //m.setFloor(top);
        //floor=top;
        
	}
	public class MyThread extends Thread
	{
		public void run()
		{
			// 인터럽트 됬을때 예외처리
			try
			{
			    while(true) {
			    	//이동하는 것 보다 전투 씬이 우선 공격받는 동안은 움직이지 못함 
			    	if(monAttack) {	// 몬스터 공격 애니메이션
			    		Monster monster = mon[m.getFloor()][m.getRoomN()];
			    		if(monster.monN==0||monster.monN==3) {	// 나무 몬스터나 블럭 몬스터이면 내려찍는 공격
			    			if(cnt<8) {	// 몬스터 공격 구간
			    				mon[m.getFloor()][m.getRoomN()].moveX(-W/48);
			    				if(cnt<6) {
				    				mon[m.getFloor()][m.getRoomN()].moveY(-H/36);
				    			}
				    			else {
				    				mon[m.getFloor()][m.getRoomN()].moveY(H/12);
				    			}
			    			}
			    			else {	// 몬스터가 공격 후 제자리로 돌아오는 구간
			    				damagedHP=monster.getAttack()-m.getDefense();
			    				if(cnt%4==1) {
			    					inviHeart=true;		// 캐릭터가 데미지를 입으면 하트가 사라짐
			    					m.isDamaged(true);	// 캐릭터가 데미지를 입으면 캐릭터가 어두워짐
			    				}
				    			else {
				    				inviHeart=false;
				    				m.isDamaged(false);
				    			}
			    				damageY+=5;
				    			mainDamage=true;
			    				mon[m.getFloor()][m.getRoomN()].moveX(W/48);
			    			}
			    		}
			    		else {
			    			if(cnt<4) {	// 몬스터 공격 구간
			    				mon[m.getFloor()][m.getRoomN()].moveX(-W/28);
			    			}
			    			else if(cnt>=4&&cnt<=6) {
			    				if(monster.getMonN()==1) snakeBite=true;
			    				if(monster.getMonN()==2) spiderBite=true;
			    			}
			    			else {
			    				damagedHP=monster.getAttack()-m.getDefense();
			    				if(cnt%4==1) {
			    					inviHeart=true;		// 캐릭터가 데미지를 입으면 하트가 사라짐
			    					m.isDamaged(true);	// 캐릭터가 데미지를 입으면 캐릭터가 어두워짐
			    				}
				    			else {
				    				inviHeart=false;
				    				m.isDamaged(false);
				    			}
			    				damageY+=4;
				    			mainDamage=true;
			    				mon[m.getFloor()][m.getRoomN()].moveX(W/70);
			    			}
			    		}
			    		cnt++;
			    		if(cnt==16) {
			    			mon[m.getFloor()][m.getRoomN()].setX(0);
			    			mon[m.getFloor()][m.getRoomN()].setY(0);
			    			monAttack=false;
			    			mainTurn=true;
			    			cnt=0;
			    			snakeBite=false;
			    			spiderBite=false;
			    			damageY=0;
			    			mainDamage=false;
			    			m.isDamaged(false);
			    			m.damaged(damagedHP);
			    			damagedHP=0;
			    			inviHeart=false;
			    		}
			    	}
			    	else if(moveXFlag) {
			    		if(moveBack) {
			    			if(cnt%2==0) m.lookBehind(true);
			    			else m.lookBehind(false);
			    		}
			    		else {
			    			if(cnt%2==0&&cnt<15) m.lookFront(true);
			    			else m.lookFront(false);
			    		}
			    		if(cnt<15) {
			    			m.moveRight(moveX/15);
			    			cnt++;
			    		}
			    		else {
			    			m.setRoomN(toGoR);
			    			if(moveBack) {
		    					moveBack=false;
		    					m.lookFront(false);
		    				}
			    			if(m.getFloor()==top&&m.getRoomN()==2) {
			    				frame.dispose();
					    		DragonFrame dragon = new DragonFrame(frame.getX(),frame.getY(),W,H);
					    	}
			    			cnt=0;
			    			moveXFlag = false;
			    			if(mon[m.getFloor()][m.getRoomN()].isExist()) monAttack=true;
			    		}
			    	}
			    	else if(moveYFlag) {
			    		if(cnt<5) {
			    			m.moveDown(moveY/5);
			    			cnt++;
			    		}
			    		else if(cnt<10&&(!bottomFlag)) {
			    			y-=moveY/5;
			    			m.moveDown(-moveY/5);
			    			cnt++;
			    		}
			    		else {
			    			m.setFloor(toGoF);
			    			cnt=0;
			    			moveYFlag=false;
			    			bottomFlag=false;
			    			y=0;
			    			floor=moveFloor;
			    			if(mon[m.getFloor()][m.getRoomN()].isExist()) monAttack=true;
			    			if(m.getFloor()==top&&m.getRoomN()==2) {
			    				frame.dispose();
					    		DragonFrame dragon = new DragonFrame(frame.getX(),frame.getY(),W,H);
					    	}
			    		}
			    	}
			    	if(m.getHp()<=0) {
		            	gameOverH+=50;
		            	if(gameOverH>H) gameOverH=H;
		            }
			    	panel.repaint();
			        Thread.sleep(sleeptime);	// 스레드 대기
			    }
			}catch(InterruptedException e)
			{
			    System.out.println(e);
			}
		}
	}
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
		public void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon icon;
            Image drawRoom = null;
            for(int i=0;i<5;i++) {
            	icon = new ImageIcon("leftRoom"+i+".png");
            	leftRoom[i] = icon.getImage();
                icon = new ImageIcon("rightRoom"+i+".png");
                rightRoom[i] = icon.getImage();
                icon = new ImageIcon("middleRoom"+i+".png");
                middleRoom[i] = icon.getImage();
                icon = new ImageIcon("darkLeftRoom"+i+".png");
                darkLeftRoom[i] = icon.getImage();
                icon = new ImageIcon("darkRightRoom"+i+".png");
                darkRightRoom[i] = icon.getImage();
                icon = new ImageIcon("darkMiddleRoom"+i+".png");
                darkMiddleRoom[i] = icon.getImage();
            }
            icon = new ImageIcon("snakeBite.png");
            snakeBiteIcon = icon.getImage();
            icon = new ImageIcon("spiderBite.png");
            spiderBiteIcon = icon.getImage();
            icon = new ImageIcon("heart.png");
            heart = icon.getImage();
            icon = new ImageIcon("attack.png");
            attackIcon = icon.getImage();
            icon = new ImageIcon("defense.png");
            defenseIcon = icon.getImage();
            icon = new ImageIcon("ivy.png");
            ivy = icon.getImage();
            icon = new ImageIcon("portal.png");
            portal = icon.getImage();
            icon = new ImageIcon("gameOver.gif");
            gameOver = icon.getImage();
            icon = new ImageIcon("skillFrame.png");
            skillFrame = icon.getImage();
            icon = new ImageIcon("slam.png");
            for(int i=0;i<4;i++) {
            	icon = new ImageIcon("skill"+i+".gif");
            	skill[i] = icon.getImage();
            }
            
            int f = m.getFloor();
            int r = m.getRoomN();
            // 이미 지나간 방은 밝은 색
            for(int i=0;i<3;i++) {
            	if(floor+1-i>top) continue;	// 꼭대기 층 출력
            	// 방 출력
            	for(int j=0;j<3;j++) {
            		if(room[floor+1-i][j].isVisit()) {
            			switch(j) {
            			case 0: drawRoom=leftRoom[(floor+1-i)%5]; break;
            			case 1: drawRoom=middleRoom[(floor+1-i)%5]; break;
            			case 2: drawRoom=rightRoom[(floor+1-i)%5];
            			}
            		}
            		else {
            			switch(j) {
            			case 0: drawRoom=darkLeftRoom[(floor+1-i)%5]; break;
            			case 1: drawRoom=darkMiddleRoom[(floor+1-i)%5]; break;
            			case 2: drawRoom=darkRightRoom[(floor+1-i)%5];
            			}
            		}
            		g.drawImage(drawRoom, j*W/3-j/2, i*H/3+y, W/3, H/3, this);
            		if(room[floor+1-i][j].isUpStair()) g.drawImage(ivy, j*W/3+W/9-W/50, i*H/3+y, W/13, H/4, this);
            		if(mon[floor+1-i][j].isExist()&&room[floor+1-i][j].isVisit()) g.drawImage(mon[floor+1-i][j].getImage(), j*W/3+2*W/11+mon[floor+1-i][j].getX(), (i+1)*H/3-mon[floor+1-i][j].getH()+y+mon[floor+1-i][j].getY(), mon[floor+1-i][j].getW(), mon[floor+1-i][j].getH(), this);
            		if(item[floor+1-i][j].isExist()&&room[floor+1-i][j].isVisit()) g.drawImage(item[floor+1-i][j].getImage(), j*W/3+W/4, (i+1)*H/3-item[floor+1-i][j].getH()+y, item[floor+1-i][j].getW(), item[floor+1-i][j].getH(), this);
            	}
            	// 덩쿨 출력
            	if(floor==top) g.drawImage(portal, 7*W/9, H/7+H/3+y, W/12, H/12, this);
            }
            // 애니메이션 과정에서 층이 바뀌면서도 room의 정보를 가져올 수 있게
            if(y>0) {
            	if(floor+2>top) {
            		System.out.println("꼭대기 top");
            		System.out.println(floor);
            	}
            	else {
            		for(int j=0;j<3;j++) {
            			if(room[floor+2][j].isVisit()) {
                			switch(j) {
                			case 0: drawRoom=leftRoom[(floor+2)%5]; break;
                			case 1: drawRoom=middleRoom[(floor+2)%5]; break;
                			case 2: drawRoom=rightRoom[(floor+2)%5];
                			}
                		}
                		else {
                			switch(j) {
                			case 0: drawRoom=darkLeftRoom[(floor+2)%5]; break;
                			case 1: drawRoom=darkMiddleRoom[(floor+2)%5]; break;
                			case 2: drawRoom=darkRightRoom[(floor+2)%5];
                			}
                		}
            			g.drawImage(drawRoom, j*W/3-j/2, y-(H/3), W/3, H/3, this);
            			if(room[floor+2][j].isUpStair()) g.drawImage(ivy, j*W/3+W/9-W/50, y-(H/3), W/13, H/4, this);
            			if(mon[floor+2][j].isExist()&&room[floor+2][j].isVisit()) g.drawImage(mon[floor+2][j].getImage(), j*W/3+2*W/11, y-mon[floor+2][j].getH(), mon[floor+2][j].getW(), mon[floor+2][j].getH(), this);
            			if(item[floor+2][j].isExist()&&room[floor+2][j].isVisit()) g.drawImage(item[floor+2][j].getImage(), j*W/3+W/4, y-item[floor+2][j].getH(), item[floor+2][j].getW(), item[floor+2][j].getH(), this);
            		}
                	if(floor+3==top) g.drawImage(portal, 7*W/9, y+2*H, W/12, H/12, this);
            	}
            }
            if(y>-(H/3)&&y<0) {
            	for(int j=0;j<3;j++) {
            		if(room[floor-2][j].isVisit()) {
            			switch(j) {
            			case 0: drawRoom=leftRoom[(floor-2)%5]; break;
            			case 1: drawRoom=middleRoom[(floor-2)%5]; break;
            			case 2: drawRoom=rightRoom[(floor-2)%5];
            			}
            		}
            		else {
            			switch(j) {
            			case 0: drawRoom=darkLeftRoom[(floor-2)%5]; break;
            			case 1: drawRoom=darkMiddleRoom[(floor-2)%5]; break;
            			case 2: drawRoom=darkRightRoom[(floor-2)%5];
            			}
            		}
            		g.drawImage(drawRoom, j*W/3-j/2, y+H-1, W/3, H/3, this);
        			if(room[floor-2][j].isUpStair()) g.drawImage(ivy, j*W/3+W/9-W/50, y+H-1, W/13, H/4, this);
        			if(mon[floor-2][j].isExist()&&room[floor-2][j].isVisit()) g.drawImage(mon[floor-2][j].getImage(), j*W/3+2*W/11, y+4*H/3-mon[floor-2][j].getH(), mon[floor-2][j].getW(), mon[floor-2][j].getH(), this);
        			if(item[floor-2][j].isExist()&&room[floor-2][j].isVisit()) g.drawImage(item[floor-2][j].getImage(), j*W/3+W/4, y+4*H/3-item[floor-2][j].getH(), item[floor-2][j].getW(), item[floor-2][j].getH(), this);
        		}
            }
            g.drawImage(m.getImage(),m.getX(),m.getY(),m.getW(),m.getH(),this);
            if(snakeBite) g.drawImage(snakeBiteIcon,m.getX()-m.getW(),m.getY()-m.getH()/4,m.getW()*3,m.getH()*3/2,this);
            if(spiderBite) g.drawImage(spiderBiteIcon,m.getX()-m.getW(),m.getY()-m.getH()/4,m.getW()*3,m.getH()*3/2,this);
            g.setFont(new Font("monospaced",Font.BOLD,22));
            g.setColor(Color.RED);
            if(mainDamage) g.drawString(Integer.toString(damagedHP), m.getX()-m.getW()/5, m.getY()+m.getH()/8-damageY);
            for(int i=0;i<m.getHp();i++) {
            	if((m.getHp()-damagedHP<i+1)&&inviHeart) continue;
            	g.drawImage(heart, 25*i+10, 10, 20, 20, this);
            }
            //g.setFont(new Font("monospaced",Font.BOLD,22));
            g.setColor(Color.WHITE);
            g.drawImage(attackIcon, 10, 40, 25, 25, this);
            g.drawString(Integer.toString(m.getAttack()), 40, 60);
            g.drawImage(defenseIcon, 65, 40, 25, 25, this);
            g.drawString(Integer.toString(m.getDefense()), 95, 60);
            if(mainTurn) {
            	int s = m.getSkillN(), cnt=0;
            	g.drawImage(skillFrame, m.getX()+25-50*s, m.getY()-100, 100*s, 100, this);
            	// 배열을 써서 몇번 스킬을 먼저 출력할지를 확인 / 배열을 통해 스킬 넘버 받은 후에 배열 길이 만큼 for문
            	for(int i=0;i<4;i++) {
            		if(m.haveSkill(i)) {
            			g.drawImage(skill[i], m.getX()+60-50*s+85*cnt, m.getY()-100, 80, 100, this);
            			m.setSkillX(m.getX()+60-50*s+85*cnt,i);
            			m.setSkillY(m.getY()-100,i);
            			cnt++;
            		}
            	}
            }
            if(m.getHp()<=0) {
            	 g.drawImage(gameOver, 0, 0, W, gameOverH, this);
            }
        }
	}
	private class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// 근접한 방으로만 이동할 수 있게
			int x = e.getX();
			int y = e.getY();
			if(mainTurn) {
				int f = 1;
				if(m.getFloor()==0) f=0;
				int r = m.getRoomN();
				System.out.println(W);
				System.out.println(H);
				Rectangle monRec = new Rectangle(r*W/3+2*W/11, (f+1)*H/3-mon[f][r].getH()+mon[f][r].getY(), mon[f][r].getW(), mon[f][r].getH());
				if(monRec.contains(x, y)) {
					System.out.println("공격!!");
				}
			}
			if((!moveXFlag)&&(!moveYFlag)&&(!monAttack)&&(!mainAttack)) {
				Rectangle rec[][] = new Rectangle[3][3];
				for(int i=0;i<3;i++) {
					for(int j=0;j<3;j++) {
						rec[i][j] = new Rectangle(j*W/3, i*H/3, W/3, H/3);
						if(rec[i][j].contains(x, y)) {
							if(j==m.getRoomN()&&!(m.getFloor()==0&&i==0)) {	// 위 아래 이동
								if(m.getFloor()==0&&room[0][m.getRoomN()].isUpStair()&&i==1) {	// 맨 아래 층일 때는 예외(화면의 층 중 맨 아래 층에 캐릭터가 위치하기 때문)
								}
								else if(!((room[floor][m.getRoomN()].isUpStair()&&i==0)||(room[floor][m.getRoomN()].isDownStair()&&i==2))) {	// 위아래로 이동 가능 여부 결정
									continue;
								}
								if(floor+1-i>=0) {
									moveFloor = floor+1-i;
									if(moveFloor>top) break;
									if(moveFloor==0||m.getFloor()==0) {
										bottomFlag = true;
										room[moveFloor][m.getRoomN()].setVisit(true);
										moveFloor=1;	// 맨 아래 층으로 내려가면 현재 층 위치는 위층으로 남겨둔 채 캐릭터의 위치만 변경(화면에 보이는 층 변경없이 캐릭터만 이동)*
										if(m.getFloor()==0&&i==1) moveY=-H/3;
										else moveY = ((i-1)*H/3);
									}
									else {
										room[moveFloor][m.getRoomN()].setVisit(true);
										moveY = ((i-1)*H/3);
									}
									toGoF=floor+1-i;
									moveYFlag = true;
									if(mon[m.getFloor()][m.getRoomN()].isExist()) monAttack=true;	// 캐릭터가 몬스터한테서 도망칠 경우 데미지 받고 도망
								}
							}
							else if((i==1||(m.getFloor()==0&&i==2))&&(m.getRoomN()-j>=-1&&m.getRoomN()-j<=1)) {	// 옆 방으로 이동
								if(j<m.getRoomN()) moveBack=true;
								moveXFlag = true;
								moveX = ((j-m.getRoomN())*W/3);
								if(mon[m.getFloor()][m.getRoomN()].isExist()) monAttack=true;	// 캐릭터가 몬스터한테서 도망칠 경우 데미지 받고 도망
								toGoR=j;
								if(m.getFloor()==0) room[0][j].setVisit(true);
								else room[floor][j].setVisit(true);
							}
						}
					}
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}