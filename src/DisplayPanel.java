import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplayPanel extends JPanel implements MouseListener, KeyListener {
    private boolean rightPressed = false;
    private boolean leftPressed = false;
    private boolean jumpPressed = false; // Track jump key state

    // Horizontal Physics
    private double velX = 0;
    private final double ACCELERATION = 0.8;
    private final double FRICTION = 0.85;
    private final double MAX_SPEED = 5.0;

    // Vertical Physics constants
    private double velY = 0;
    private final double GRAVITY = 0.6;      // Pulls Mario down every frame
    private final double JUMP_FORCE = -13.0;  // Upward burst (negative moves UP in Java 2D)
    private final int FLOOR_Y = 435;         // The starting Y coordinate
    private boolean isGrounded = true;       // True if Mario is on the floor

    private int score;
    private boolean yellowColor;
    private int marioX;
    private int marioY;

    private BufferedImage background;
    private BufferedImage currentMario;
    private BufferedImage marioLeft;
    private BufferedImage marioRight;

    private Timer timer = new Timer(16, e -> { // ~60 FPS
        // --- HORIZONTAL MOVEMENT ---
        if (leftPressed) {
            velX -= ACCELERATION;
        }
        if (rightPressed) {
            velX += ACCELERATION;
        }

        if (!leftPressed && !rightPressed) {
            velX *= FRICTION;
            if (Math.abs(velX) < 0.1) {
                velX = 0;
            }
        }

        if (velX > MAX_SPEED) velX = MAX_SPEED;
        if (velX < -MAX_SPEED) velX = -MAX_SPEED;

        // --- VERTICAL MOVEMENT (JUMPING & GRAVITY) ---
        if (jumpPressed && isGrounded) {
            velY = JUMP_FORCE; // Apply instant upward thrust
            isGrounded = false; // Mario leaves the ground
        }

        // Apply constant gravity pulling down if in mid-air
        if (!isGrounded) {
            velY += GRAVITY;
        }

        // --- POSITION UPDATES ---
        marioX += velX;
        marioY += velY; // Move vertically

        // --- GROUND COLLISION ---
        if (marioY >= FLOOR_Y) {
            marioY = FLOOR_Y; // Snap to floor so he doesn't sink
            velY = 0;         // Stop falling downward
            isGrounded = true; // He is back on solid ground
        }

        repaint();
    });

    public DisplayPanel() {
        score = 0;
        yellowColor = true;
        marioX = 50;
        marioY = FLOOR_Y; // Match initial floor height

        try {
            background = ImageIO.read(new File("src/background.png"));
            marioLeft = ImageIO.read(new File("src/marioleft.png"));
            marioRight = ImageIO.read(new File("src/marioright.png"));
            currentMario = marioRight;
        } catch (IOException e) {
            System.out.println("Error loading images: " + e.getMessage());
        }

        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, -marioX, 0, null);
        g.drawImage(currentMario, 100, ((int) marioY), null); // Cast Y to int for rendering

        g.setFont(new Font("Arial", Font.BOLD, 16));
        if (yellowColor) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawString("Score: " + score, 50, 30);
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            yellowColor = !yellowColor;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_A) {
            leftPressed = true;
            currentMario = marioLeft;
        }
        if (keyCode == KeyEvent.VK_D) {
            rightPressed = true;
            currentMario = marioRight;
        }
        // Jump when hitting the Spacebar or W key
        if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_W) {
            jumpPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_A) leftPressed = false;
        if (keyCode == KeyEvent.VK_D) rightPressed = false;
        if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_W) {
            jumpPressed = false;
        }
    }
}
