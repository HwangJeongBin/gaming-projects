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
	private int W, H, page=1;
	private JFrame frame;
	private Image background;
	private JButton backToMenu, leftPointer, rightPointer;
	ImageIcon icon;
	
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
		ImageIcon img = new ImageIcon("howToPlayMenu.png");
		ImageIcon img2 = new ImageIcon("howToPlayMenu2.png");
		backToMenu = new JButton(img);
		backToMenu.setRolloverIcon(img2);
		backToMenu.setBounds(W-240,H-150,200,100);
		backToMenu.setBorderPainted(false);
		backToMenu.setFocusPainted(false);
		backToMenu.setContentAreaFilled(false);
		backToMenu.addActionListener(event -> {
			frame.dispose();
			new GameMenu();
		});
		img = new ImageIcon("rightPointer.png");
		img2 = new ImageIcon("rightPointer2.png");
		rightPointer = new JButton(img);
		rightPointer.setRolloverIcon(img2);
		rightPointer.setBounds(W-150,H/2,160,80);
		rightPointer.setBorderPainted(false);
		rightPointer.setFocusPainted(false);
		rightPointer.setContentAreaFilled(false);
		rightPointer.addActionListener(event -> {
			page++;
			panel.repaint();
		});
		img = new ImageIcon("leftPointer.png");
		img2 = new ImageIcon("leftPointer2.png");
		leftPointer = new JButton(img);
		leftPointer.setRolloverIcon(img2);
		leftPointer.setBounds(0,H/2,160,80);
		leftPointer.setBorderPainted(false);
		leftPointer.setFocusPainted(false);
		leftPointer.setContentAreaFilled(false);
		leftPointer.addActionListener(event -> {
			page--;
			panel.repaint();
		});
		panel.add(backToMenu);
		panel.add(rightPointer);
		panel.add(leftPointer);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
        frame.setResizable(false);
	}
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            switch(page) {
            case 1:
            	icon = new ImageIcon("gameStory.png");
            	leftPointer.setEnabled(false);
            	leftPointer.setVisible(false);
            	break;
            case 2:
            	icon = new ImageIcon("howToPlay1.png");
            	leftPointer.setEnabled(true);
            	leftPointer.setVisible(true);
            	rightPointer.setEnabled(true);
            	rightPointer.setVisible(true);
            	break;
            case 3:
            	icon = new ImageIcon("howToPlay2.png");
            	rightPointer.setEnabled(false);
            	rightPointer.setVisible(false);
            	break;
            }
            background = icon.getImage();
            g.drawImage(background,0,0,W,H,this);
        }
	}
}