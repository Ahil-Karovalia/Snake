# Snake

This is a classic Snake game built in Java using Swing. The game includes multiple modes for a fun and customizable experience.

# Game Features
  - Classic Mode: Play the traditional Snake game, where you avoid hitting the walls and           yourself.
  - Obstacle Mode: As you eat food, obstacles appear on the grid, making it more challenging       to navigate.
  - Infinite Mode: The Snake wraps around the screen edges, allowing for continuous movement       without hitting the walls.
    Controls:
      - Arrow keys to control the Snake's direction.
      - Press 1, 2, or 3 to start Classic, Obstacle, or Infinite mode, respectively.
      - Press R to restart the game.

# Game Rules
1. Eating Food: Each time the Snake eats food, it grows longer, and your score increases by one.
2. Collision: In Classic and Obstacle modes, the game ends if the Snake hits the wall, itself, or obstacles. In Infinite mode, the Snake wraps around the screen but still ends the game if it hits itself.
3. Obstacles: Obstacles appear each time the Snake eats food in Obstacle Mode.

# Game Mode Descriptions
  - Classic Mode: The game ends upon collision with the wall or the Snake's own body.
  - Obstacle Mode: In addition to the Classic rules, obstacles appear on the screen as the         Snake eats food.
  - Infinite Mode: The Snake wraps around the screen edges. The game only ends if the Snake        collides with its own body.

# Code Structure
  - SnakeGame.java: The main class containing the game logic, GUI setup, and control functions.
  - GamePanel: Handles graphics and draws the game elements, including the Snake, food,            obstacles, and score.
  - GameLoop: Manages the game loop, updating the game state and handling movement and             collision detection.
  - GameMode Enum: Defines the different game modes (Classic, Obstacle, Infinite).

# Getting Started
Prerequisites
  - Java: Ensure Java is installed. You can check by running java -version in your terminal.
  - Java IDE (optional): For easier editing and running, use an IDE like IntelliJ IDEA,            Eclipse, or VS Code.

# Running the Game
1. Clone or download the repository.
2. Open the project in your Java IDE or compile it via the terminal.
3. Run the SnakeGame class to start the game.

# To compile
javac SnakeGame.java

# To run
java SnakeGame

# Future Enhancements
  - Adding a pause/resume functionality.
  - Including more obstacles as difficulty increases.
  - Adding a scoring leaderboard or high-score feature.
