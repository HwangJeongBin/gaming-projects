package shadow;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import shadow.GameMenu.MyPanel;

public class HowToPlay {
	private Container contentPane;
	private MyPanel panel;
	private int W, H;
	private JFrame frame;
	private Image background;
	private JButton backToMenu;
	
	HowToPlay() {
		frame = new JFrame();
		frame.setTitle("Shadow");
		frame.setSize(1100,700);
		frame.setVisible(true);
		contentPane = frame.getContentPane();
		W = contentPane.getWidth();
		H = contentPane.getHeight();
		//frame.setLocationRelativeTo(null);
		//frame.setLayout(null);
		panel = new MyPanel();
		panel.setLayout(null);
		frame.add(panel);
		backToMenu = new JButton("메뉴로");
		backToMenu.setBounds(W-100,H-50,80,20);
		backToMenu.addActionListener(event -> {
			frame.dispose();
			new GameMenu();
		});
		panel.add(backToMenu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
        frame.setResizable(false);
	}
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon icon = new ImageIcon("HowToPlay.png");
            background = icon.getImage();
            g.drawImage(background,0,0,W,H,this);
        }
	}
}
