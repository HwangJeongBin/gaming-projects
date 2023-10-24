package shadow;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import shadow.DragonFrame.MyKeyListener;
import shadow.DragonFrame.MyPanel;
import shadow.DragonFrame.MyThread;

public class GameMenu {
	private Container contentPane;
	private MyPanel panel;
	private int W, H;
	private JFrame frame;
	private Image background;
	private JButton start, exit, howToPlay;
	
	GameMenu() {
		frame = new JFrame();
		frame.setTitle("Shadow");
		frame.setSize(1100,700);
		frame.setVisible(true);
		contentPane = frame.getContentPane();
		panel = new MyPanel();
		frame.add(panel);
		JButton start = new JButton("게임 시작");
		JButton howToPlay = new JButton("게임 방법");
		JButton exit = new JButton("게임 종료");
		panel.add(start);
		panel.add(howToPlay);
		panel.add(exit);
		start.addActionListener(event -> {
			frame.dispose();
			new TempleFrame();
		});
		howToPlay.addActionListener(event -> {
			frame.dispose();
			new HowToPlay();
		});
		exit.addActionListener(event -> System.exit(0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
        frame.setResizable(false);
        W = contentPane.getWidth();
		H = contentPane.getHeight();
	}
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon icon = new ImageIcon("sleepingDragon.png");
            background = icon.getImage();
            g.drawImage(background,0,0,W,H,this);
        }
	}
}
