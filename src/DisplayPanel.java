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
    private int score;
    private boolean yellowColor;
    Player mario;
    Player luigi;

    private BufferedImage background;

    private Timer timer = new Timer(16, e -> { // ~60 FPS
        update();
    });

    public DisplayPanel() {
        mario = new Player("mario");
        luigi = new Player("luigi");
        score = 0;
        yellowColor = true;

        try {
            background = ImageIO.read(new File("src/background.png"));
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
        g.drawImage(background, 0, 0, null);
        mario.render(g);
        luigi.render(g);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        if (yellowColor) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawString("Score: " + score, 50, 30);
    }

    public void update() {
        mario.update();
        luigi.update();
        repaint();
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
        mario.updatePos(e.getPoint());
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_SPACE -> mario.Jump(true);
            case KeyEvent.VK_A -> mario.Left(true);
            case KeyEvent.VK_D -> mario.Right(true);
            case KeyEvent.VK_UP -> luigi.Jump(true);
            case KeyEvent.VK_LEFT -> luigi.Left(true);
            case KeyEvent.VK_RIGHT -> luigi.Right(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_SPACE -> mario.Jump(false);
            case KeyEvent.VK_A -> mario.Left(false);
            case KeyEvent.VK_D -> mario.Right(false);
            case KeyEvent.VK_UP -> luigi.Jump(false);
            case KeyEvent.VK_LEFT -> luigi.Left(false);
            case KeyEvent.VK_RIGHT -> luigi.Right(false);
        }
    }
}
