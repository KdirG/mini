import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreakerGame extends JFrame {
    public BrickBreakerGame() {
        setTitle("Brick Breaker");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GamePanel());
        setVisible(true);
    }

    public static void main(String[] args) {
        new BrickBreakerGame();
    }
}

class GamePanel extends JPanel implements ActionListener, MouseMotionListener {
    private Timer timer;
    private int ballX = 400, ballY = 300, ballSize = 20;
    private int ballSpeedX = 3, ballSpeedY = 3;
    private int paddleX = 350, paddleWidth = 100, paddleHeight = 10;

    private boolean[][] bricks;
    private final int brickRows = 3;
    private final int brickCols = 8;
    private final int brickWidth = 80;
    private final int brickHeight = 30;

    public GamePanel() {
        setBackground(Color.BLACK);
        bricks = new boolean[brickRows][brickCols];
        for (int i = 0; i < brickRows; i++)
            for (int j = 0; j < brickCols; j++)
                bricks[i][j] = true;

        timer = new Timer(10, this);
        timer.start();
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw ball
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, ballSize, ballSize);

        // Draw paddle
        g.setColor(Color.BLUE);
        g.fillRect(paddleX, getHeight() - 50, paddleWidth, paddleHeight);

        // Draw bricks
        g.setColor(Color.YELLOW);
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (bricks[i][j]) {
                    int x = j * (brickWidth + 10) + 35;
                    int y = i * (brickHeight + 10) + 30;
                    g.fillRect(x, y, brickWidth, brickHeight);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Top hareket
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Duvar çarpması
        if (ballX <= 0 || ballX + ballSize >= getWidth()) ballSpeedX *= -1;
        if (ballY <= 0) ballSpeedY *= -1;
        if (ballY + ballSize >= getHeight()) {
            ballX = 400;
            ballY = 300;
        }

        // Paddle çarpması
        Rectangle ballRect = new Rectangle(ballX, ballY, ballSize, ballSize);
        Rectangle paddleRect = new Rectangle(paddleX, getHeight() - 50, paddleWidth, paddleHeight);
        if (ballRect.intersects(paddleRect)) {
            ballSpeedY *= -1;
            ballY = getHeight() - 50 - ballSize; // yapışmasın
        }

        // Brick çarpışması
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (bricks[i][j]) {
                    int brickX = j * (brickWidth + 10) + 35;
                    int brickY = i * (brickHeight + 10) + 30;
                    Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                    if (ballRect.intersects(brickRect)) {
                        bricks[i][j] = false;
                        ballSpeedY *= -1;
                    }
                }
            }
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        paddleX = e.getX() - paddleWidth / 2;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {}
}
