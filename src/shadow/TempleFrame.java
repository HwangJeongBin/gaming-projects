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
	private int y=0, W,H, moveX, moveY, cnt=0, floor=1, top=15, moveFloor, sleepTime=70, damageY=0, damagedHP=0, gameOverH=0, toGoF=0, toGoR=0, skillTime;	// floor은 화면의 층이지 캐릭터의 현재 층이 아님
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
	private Image sandglass;
	private int itemN[] = {11,12,13,41,42};
	
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
        	int monN=i%4;
        	if(monN==2&&mf<=top/2) {	// 난이도 조절을 위한 것
        		i--;
        		continue;
        	}
        	if(mon[mf][mr].isExist()||room[mf][mr].isDownStair()||(mf==top&&mr==2)||(mf==1&&mr==0)) i--;	// 몬스터 세팅이 중복되지 않고 덩쿨이나 포탈 및 스폰 위치에 몬스터가 있지 않게 처리
        	else {
        		mon[mf][mr].setExist(true);
        		mon[mf][mr].setMonN(monN);
        	}
        }
        //mon[1][1].setExist(true);
        //mon[1][1].setMonN(1);
        //mon[2][2].setExist(true);
        //mon[2][2].setMonN(1);
        for(int i=0;i<3*top/4;i++) {
        	int f = (int)(Math.random()*top+1);
        	int r = (int)(Math.random()*3);
        	if(item[f][r].isExist()||(f==1&&r==0)) {
        		i--;
        		continue;
        	}
        	if(i==0) f=top/5;
        	if(i==1) f=2*top/5;
        	if(i==2) f=3*top/5;
        	if(i==3) f=top/2;
        	if(i==4) f=4*top/5;
        	item[f][r].setExist(true);
        	if(i<5) item[f][r].setItemN(itemN[i]);
        	else if(i%4==0) item[f][r].setItemN(3);
        	else item[f][r].setItemN(2);
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
			    			m.decSkillTurn();
			    			monAttack=false;
			    			if(!(moveXFlag||moveYFlag)) mainTurn=true;	// 도망갈 때 맞는 경우에는 턴을 시작하지 않음
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
			    			if(!mon[m.getFloor()][m.getRoomN()].isExist()&&item[m.getFloor()][m.getRoomN()].isExist()) item[m.getFloor()][m.getRoomN()].itemOpen(true);
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
			    			if(!mon[m.getFloor()][m.getRoomN()].isExist()&&item[m.getFloor()][m.getRoomN()].isExist()) item[m.getFloor()][m.getRoomN()].itemOpen(true);
			    			if(mon[m.getFloor()][m.getRoomN()].isExist()) monAttack=true;
			    			if(m.getFloor()==top&&m.getRoomN()==2) {
			    				frame.dispose();
					    		DragonFrame dragon = new DragonFrame(frame.getX(),frame.getY(),W,H);
					    	}
			    		}
			    	}
			    	if(m.getHp()<=0) {
			    		m.setY(m.getY()+10);
			    		m.setH(m.getH()-10);
		            	gameOverH+=50;
		            	if(gameOverH>H) gameOverH=H;
		            }
			    	panel.repaint();
			        Thread.sleep(sleepTime);	// 스레드 대기
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
            icon = new ImageIcon("sandglass.png");
            sandglass = icon.getImage();
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
            		if(mon[floor+1-i][j].isExist()&&room[floor+1-i][j].isVisit()) {
            			g.drawImage(mon[floor+1-i][j].getImage(), j*W/3+2*W/11+mon[floor+1-i][j].getX(), (i+1)*H/3-mon[floor+1-i][j].getH()+y+mon[floor+1-i][j].getY(), mon[floor+1-i][j].getW(), mon[floor+1-i][j].getH(), this);
            			g.setColor(new Color(0xD1180B));
            			g.fillRoundRect(j*W/3+2*W/11+mon[floor+1-i][j].getX(), (i+1)*H/3-mon[floor+1-i][j].getH()+y+mon[floor+1-i][j].getY()-20, mon[floor+1-i][j].getHp()*20, 15, 10, 10);
            		}
            		if(item[floor+1-i][j].isExist()&&room[floor+1-i][j].isVisit()) {
            			g.drawImage(item[floor+1-i][j].getImage(), j*W/3+W/4, (i+1)*H/3-item[floor+1-i][j].getH()+y, item[floor+1-i][j].getW(), item[floor+1-i][j].getH(), this);
            			if(item[floor+1-i][j].isItemOpen()) {
            				if(item[floor+1-i][j].getOpenTime()<40) {
                				g.drawImage(item[floor+1-i][j].getItemImage(), j*W/3+W/4, (i+1)*H/3-item[floor+1-i][j].getH()+y, 40, 40, this);
                				item[floor+1-i][j].incOpenTime();
                			}
            				else {
        						switch(item[floor+1-i][j].getItemN()) {
        						case 11:
        							m.getSkill(1);
        							break;
        						case 12:
        							m.getSkill(2);
        							break;
        						case 13:
        							m.getSkill(3);
        					        break;
        						case 2:
        							m.incHp();
        					        break;
        						case 3:
        							m.incCoolDown();
        					        break;
        						case 41:
        							m.incAttack();
        					        break;
        						case 42:
        							m.incDefense();
        					        break;
        						}
        						item[floor+1-i][j].setExist(false);
        					}
            			}
            		}
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
            			if(mon[floor+2][j].isExist()&&room[floor+2][j].isVisit()) {
            				g.drawImage(mon[floor+2][j].getImage(), j*W/3+2*W/11, y-mon[floor+2][j].getH(), mon[floor+2][j].getW(), mon[floor+2][j].getH(), this);
            				g.setColor(new Color(0xD1180B));
            				g.fillRoundRect(j*W/3+2*W/11, y-mon[floor+2][j].getH()-20, mon[floor+2][j].getHp()*20, 15, 10, 10);
            			}
            			if(item[floor+2][j].isExist()&&room[floor+2][j].isVisit()) {
            				g.drawImage(item[floor+2][j].getImage(), j*W/3+W/4, y-item[floor+2][j].getH(), item[floor+2][j].getW(), item[floor+2][j].getH(), this);
            				if(item[floor+2][j].isItemOpen()) {
                				if(item[floor+2][j].getOpenTime()<40) {
                    				g.drawImage(item[floor+2][j].getItemImage(), j*W/3+W/4, y-item[floor+2][j].getH(), 40, 40, this);
                    				item[floor+2][j].incOpenTime();
                    			}
                				else {
            						switch(item[floor+2][j].getItemN()) {
            						case 11:
            							m.getSkill(1);
            							break;
            						case 12:
            							m.getSkill(2);
            							break;
            						case 13:
            							m.getSkill(3);
            					        break;
            						case 2:
            							m.incHp();
            					        break;
            						case 3:
            							m.incCoolDown();
            					        break;
            						case 41:
            							m.incAttack();
            					        break;
            						case 42:
            							m.incDefense();
            					        break;
            						}
            						item[floor+2][j].setExist(false);
            					}
            				}
            			}
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
        			if(mon[floor-2][j].isExist()&&room[floor-2][j].isVisit()) {
        				g.drawImage(mon[floor-2][j].getImage(), j*W/3+2*W/11, y+4*H/3-mon[floor-2][j].getH(), mon[floor-2][j].getW(), mon[floor-2][j].getH(), this);
        				g.setColor(new Color(0xD1180B));
        				g.fillRoundRect(j*W/3+2*W/11, y+4*H/3-mon[floor-2][j].getH()-20, mon[floor-2][j].getHp()*20, 15, 10, 10);
        			}
        			if(item[floor-2][j].isExist()&&room[floor-2][j].isVisit()) {
        				g.drawImage(item[floor-2][j].getImage(), j*W/3+W/4, y+4*H/3-item[floor-2][j].getH(), item[floor-2][j].getW(), item[floor-2][j].getH(), this);
        				if(item[floor-2][j].isItemOpen()) {
        					if(item[floor-2][j].getOpenTime()<40) {
        						g.drawImage(item[floor-2][j].getItemImage(), j*W/3+W/4, y+4*H/3-item[floor-2][j].getH(), 40, 40, this);
                				item[floor-2][j].incOpenTime();
                			}
        					else {
        						switch(item[floor-2][j].getItemN()) {
        						case 11:
        							m.getSkill(1);
        							break;
        						case 12:
        							m.getSkill(2);
        							break;
        						case 13:
        							m.getSkill(3);
        					        break;
        						case 2:
        							m.incHp();
        					        break;
        						case 3:
        							m.incCoolDown();
        					        break;
        						case 41:
        							m.incAttack();
        					        break;
        						case 42:
        							m.incDefense();
        					        break;
        						}
        						item[floor-2][j].setExist(false);
        					}
        				}	
        			}
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
            for(int i=0;i<m.getCoolDown();i++) {
            	System.out.println("sandglass " + m.getCoolDown());
            	g.drawImage(sandglass, 25*i+W-150, 10, 20, 30, this);
            }
            //g.setFont(new Font("monospaced",Font.BOLD,22));
            g.setColor(Color.WHITE);
            g.drawImage(attackIcon, 10, 40, 25, 25, this);
            g.drawString(Integer.toString(m.getAttack()), 40, 60);
            g.drawImage(defenseIcon, 65, 40, 25, 25, this);
            g.drawString(Integer.toString(m.getDefense()), 95, 60);
            if(mainTurn) {
            	int s = m.getSkillN(), cnt=0;
            	g.drawImage(skillFrame, m.getX()-40, m.getY()-100, 100*s-10, 100, this);
            	// 배열을 써서 몇번 스킬을 먼저 출력할지를 확인 / 배열을 통해 스킬 넘버 받은 후에 배열 길이 만큼 for문
            	for(int i=0;i<4;i++) {
            		if(m.haveSkill(i)) {
            			m.setSkillX(m.getX()-30+85*cnt,i);
            			m.setSkillY(m.getY()-100,i);
            			g.drawImage(skill[i], m.getSkillX(i), m.getSkillY(i), 80, 100, this);
            			if(m.getSkillTurn(i)>0) g.drawString(Integer.toString(m.getSkillTurn(i)), m.getSkillX(i)+35, m.getSkillY(i)-10);
            			cnt++;
            		}
            	}
            }
            for(int i=0;i<4;i++) {
            	if(m.isUsingSkill(i)) {
            		int skillW = 250;
            		if(i==3) skillW=300;
            		g.drawImage(skill[i], m.getX()+W/17, m.getY()-50, skillW, 250, this);
            		if(skillTime%10>0&&skillTime%10<5) mon[m.getFloor()][m.getRoomN()].setDamaged(true);
            		else mon[m.getFloor()][m.getRoomN()].setDamaged(false);
            		skillTime++;
            		if(skillTime>50) {
            			m.setUsingSkill(false, i);
            			skillTime=0;
            			mon[m.getFloor()][m.getRoomN()].damaged(m.getSkillDamgae(i));
            			mon[m.getFloor()][m.getRoomN()].setDamaged(false);
            			if(mon[m.getFloor()][m.getRoomN()].getHp()<=0) {	// 몬스터의 체력이 0이 되면
            				if(item[m.getFloor()][m.getRoomN()].isExist()) item[m.getFloor()][m.getRoomN()].itemOpen(true);
            				mon[m.getFloor()][m.getRoomN()].setExist(false);	// 몬스터는 존재하지 않는다
            				mainTurn=false;
            			}
            			else monAttack=true;	// 몬스터가 살아 있다면 공격
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
			if(mainTurn&&!monAttack) {
				if(new Rectangle(W-150, 10, 25*m.getCoolDown()+10, 30).contains(x,y)) {
					m.useCoolDown();
				}
				for(int i=0;i<4;i++) {
					if(m.haveSkill(i)) {
						if(!(m.isUsingSkill(i))&& new Rectangle(m.getSkillX(i), m.getSkillY(i), 80, 100).contains(x,y)) {
							m.setUsingSkill(true, i);
							return;
						}
					}
				}
			}
			if((!moveXFlag)&&(!moveYFlag)&&(!monAttack)&&(!mainAttack)&&skillTime==0) {
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
									mainTurn=false;	// 이동하는 룸에 몬스터가 없으면 메인 캐릭터의 공격 턴을 종료
								}
							}
							else if((i==1||(m.getFloor()==0&&i==2))&&(m.getRoomN()-j>=-1&&m.getRoomN()-j<=1)) {	// 옆 방으로 이동
								if(j<m.getRoomN()) moveBack=true;
								moveXFlag = true;
								moveX = ((j-m.getRoomN())*W/3);
								if(mon[m.getFloor()][m.getRoomN()].isExist()) monAttack=true;	// 캐릭터가 몬스터한테서 도망칠 경우 데미지 받고 도망
								mainTurn=false;	// 이동하는 룸에 몬스터가 없으면 메인 캐릭터의 공격 턴을 종료
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