package pacman;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.ImageIcon;


public class Pacman implements KeyListener{

	private JFrame frame;
	private DrawingPanel tablero;
	private Player pacman;
	private List<Player> paredes = new ArrayList<>();
	Timer timer;
	private int lastPress = 0;
	private int velocidad = 3;
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
          
		pacman = new Player(200, 200, 30, 30, Color.yellow);
		
		paredes.add(new Player(120, 300, 200, 30, Color.orange));
		paredes.add(new Player(120, 50, 200, 30, Color.orange));
		paredes.add(new Player(380, 90, 30, 200, Color.magenta));

		JPanel pnl_norte = new JPanel();
		pnl_norte.setBackground(new Color(0, 0, 128));
		frame.getContentPane().add(pnl_norte, BorderLayout.NORTH);
		pnl_norte.setLayout(null);
		pnl_norte.setLayout(new BorderLayout(0,0));
		pnl_norte.setBorder(BorderFactory.createLineBorder(Color.YELLOW,4));
		
		JLabel titulo = new JLabel();
		titulo.setHorizontalAlignment(JLabel.RIGHT);
		ImageIcon logo = new ImageIcon("img/logo.png");
		Image img = logo.getImage().getScaledInstance(300, 80, Image.SCALE_SMOOTH);
		ImageIcon icono = new ImageIcon(img);
		titulo.setIcon(icono);
		titulo.setForeground(new Color(255, 255, 0));
		titulo.setFont(new Font("Dialog", Font.BOLD, 18));
		pnl_norte.add(titulo, BorderLayout.CENTER);
		
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
				pacman.x = 200;
				pacman.y = 200;
				
				tablero.repaint();
				tablero.requestFocus();
				
			}
		});
		footer.add(reiniciar);
		
		JPanel panelDer = new JPanel();
		panelDer.setBackground(new Color(255, 255, 0));
		frame.getContentPane().add(panelDer, BorderLayout.EAST);
		
		JPanel panelIzq = new JPanel();
		panelIzq.setBackground(new Color(255, 255, 0));
		frame.getContentPane().add(panelIzq, BorderLayout.WEST);
	
		timer = new Timer(10, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
//				tablero.repaint();
				
			}
		});
		timer.start();
		
		JLabel tiempo = new JLabel("0:00");
		tiempo.setHorizontalAlignment(JLabel.RIGHT);
		tiempo.setFont(new Font("Dialog", Font.BOLD, 32));
		tiempo.setForeground(Color.white);
		pnl_norte.add(tiempo, BorderLayout.LINE_END);
	}
	
   class DrawingPanel extends JPanel {
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
 
            g2d.setColor(pacman.c);
            g2d.fillOval(pacman.x, pacman.y, pacman.w, pacman.h);
            
            for(Player pared : paredes) {
            	g2d.setColor(pared.c);
            	g2d.fillRect(pared.x, pared.y, pared.w, pared.h);  	
            }
        }
    }
	
	class Player{
	   
	   int x,y,w,h;
	   Color c;
	   
	   public Player(int x,int y,int w, int h, Color c) {
		   this.x = x;
		   this.y = y;
		   this.w = w;
		   this.h = h;
		   this.c = c;
	   }
	   
	    public boolean colision(Player target) {
			
			if(this.x < target.x + target.w && 
					this.x + this.w > target.x &&
					this.y < target.y + target.h &&
					this.y + this.h > target.y) {
				
				return true;
			}	
			return false;
		}
	}
   
	public void update() {
		Boolean colision = false;
		
		for (Player pared : paredes) {
			if(pacman.colision(pared)) {
				colision = true;
			}
		}
		
		if(lastPress == 87) { //TECLA W
			if(!colision) {
				pacman.y -= velocidad;		
			}else {
				pacman.y += velocidad;
				lastPress = 0;
			}	
			//LIMITE DE ARRIBA
			if (pacman.y <= -30) {
				pacman.y = 585 ;
			}
		} 
	
		if(lastPress == 83) { //TECLA S
			if(!colision) {
				pacman.y += velocidad;							
			} else {
				pacman.y -= velocidad;
				lastPress = 0;
			}
			//LIMITE DE ABAJO
			if (pacman.y >= 585) {
				pacman.y = -35;
			}
		}
		
		if(lastPress == 65) { //TECLA A
			if(!colision) {
				pacman.x -= velocidad;							
			} else {
				
				lastPress = 0;
			}
			//LIMITE DE LA IZQUIERDA
			if (pacman.x <= -30) {
				pacman.x = 445;
			}
		}
			
		if(lastPress == 68) { //TECLA D
			if(!colision) {
				pacman.x += velocidad;				
			} else {
				
				lastPress = 0;
			}
			//LIMITE DE LA DERECHA
			if(pacman.x >= 445) {
				pacman.x = -25;
			}
		}
		tablero.repaint();
	}
	   
	@Override
	public void keyTyped(KeyEvent e) {
//		System.out.println(e.getKeyCode());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		lastPress = e.getKeyCode();
		timer.start();
		update();
		tablero.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
