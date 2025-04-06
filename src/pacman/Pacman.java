package pacman;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.ImageIcon;


public class Pacman implements KeyListener{

	private JFrame frame;
	private DrawingPanel tablero;
	private int x = 205, y = 250;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pacman window = new Pacman();
					window.frame.setVisible(true);
					ImageIcon pac_man = new ImageIcon("img/icono.jpg");
					Image icono2 = pac_man.getImage();
					window.frame.setIconImage(icono2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Pacman() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		JPanel pnl_norte = new JPanel();
		pnl_norte.setBackground(new Color(0, 0, 128));
		frame.getContentPane().add(pnl_norte, BorderLayout.NORTH);
		pnl_norte.setLayout(null);
		pnl_norte.setLayout(new GridLayout(1, 0, 0, 0));
		pnl_norte.setBorder(BorderFactory.createLineBorder(Color.YELLOW,4));
		
		JLabel titulo = new JLabel();
		titulo.setHorizontalAlignment(JLabel.CENTER);
		ImageIcon logo = new ImageIcon("img/logo.png");
		Image img = logo.getImage().getScaledInstance(300, 80, Image.SCALE_SMOOTH);
		ImageIcon icono = new ImageIcon(img);
		titulo.setIcon(icono);
		titulo.setForeground(new Color(255, 255, 0));
		titulo.setFont(new Font("Dialog", Font.BOLD, 18));
		pnl_norte.add(titulo);
		
		JPanel footer = new JPanel();
		footer.setBackground(new Color(0, 0, 160));
		frame.getContentPane().add(footer, BorderLayout.SOUTH);
		footer.setLayout(new GridLayout(1, 2, 20, 20));
		footer.setBorder(BorderFactory.createLineBorder(Color.BLACK,4));
		
		tablero = new DrawingPanel();
		tablero.setBackground(Color.BLACK);
		frame.getContentPane().add(tablero, BorderLayout.CENTER);
		tablero.setLayout(new GridLayout(1, 2, 20, 20));
		tablero.setBorder(BorderFactory.createLineBorder(Color.BLACK,4));
		
		tablero.addKeyListener(this);
		tablero.setFocusable(true);
		
		JButton reiniciar = new JButton("REINICIAR");
		reiniciar.setForeground(new Color(255, 255, 0));
		reiniciar.setBackground(new Color(0, 0, 160));
		reiniciar.setBorder(BorderFactory.createLineBorder(Color.YELLOW,2));
		reiniciar.setFont(new Font("Dialog", Font.BOLD, 18));
		reiniciar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				x = 205;
				y = 250;
				
				tablero.repaint();
				tablero.requestFocus();
				
			}
		});
		footer.add(reiniciar);
		
	}
	   class DrawingPanel extends JPanel {
		   private Image fondo;
	        public DrawingPanel() {
	            setBackground(Color.WHITE);
	            fondo = new ImageIcon("img/fondo.jpg").getImage();
	        }
	        
	        

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            Graphics2D g2d = (Graphics2D) g;
	            
	            if(fondo != null) {
	            	g2d.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
	            }
	            g2d.setColor(Color.yellow);
	            g2d.fillOval(x,y, 30, 30);
	            
	        }
	    }
	@Override
	public void keyTyped(KeyEvent e) {
//		System.out.println(e.getKeyCode());
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println(y);
		if(e.getKeyCode() == 87) { //TECLA W
			y-=5;
			//LIMITE DE ARRIBA
			if (y <= -30) {
				y =+ 585 ;
			}
		}
		if(e.getKeyCode() == 83) { //TECLA S
			y+=5;
			//LIMITE DE ABAJO
			if (y >= 585) {
				y =+ -35;
			}
		}
		if(e.getKeyCode() == 65) { //TECLA A
			x-=5;
			//LIMITE DE LA IZQUIERDA
			if (x <= -30) {
				x =+ 445;
			}

		}
		if(e.getKeyCode() == 68) { //TECLA D
			x+=5;
			//LIMITE DE LA DERECHA
			if(x >= 445) {
				x =-25;
			}
		}
		tablero.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
