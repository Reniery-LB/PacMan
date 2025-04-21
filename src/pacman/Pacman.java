package pacman;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Pacman implements KeyListener {

    private JFrame frame;
    private DrawingPanel tablero;
    private Player pacman;
    private java.util.List<Player> paredes = new ArrayList<>();
    private java.util.List<Point> puntos = new ArrayList<>();
    private int segundos = 0;
    private int puntaje = 0;
    private javax.swing.Timer timerTiempo;
    private boolean tiempoIniciado = false;
    private int lastPress = 0;
    private int velocidad = 5;
    private JLabel tiempo;
    private JLabel score;
    private javax.swing.Timer timerMovimiento;
    private final int ANCHO_MAPA = 600;
    private final int ALTO_MAPA = 780;

    private int[][] mapa = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,0,1,1,1,0,1,1,0,1,1,1,0,1,1,0,1},
            {1,0,1,1,0,1,1,1,0,1,1,0,1,1,1,0,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,1,0,1},
            {1,0,0,0,0,1,0,0,0,1,1,0,0,0,1,0,0,0,0,1},
            {1,1,1,1,0,1,1,1,0,1,1,0,1,1,1,0,1,1,1,1},
            {0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
            {1,1,1,1,0,1,0,1,1,0,0,1,1,0,1,0,1,1,1,1},
            {0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0},
            {1,1,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,1,1,1},
            {0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
            {1,1,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,0,1,1,1,0,1,1,0,1,1,1,0,1,1,0,1},
            {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1},
            {1,1,0,1,0,1,0,1,1,1,1,1,1,0,1,0,1,0,1,1},
            {1,0,0,0,0,1,0,0,0,1,1,0,0,0,1,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Pacman window = new Pacman();
                window.frame.setVisible(true);
                window.frame.setIconImage(new ImageIcon("img/icono.jpg").getImage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Pacman() {
        initialize();
        crearMapa();
        crearPuntos();
        ajustarPosicionInicial(); 
    }
    
    private void ajustarPosicionInicial() {
        int cellSize = 30;
        
        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                if (mapa[y][x] == 0) {
                    pacman.x = x * cellSize + cellSize/2 - pacman.w/2;
                    pacman.y = y * cellSize + cellSize/2 - pacman.h/2;
                    return;
                }
            }
        }
    }

    private void initialize() {
        frame = new JFrame("Pacman");
        frame.setSize(ANCHO_MAPA, ALTO_MAPA);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        pacman = new Player(270, 450, 24, 24, Color.YELLOW);

        JPanel pnl_norte = new JPanel(new BorderLayout());
        pnl_norte.setBackground(new Color(0, 0, 128));
        pnl_norte.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));

        JLabel titulo = new JLabel("PACMAN");
        titulo.setHorizontalAlignment(JLabel.CENTER);
        titulo.setForeground(Color.YELLOW);
        titulo.setFont(new Font("Dialog", Font.BOLD, 24));
        pnl_norte.add(titulo, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.setOpaque(false);
        
        tiempo = new JLabel("Tiempo: 0:00", JLabel.LEFT);
        tiempo.setForeground(Color.WHITE);
        tiempo.setFont(new Font("Dialog", Font.BOLD, 16));
        
        score = new JLabel("Puntos: 0", JLabel.RIGHT);
        score.setForeground(Color.WHITE);
        score.setFont(new Font("Dialog", Font.BOLD, 16));
        
        infoPanel.add(tiempo);
        infoPanel.add(score);
        pnl_norte.add(infoPanel, BorderLayout.SOUTH);

        frame.add(pnl_norte, BorderLayout.NORTH);

        tablero = new DrawingPanel();
        tablero.setBackground(Color.BLACK);
        tablero.addKeyListener(this);
        tablero.setFocusable(true);
        frame.add(tablero, BorderLayout.CENTER);

        JButton reiniciar = new JButton("REINICIAR");
        reiniciar.setForeground(Color.YELLOW);
        reiniciar.setBackground(new Color(0, 0, 160));
        reiniciar.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        reiniciar.setFont(new Font("Dialog", Font.BOLD, 18));
        reiniciar.addActionListener(e -> reiniciarJuego());
        frame.add(reiniciar, BorderLayout.SOUTH);

        timerTiempo = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                segundos++;
                tiempo.setText(String.format("Tiempo: %d:%02d", segundos / 60, segundos % 60));
            }
        });

        timerMovimiento = new javax.swing.Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                tablero.repaint();
            }
        });
    }

    private void crearMapa() {
        int cellSize = 30;
        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                if (mapa[y][x] == 1) {
                    paredes.add(new Player(x * cellSize, y * cellSize, cellSize, cellSize, Color.BLUE));
                }
            }
        }
    }

    private void crearPuntos() {
        int cellSize = 30;
        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                if (mapa[y][x] == 0) {
                    puntos.add(new Point(x * cellSize + cellSize/2, y * cellSize + cellSize/2));
                }
            }
        }
    }
    
    private Point encontrarPosicionValida() {
        int cellSize = 30;
        Random rand = new Random();
        ArrayList<Point> posicionesValidas = new ArrayList<>();

        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                if (mapa[y][x] == 0) {
                    posicionesValidas.add(new Point(
                        x * cellSize + cellSize/2 - 15, 
                        y * cellSize + cellSize/2 - 15
                    ));
                }
            }
        }

        if (!posicionesValidas.isEmpty()) {
            return posicionesValidas.get(rand.nextInt(posicionesValidas.size()));
        }
        
        return new Point(270, 450);
    }

    private void reiniciarJuego() {
        Point nuevaPosicion = encontrarPosicionValida();
        pacman.x = nuevaPosicion.x;
        pacman.y = nuevaPosicion.y;
        
        tiempoIniciado = false;
        segundos = 0;
        puntaje = 0;
        lastPress = 0;
        timerTiempo.stop();
        timerMovimiento.stop();
        tiempo.setText("Tiempo: 0:00");
        score.setText("Puntos: 0");
        puntos.clear();
        crearPuntos();
        tablero.repaint();
        tablero.requestFocus();
    }

    class Player {
        int x, y, w, h;
        Color c;

        public Player(int x, int y, int w, int h, Color c) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.c = c;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, w, h);
        }
    }

    class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.WHITE);
            for (Point punto : puntos) {
                g2d.fillOval(punto.x - 3, punto.y - 3, 6, 6);
            }

            for (Player pared : paredes) {
                g2d.setColor(pared.c);
                g2d.fillRect(pared.x, pared.y, pared.w, pared.h);
            }

            g2d.setColor(pacman.c);
            g2d.fillOval(pacman.x, pacman.y, pacman.w, pacman.h);
        }
    }

    private void update() {
        if (lastPress == 0) return;

        int newX = pacman.x;
        int newY = pacman.y;

        switch (lastPress) {
            case KeyEvent.VK_W: newY -= velocidad; break;
            case KeyEvent.VK_S: newY += velocidad; break;
            case KeyEvent.VK_A: newX -= velocidad; break;
            case KeyEvent.VK_D: newX += velocidad; break;
        }

        if (newX < 0) {
            newX = ANCHO_MAPA - pacman.w;
        } else if (newX > ANCHO_MAPA - pacman.w) {
            newX = 0;
        }

        Rectangle nuevoPacman = new Rectangle(newX, newY, pacman.w, pacman.h);
        boolean puedeMoverse = true;
        
        for (Player pared : paredes) {
            if (nuevoPacman.intersects(pared.getBounds())) {
                puedeMoverse = false;
                break;
            }
        }

        if (puedeMoverse) {
            pacman.x = newX;
            pacman.y = newY;
            
            
            Iterator<Point> iterator = puntos.iterator();
            while (iterator.hasNext()) {
                Point punto = iterator.next();
                if (nuevoPacman.contains(punto)) {
                    iterator.remove();
                    puntaje += 10;
                    score.setText("Puntos: " + puntaje);
                }
            }
            if (puntos.isEmpty()) {
            	timerMovimiento.stop();
            	timerTiempo.stop();
            	JOptionPane.showMessageDialog(frame, "¡Felicidades! Has recolectado todos los puntos.\n"
            			+ "Puntucación final: " + puntaje + "\n" +
            			"Tiempo: " + tiempo.getText(),
            			"¡Ganaste!",
            			JOptionPane.INFORMATION_MESSAGE);
            	reiniciarJuego();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_S || 
            keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D) {
            
            lastPress = keyCode;
            
            if (!tiempoIniciado) {
                tiempoIniciado = true;
                segundos = 0;
                timerTiempo.start();
            }
            
            if (!timerMovimiento.isRunning()) {
                timerMovimiento.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
   
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}