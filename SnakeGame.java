import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Main class for the Snake Game
public class SnakeGame extends JFrame {

    // Game constants
    private static final int GAME_WIDTH = 700;
    private static final int GAME_HEIGHT = 700;
    private static final int SPACE_SIZE = 25; // Size of each grid space
    private static final int BODY_PARTS = 3; // Initial snake length
    private static final int SPEED = 100; // Speed of game

    // Color settings
    private static final Color SNAKE_COLOR = new Color(34, 139, 34);
    private static final Color FOOD_COLOR = Color.RED;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color GRID_COLOR = Color.DARK_GRAY;
    private static final Color OBSTACLE_COLOR = Color.GRAY;

    // Game state variables
    private int[] x = new int[(GAME_WIDTH * GAME_HEIGHT) / (SPACE_SIZE * SPACE_SIZE)]; // Snake's x positions
    private int[] y = new int[(GAME_WIDTH * GAME_HEIGHT) / (SPACE_SIZE * SPACE_SIZE)]; // Snake's y positions
    private int bodySize = BODY_PARTS; // Current size of snake
    private int foodX, foodY; // Position of food
    private char direction = 'D'; // Start direction
    private boolean running = false;
    private boolean gameStarted = false;
    private Timer timer;
    private int score = 0;
    private List<Point> obstacles = new ArrayList<>(); // List to store obstacles
    private GameMode gameMode = GameMode.CLASSIC; // Default game mode

    // Constructor sets up the game window
    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        add(panel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Starts or restarts the game
    public void startGame() {
        if (timer != null) {
            timer.stop(); // Stop the timer if it was running
        }
        bodySize = BODY_PARTS; // Reset snake size
        running = true;
        gameStarted = true;
        direction = 'D'; // Reset direction
        score = 0;
        obstacles.clear(); // Clear any existing obstacles
        spawnFood(); // Place initial food on the board

        // Initialize the snake's starting position
        for (int i = 0; i < bodySize; i++) {
            x[i] = GAME_WIDTH / 2 - i * SPACE_SIZE;
            y[i] = GAME_HEIGHT / 2;
        }

        timer = new Timer(SPEED, new GameLoop());
        timer.start();
    }

    // Restarts the game and resets necessary variables
    public void restartGame() {
        if (timer != null) {
            timer.stop();
        }
        
        running = false;
        gameStarted = false;
        direction = 'D';
        bodySize = BODY_PARTS;
        score = 0;
        obstacles.clear();
        spawnFood();

        for (int i = 0; i < x.length; i++) {
            x[i] = -1;
            y[i] = -1;
        }
    }

    // Places food randomly on the board
    public void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(GAME_WIDTH / SPACE_SIZE) * SPACE_SIZE;
        foodY = random.nextInt(GAME_HEIGHT / SPACE_SIZE) * SPACE_SIZE;
    }

    // Moves the snake based on the current direction
    public void move() {
        for (int i = bodySize - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] -= SPACE_SIZE; break;
            case 'D': y[0] += SPACE_SIZE; break;
            case 'L': x[0] -= SPACE_SIZE; break;
            case 'R': x[0] += SPACE_SIZE; break;
        }

        // Handle boundaries based on game mode
        if (gameMode == GameMode.CLASSIC || gameMode == GameMode.OBSTACLE) {
            if (x[0] < 0 || x[0] >= GAME_WIDTH || y[0] < 0 || y[0] >= GAME_HEIGHT) {
                running = false;
            }
        } else if (gameMode == GameMode.INFINITE) { // Wrap-around in INFINITE mode
            if (x[0] < 0) x[0] = GAME_WIDTH - SPACE_SIZE;
            if (x[0] >= GAME_WIDTH) x[0] = 0;
            if (y[0] < 0) y[0] = GAME_HEIGHT - SPACE_SIZE;
            if (y[0] >= GAME_HEIGHT) y[0] = 0;
        }

        checkCollision();
    }

    // Checks for collisions with food, self, and obstacles
    public void checkCollision() {
        if (x[0] == foodX && y[0] == foodY) {
            bodySize++;
            score++;
            spawnFood();

            // Add obstacle in OBSTACLE mode
            if (gameMode == GameMode.OBSTACLE) {
                if (obstacles.size() < score) {
                    Random random = new Random();
                    int obstacleX, obstacleY;
                    do {
                        obstacleX = random.nextInt(GAME_WIDTH / SPACE_SIZE) * SPACE_SIZE;
                        obstacleY = random.nextInt(GAME_HEIGHT / SPACE_SIZE) * SPACE_SIZE;
                    } while (obstacleX == foodX && obstacleY == foodY);
                    obstacles.add(new Point(obstacleX, obstacleY));
                }
            }
        }

        // Check for self-collision
        for (int i = bodySize - 1; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i] && gameMode != GameMode.INFINITE) {
                running = false;
            }
        }

        // Check for obstacle collision in OBSTACLE mode
        if (gameMode == GameMode.OBSTACLE) {
            for (Point obstacle : obstacles) {
                if (x[0] == obstacle.x && y[0] == obstacle.y) {
                    running = false;
                }
            }
        }

        if (!running) {
            timer.stop();
        }
    }

    // Draws all game components
    public void draw(Graphics g) {
        g.setColor(GRID_COLOR);
        for (int i = 0; i < GAME_WIDTH; i += SPACE_SIZE) {
            for (int j = 0; j < GAME_HEIGHT; j += SPACE_SIZE) {
                g.drawRect(i, j, SPACE_SIZE, SPACE_SIZE);
            }
        }

        // Draw food
        g.setColor(FOOD_COLOR);
        g.fillOval(foodX, foodY, SPACE_SIZE, SPACE_SIZE);

        // Draw obstacles if in OBSTACLE mode
        if (gameMode == GameMode.OBSTACLE) {
            g.setColor(OBSTACLE_COLOR);
            for (Point obstacle : obstacles) {
                g.fillRect(obstacle.x, obstacle.y, SPACE_SIZE, SPACE_SIZE);
            }
        }

        // Draw the snake
        for (int i = 0; i < bodySize; i++) {
            g.setColor(SNAKE_COLOR);
            g.fillRoundRect(x[i], y[i], SPACE_SIZE, SPACE_SIZE, 15, 15);
        }

        // Display score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);
    }

    // Inner class handling game panel
    private class GamePanel extends JPanel implements KeyListener {

        public GamePanel() {
            setBackground(BACKGROUND_COLOR);
            setFocusable(true);
            addKeyListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (gameStarted) {
                if (running) {
                    draw(g);
                } else {
                    gameOver(g);
                }
            } else {
                drawMenu(g);
            }
        }

        // Display start menu
        public void drawMenu(Graphics g) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            g.drawString("Press 1 for Classic Mode", (GAME_WIDTH - g.getFontMetrics().stringWidth("Press 1 for Classic Mode")) / 2, GAME_HEIGHT / 2 - 60);
            g.drawString("Press 2 for Obstacle Mode", (GAME_WIDTH - g.getFontMetrics().stringWidth("Press 2 for Obstacle Mode")) / 2, GAME_HEIGHT / 2);
            g.drawString("Press 3 for Infinite Mode", (GAME_WIDTH - g.getFontMetrics().stringWidth("Press 3 for Infinite Mode")) / 2, GAME_HEIGHT / 2 + 60);
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // Start Classic Mode
            if (key == KeyEvent.VK_1) {
                gameMode = GameMode.CLASSIC;
                restartGame();
                startGame();
            }

            // Start Obstacle Mode
            if (key == KeyEvent.VK_2) {
                gameMode = GameMode.OBSTACLE;
                restartGame();
                startGame();
            }

            // Start Infinite Mode
            if (key == KeyEvent.VK_3) {
                gameMode = GameMode.INFINITE;
                restartGame();
                startGame();
            }

            // Direction control: ensure no reversal of current direction
            if (key == KeyEvent.VK_UP && direction != 'D') {
                direction = 'U'; // Move up
            } else if (key == KeyEvent.VK_DOWN && direction != 'U') {
                direction = 'D'; // Move down
            } else if (key == KeyEvent.VK_LEFT && direction != 'R') {
                direction = 'L'; // Move left
            } else if (key == KeyEvent.VK_RIGHT && direction != 'L') {
                direction = 'R'; // Move right
            }

            // Restart the game if 'R' is pressed
            if (key == KeyEvent.VK_R) {
                restartGame();
                startGame();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // Not needed for this game
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // Not needed for this game
        }
    }

    // Inner class for handling game loop updates (movement, repaint, and collision checks)
    private class GameLoop implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                move();       // Move the snake
                repaint();    // Redraw the game panel
            } else {
                repaint();    // Redraw to show "Game Over" message
            }
        }
    }

    // Draws the game-over screen
    public void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 40));
        g.drawString("Game Over!", (GAME_WIDTH - g.getFontMetrics().stringWidth("Game Over!")) / 2, GAME_HEIGHT / 2 - 60);

        // Display final score
        g.setFont(new Font("Consolas", Font.BOLD, 30));
        g.drawString("Score: " + score, (GAME_WIDTH - g.getFontMetrics().stringWidth("Score: " + score)) / 2, GAME_HEIGHT / 2);

        // Instructions to restart
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.drawString("Press R to Restart", (GAME_WIDTH - g.getFontMetrics().stringWidth("Press R to Restart")) / 2, GAME_HEIGHT / 2 + 60);
    }

    public static void main(String[] args) {
        // Launches the game on the Swing event dispatch thread
        SwingUtilities.invokeLater(() -> new SnakeGame());
    }
}

// Enum to define the different game modes
enum GameMode {
    CLASSIC,   // Classic snake game mode
    OBSTACLE,  // Mode with obstacles added as the snake eats food
    INFINITE   // Infinite mode with wrap-around screen edges
}