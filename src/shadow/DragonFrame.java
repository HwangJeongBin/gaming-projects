package shadow;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DragonFrame{
	private Container contentPane;
	private MyPanel panel;
	private int W, H, cnt=0, sleepTime, sleepCnt=0,meteoCnt=0,meteoX,meteoW=60,meteoH=120;
	private MainCharacter m;
	private JFrame frame;
	private MyThread t = new MyThread();
	private boolean leftFlag=false, rightFlag=false, getNumFlag=true, sleepFlag=true, chracterMoveFlag=false, meteoFlag=false;
	private Image wakeDragon, sleepDragon, meteo;
	
	DragonFrame(int X, int Y,int W,int H) {
		frame = new JFrame();
		frame.setTitle("Shadow");
		frame.setBounds(X,Y,W,H);
		frame.setVisible(true);
		contentPane = frame.getContentPane();
		m = new MainCharacter(W,H);
		panel = new MyPanel();
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
        frame.addKeyListener(new MyKeyListener());
        frame.setResizable(false);
        W = contentPane.getWidth();
		H = contentPane.getHeight();
        m.setDragonPos();
        ImageIcon icon = new ImageIcon("staringDragon.png");
        wakeDragon = icon.getImage();
        icon = new ImageIcon("sleepingDragon.png");
        sleepDragon = icon.getImage();
        icon = new ImageIcon("meteo.png");
        meteo = icon.getImage();
        t.start();
	}
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            W = contentPane.getWidth();
    		H = contentPane.getHeight();
            if(sleepFlag) g.drawImage(sleepDragon,0,0,W,H,this);
            else g.drawImage(wakeDragon,0,0,W,H,this);
            g.drawImage(m.getImage(),m.getX(),m.getY(),m.getW(),m.getH(),this);
            if(meteoFlag) {
            	g.drawImage(meteo,meteoX,meteoCnt*H/40,meteoW,meteoH,this);
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
			    		if(m.getX() + m.getW() >= meteoX+20 && m.getX() <= meteoX+20 + meteoW && m.getY() + m.getH() >= meteoCnt*H/40 && m.getY() <= meteoCnt*H/40 + meteoH - 20) System.exit(0);	// 사각형 겹치는지 확인
			    		meteoCnt++;
			    	}
			    	if(sleepCnt>sleepTime) {
			    		sleepFlag=false;
			    	}
			    	if(sleepCnt>sleepTime+4) {
			    		if(leftFlag||rightFlag) System.exit(0);
			    	}
			    	if(sleepCnt>sleepTime+30) {
		    			sleepFlag=true;	// 화면 전환
		    			getNumFlag=true;
		    			sleepCnt=0;
		    		}
			    	if(leftFlag) {
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
}