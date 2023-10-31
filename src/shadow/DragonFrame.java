package shadow;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DragonFrame{
	private Container contentPane;
	private MyPanel panel;
	private int W, H, cnt=0, sleepTime, sleepCnt=0,meteoCnt=0,meteoX,meteoW=60,meteoH=120,gameOverCnt=0;
	private MainCharacter m;
	private JFrame frame;
	private MyThread t = new MyThread();
	private boolean leftFlag=false, rightFlag=false, getNumFlag=true, sleepFlag=true, chracterMoveFlag=false, meteoFlag=false, gameOverFlag=false;
	private Image wakeDragon, sleepDragon, meteo, suprised, flyingDragon, runningDust, forest, replay, menu;
	
	DragonFrame() {
		frame = new JFrame();
		frame.setTitle("Shadow");
		frame.setSize(1150,650);
		frame.setVisible(true);
		contentPane = frame.getContentPane();
		W = contentPane.getWidth();
		H = contentPane.getHeight();
		m = new MainCharacter(W,H);
		panel = new MyPanel();
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
        frame.addKeyListener(new MyKeyListener());
        panel.addMouseListener(new MyMouseListener());
        frame.setResizable(false);
        m.setDragonPos();
        ImageIcon icon = new ImageIcon("staringDragon.png");
        wakeDragon = icon.getImage();
        icon = new ImageIcon("sleepingDragon.png");
        sleepDragon = icon.getImage();
        icon = new ImageIcon("meteo.png");
        meteo = icon.getImage();
        icon = new ImageIcon("suprised.png");
        suprised = icon.getImage();
        icon = new ImageIcon("flyingDragon.gif");
        flyingDragon = icon.getImage();
        icon = new ImageIcon("runningDust.gif");
        runningDust = icon.getImage();
        icon = new ImageIcon("forest.png");
        forest = icon.getImage();
        icon = new ImageIcon("replay.png");
        replay = icon.getImage();
        icon = new ImageIcon("menu.png");
        menu = icon.getImage();
        t.start();
	}
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(gameOverFlag&&gameOverCnt>40) {
            	g.drawImage(forest,0,0,W,H,this);
            	g.drawImage(m.getImage(),W/4,H-m.getH(),m.getW(),m.getH(),this);
            	g.drawImage(runningDust,W/4+m.getW()/2,H-100,200,m.getH()*2/3,this);
            	g.drawImage(flyingDragon,W/2,0,W/2,H,this);
            	if(gameOverCnt>60) {
            		g.drawImage(replay,W/6,200,150,40,this);
            		g.drawImage(menu,W/6,280,150,40,this);
            	}
            	return;
            }
            W = contentPane.getWidth();
    		H = contentPane.getHeight();
            if(sleepFlag) g.drawImage(sleepDragon,0,0,W,H,this);
            else g.drawImage(wakeDragon,0,0,W,H,this);
            g.drawImage(m.getImage(),m.getX(),m.getY(),m.getW(),m.getH(),this);
            if(meteoFlag) {
            	g.drawImage(meteo,meteoX,meteoCnt*H/40,meteoW,meteoH,this);
            }
            if(gameOverFlag) {
            	g.drawImage(suprised, m.getX()+m.getW(), m.getY()-50, 50, 100, this);
            }
        }
	}
	public class MyThread extends Thread
	{
		public void run()
		{
			// 인터럽트 됬을때 예외처리
			try
			{
			    while(true) {
			    	if(getNumFlag) {
			    		sleepTime=(int)Math.random()*10+50;
			    		getNumFlag=false;
			    	}
			    	if(sleepTime/2==sleepCnt) {
			    		if((int)(Math.random()*2)==0) {
			    			meteoFlag=true;
			    			meteoX=m.getX()+20;
			    			meteoCnt=0;
			    		}
			    	}
			    	if(meteoCnt>45) meteoFlag=false;
			    	if(meteoFlag) {
			    		if(m.getX() + m.getW() >= meteoX+20 && m.getX() <= meteoX+20 + meteoW && m.getY() + m.getH() >= meteoCnt*H/40 && m.getY() <= meteoCnt*H/40 + meteoH - 20) gameOverFlag = true;	// 오브젝트끼리 겹치는지 확인
			    		meteoCnt++;
			    	}
			    	if(sleepCnt>sleepTime) {
			    		sleepFlag=false;
			    	}
			    	if(sleepCnt>sleepTime+4) {
			    		if(leftFlag||rightFlag) gameOverFlag = true;
			    	}
			    	if(sleepCnt>sleepTime+30 && !gameOverFlag) {
		    			sleepFlag=true;	// 화면 전환
		    			getNumFlag=true;
		    			sleepCnt=0;
		    		}
			    	if(gameOverFlag) {
			    		
			    		gameOverCnt++;
			    		if(gameOverCnt>40) {
			    			if(gameOverCnt%2==0)m.lookBehind(false);
			    			else m.lookBehind(true);
			    		}
			    		else {
			    			m.lookFront(false);
			    		}
			    	}
			    	else if(leftFlag) {
			    		if(cnt>1) {
			    			//m.hide(false);	// 쭈그리는 자세로 수정
			    			m.moveLeft(2);
			    			if(cnt%2==0) m.lookBehind(true);
			    			else m.lookBehind(false);
			    			chracterMoveFlag=true;
			    		}
			    	}
			    	else if(rightFlag) {
			    		if(cnt>1) {
			    			//m.hide(false);
			    			m.moveRight(2);
			    			if(cnt%2==0) m.lookFront(true);
			    			else m.lookFront(false);
			    			chracterMoveFlag=true;
			    		}
			    	}
			    	else {
			    		if(chracterMoveFlag) {
			    			cnt++;
			    			chracterMoveFlag=false;
			    			m.lookFront(false);
			    		}
			    		m.hide();	// 쭈그리는 자세로 수정
			    		cnt=0;
			    	}
			    	if(m.getX()>W-100) {
			    		frame.dispose();
			    		m.setX(0);
			    		new TempleFrame();
			    	}
			    	sleepCnt++;
			    	cnt++;
			    	panel.repaint();
			        Thread.sleep(100);	// 스레드 0.5초동안 대기
			    }
			}catch(InterruptedException e)
			{
			    System.out.println(e);
			}
		}
	}
	class MyKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode()==37) {	// 왼쪽 방향키
				leftFlag=true;
			}
			if(e.getKeyCode()==39) {	// 오른쪽 방향키
				rightFlag=true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==37||e.getKeyCode()==39) {	// 왼쪽 혹은 오른쪽 방향키
				leftFlag=false;
				rightFlag=false;
			}
		}
	}
	private class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			int x = e.getX();
			int y = e.getY();
			if(gameOverFlag&&gameOverCnt>60) {
				if(new Rectangle(W/6,200,150,40).contains(x, y)) {
					m.setDragonPos();
					gameOverFlag=false;
					gameOverCnt=0;
				}
				else if(new Rectangle(W/6,280,150,40).contains(x, y)) {
					frame.dispose();
					new GameMenu();
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