package SnakeProject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    static final int SCREEN_WIDTH = 100;
    static final int SCREEN_HEIGHT = 100;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten = 0;
    int spAppleX;
    int spAppleY;
    int appleX;
    int appleY;
    int highscore = 0;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    boolean directionChanged = false;
    String gameState = "MENU";

    Rectangle startGameButton;
    Rectangle settingsButton;
    Rectangle retryButton;
    Rectangle backToMenuButton;
    Rectangle easyButton, normalButton, hardButton;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH * 5, SCREEN_HEIGHT * 5));
        this.setBackground(new Color(34, 34, 34));
        this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e);
            }
        });
        this.addKeyListener(this);
        initializeButtons();
    }

    // ============================= FOR MAIN MENU AND ENTITY DESIGNS =================================

    private void initializeButtons() {
        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = (SCREEN_WIDTH * 5 - buttonWidth) / 2;

        //============== MGA BUTTONS =================
        startGameButton = new Rectangle(centerX, SCREEN_HEIGHT * 5 / 2, buttonWidth, buttonHeight);
        settingsButton = new Rectangle(centerX, SCREEN_HEIGHT * 5 / 2 + 70, buttonWidth, buttonHeight);
        retryButton = new Rectangle(centerX, SCREEN_HEIGHT * 5 / 2, buttonWidth, buttonHeight);
        backToMenuButton = new Rectangle(centerX, SCREEN_HEIGHT * 5 / 2 + 70, buttonWidth, buttonHeight);
        easyButton = new Rectangle(centerX, SCREEN_HEIGHT * 5 / 3, buttonWidth, buttonHeight);
        normalButton = new Rectangle(centerX, SCREEN_HEIGHT * 5 / 2, buttonWidth, buttonHeight);
        hardButton = new Rectangle(centerX, (int) (SCREEN_HEIGHT * 5 / 1.5f), buttonWidth, buttonHeight);
    }

    public void startGame() {
        resetGame();
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void resetGame() {
        bodyParts = 5;
        applesEaten = 0;
        direction = 'R';
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState.equals("MENU")) {
            drawMenu(g);
        } else if (gameState.equals("SETTINGS")) {
            drawSettings(g);
        } else {
            draw(g);
        }
    }

    public void drawMenu(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Snake Game", (SCREEN_WIDTH * 5 - metrics.stringWidth("Snake Game")) / 2, SCREEN_HEIGHT * 2);

        g.setFont(new Font("Consolas", Font.BOLD, 20));
        drawButton(g, startGameButton, "Start Game");
        drawButton(g, settingsButton, "Settings");
    }

    public void drawSettings(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Settings", (SCREEN_WIDTH * 5 - metrics.stringWidth("Settings")) / 2, SCREEN_HEIGHT * 2);

        // Draw buttons
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        drawButton(g, easyButton, "Slow");
        drawButton(g, normalButton, "Average");
        drawButton(g, hardButton, "Fast");
    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT * 5 / UNIT_SIZE; i++) {
                g.setColor(new Color(50, 50, 50));
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT * 5);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH * 5, i * UNIT_SIZE);
            }
            g.setColor(new Color(255, 69, 0));
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            if (spAppleX != 0 && spAppleY != 0) {
                g.setColor(new Color(245, 253, 10));
                g.fillOval(spAppleX, spAppleY, UNIT_SIZE, UNIT_SIZE);
            }

            for (int i = 0; i < bodyParts; i++) {
                g.setColor(i == 0 ? new Color(255, 215, 0) : new Color(255, 140, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.white);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH * 5 - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
            g.drawString("Highscore: " + highscore, (SCREEN_WIDTH * 5 - metrics.stringWidth("Highscore: " + highscore)) / 2, g.getFont().getSize() + 40);
        } else {
            drawGameOver(g);
        }
    }

    public void drawGameOver(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.BOLD, 25));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH * 5 - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT * 2);

        g.setFont(new Font("Consolas", Font.BOLD, 15));
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH * 5 - metrics.stringWidth("Score: " + applesEaten)) / 2, SCREEN_HEIGHT * 3);
        g.drawString("Highscore: " + highscore, (SCREEN_WIDTH * 5 - metrics.stringWidth("Highscore: " + highscore)) / 2, SCREEN_HEIGHT * 4);

        drawButton(g, retryButton, "Retry");
        drawButton(g, backToMenuButton, "Back to Menu");
    }

    private void drawButton(Graphics g, Rectangle button, String text) {
        g.setColor(new Color(70, 70, 70));
        g.fillRect(button.x, button.y, button.width, button.height);
        g.setColor(Color.white);
        g.drawRect(button.x, button.y, button.width, button.height);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(text, button.x + (button.width - metrics.stringWidth(text)) / 2, button.y + button.height / 2 + metrics.getHeight() / 4);
    }

    // ============================= FOR GAME MECHS =================================

    private void handleMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (gameState.equals("MENU")) {
            if (startGameButton.contains(mouseX, mouseY)) {
                gameState = "GAME";
                startGame();
            } else if (settingsButton.contains(mouseX, mouseY)) {
                gameState = "SETTINGS";
                repaint();
            }
        } else if (gameState.equals("SETTINGS")) {
            if (easyButton.contains(mouseX, mouseY)) {
                DELAY = 100;
                gameState = "MENU";
            } else if (normalButton.contains(mouseX, mouseY)) {
                DELAY = 75;
                gameState = "MENU";
            } else if (hardButton.contains(mouseX, mouseY)) {
                DELAY = 50;
                gameState = "MENU";
            }
            repaint();
        } else if (gameState.equals("GAME") && !running) {
            if (retryButton.contains(mouseX, mouseY)) {
                gameState = "GAME";
                startGame();
            } else if (backToMenuButton.contains(mouseX, mouseY)) {
                gameState = "MENU";
                repaint();
            }
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH * 5 / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT * 5 / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void specialApple() {
        int chance = random.nextInt(100); // Random number between 0 and 99
        if (chance < 20) { // 20% chance
            spAppleX = random.nextInt((int) (SCREEN_WIDTH * 5 / UNIT_SIZE)) * UNIT_SIZE;
            spAppleY = random.nextInt((int) (SCREEN_HEIGHT * 5 / UNIT_SIZE)) * UNIT_SIZE;
        } else {
            spAppleX = 0;
            spAppleY = 0;
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] -= UNIT_SIZE; break;
            case 'D': y[0] += UNIT_SIZE; break;
            case 'L': x[0] -= UNIT_SIZE; break;
            case 'R': x[0] += UNIT_SIZE; break;
        }
    }

    public void checkSpecialApple() {
        if (x[0] == spAppleX && y[0] == spAppleY) {
            bodyParts++;
            applesEaten += 10;
            if (applesEaten > highscore) {
                highscore = applesEaten;
            }
            specialApple();
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            if (applesEaten > highscore) {
                highscore = applesEaten;
            }
            newApple();
            specialApple();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH * 5 || y[0] < 0 || y[0] >= SCREEN_HEIGHT * 5) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            directionChanged = false;
            move();
            checkApple();
            checkSpecialApple();
            checkCollisions();
        }
        repaint();
    }

    // ============================= FOR MOVEMENTS AND SHII =================================

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R' && !directionChanged) {
                    direction = 'L';
                    directionChanged = true;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L' && !directionChanged) {
                    direction = 'R';
                    directionChanged = true;
                }
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D' && !directionChanged) {
                    direction = 'U';
                    directionChanged = true;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U' && !directionChanged) {
                    direction = 'D';
                    directionChanged = true;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
