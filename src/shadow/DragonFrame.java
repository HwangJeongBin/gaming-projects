package shadow;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DragonFrame{
	private Container contentPane;
	private MyPanel panel;
	private int W, H, cnt=0, sleepTime, sleepCnt=0;
	private MainCharacter m;
	private JFrame frame;
	private MyThread t = new MyThread();
	private boolean leftFlag=false, rightFlag=false, getNumFlag=true, sleepFlag=true, chracterMoveFlag=false;
	private Image wakeDragon, sleepDragon;
	
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
        t.start();
	}
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            W = contentPane.getWidth();
    		H = contentPane.getHeight();
            ImageIcon icon = new ImageIcon("leftRoom.png");
            wakeDragon = icon.getImage();
            icon = new ImageIcon("sleepingDragon.png");
            sleepDragon = icon.getImage();
            if(sleepFlag) g.drawImage(sleepDragon,0,0,W,H,this);
            else g.drawImage(wakeDragon,0,0,W,H,this);
            g.drawImage(m.getImage(),m.getX(),m.getY(),m.getW(),m.getH(),this);
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
			    			if(cnt==2) m.moveUp(50);
			    			m.setH(100);	// 쭈그리는 자세로 수정
			    			m.moveLeft(2);
			    			if(cnt%2==0) m.lookBehind(true);
			    			else m.lookBehind(false);
			    			chracterMoveFlag=true;
			    		}
			    	}
			    	else if(rightFlag) {
			    		if(cnt>1) {
			    			if(cnt==2) m.moveUp(50);
			    			m.setH(100);
			    			m.moveRight(2);
			    			if(cnt%2==0) m.lookFront(true);
			    			else m.lookFront(false);
			    			chracterMoveFlag=true;
			    		}
			    	}
			    	else {
			    		if(chracterMoveFlag) {
			    			m.moveDown(50);
			    			cnt++;
			    			chracterMoveFlag=false;
			    			m.lookFront(false);
			    		}
			    		m.setH(40);	// 쭈그리는 자세로 수정
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
