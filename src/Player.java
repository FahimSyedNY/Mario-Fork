import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private boolean rightPressed = false;
    private boolean leftPressed = false;
    private boolean jumpPressed = false;

    // Horizontal Physics
    private double velX = 0;
    private final double ACCELERATION = 0.8;
    private final double FRICTION = 0.85;
    private double MAX_SPEED = 5.0;

    // Vertical Physics constants
    private double velY = 0;
    private final double GRAVITY = 0.6;
    private double JUMP_FORCE = -13.0;
    private final int FLOOR_Y = 435;
    private boolean isGrounded = true;

    private int xPos;
    private int Ypos;

    private BufferedImage currentPlayer;
    private BufferedImage playerLeft;
    private BufferedImage playerRight;

    public Player(String character) {
        xPos = 50;
        Ypos = FLOOR_Y;
        if (character.equals("luigi")) JUMP_FORCE = -16;
        if (character.equals("mario")) MAX_SPEED = 7;

        try {
            playerLeft = ImageIO.read(new File("src/" + character + "left.png"));
            playerRight = ImageIO.read(new File("src/" + character + "right.png"));
            currentPlayer = playerRight;
        } catch (IOException e) {
            System.out.println("Error loading images: " + e.getMessage());
        }
    }

    public void updatePos(Point p) {
        this.xPos = (int) p.getX();
        this.Ypos = (int) p.getY();
        isGrounded = false;
    }

    public void update() {
        // Horizontal Movement
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

        // Vertical Movement
        if (jumpPressed && isGrounded) {
            velY = JUMP_FORCE; // Apply instant upward thrust
            isGrounded = false; // Mario leaves the ground
        }

        if (!isGrounded) {
            velY += GRAVITY;
        }

        // Update Pos

        xPos += velX;
        Ypos += velY;

        // Ground Collision
        if (Ypos >= FLOOR_Y) {
            Ypos = FLOOR_Y; // Snap to floor
            velY = 0;
            isGrounded = true;
        }

        // Wall Collision

        if (xPos >= 920) {
            xPos = 920; // Snap to wall
            velX = 0;
        }
        if (xPos < 0) {
            xPos = 0;
            velX = 0;
        }
    }

    public void render(Graphics g) {
        g.drawImage(currentPlayer, xPos, ((int) Ypos), null);
    }

    // Inputs
    public void Jump(boolean isTrue) {
        jumpPressed = isTrue;
    }

    public void Right(boolean isTrue) {
        rightPressed = isTrue;
        if (isTrue) currentPlayer = playerRight;
    }

    public void Left(boolean isTrue) {
        leftPressed = isTrue;
        if (isTrue) currentPlayer = playerLeft;
    }
}
