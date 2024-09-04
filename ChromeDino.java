import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ChromeDino extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 900;
    int boardHeight = 500;

    // Images
    Image dinosaurImg;
    Image dinosaurDeadImg; 
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    // Dinosaur
    int dinosaurWidth = 88;
    int dinosaurHeight = 90;
    int dinosaurX = 100;
    int dinosaurY = boardHeight - dinosaurHeight;

    Block dinosaur;

    // Cactus
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;

    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray;



    // Game physics
    int velocityX = -12;
    int velocityY = 0; // Jump velocity of the dinosaur
    int gravity = 1; // Gravity of the dinosaur

    boolean gameOver = false;
    int score = 0;

    Timer gameLoop;
    Timer placeCactusTimer;

    public ChromeDino() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.darkGray);
        setFocusable(true);
        addKeyListener(this);

        // Corrected resource paths to use absolute path
        dinosaurImg = new ImageIcon(getClass().getResource("/img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("/img/dino-dead.png")).getImage(); 
        dinosaurJumpImg = new ImageIcon(getClass().getResource("/img/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("/img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("/img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("/img/cactus3.png")).getImage();

        // Initialize dinosaur
        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);

        // Initialize cactus array
        cactusArray = new ArrayList<Block>();

        // Initialize and start game loop timer (60 FPS)
        gameLoop = new Timer(1000 / 60, this); // 60 fps 
        gameLoop.start();

        // Initialize and start cactus placement timer
        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        placeCactusTimer.start(); // Started the timer
    }

    public void placeCactus() {
        if (gameOver) {
            return;
        }
        double placeCactusChance = Math.random(); // Generates a random number between 0 and 1
        if (placeCactusChance > 0.90) {
            Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > 0.70) {
            Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > 0.40) {
            Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img);
            cactusArray.add(cactus);
        }
    } 

    @Override
    protected void paintComponent(Graphics g) {    
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw dinosaur
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null); 
        
        // Draw cactuses
        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }
        
        g.setColor(Color.green);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver){
            g.drawString("Game Over: " + String.valueOf(score), 10,35);
        }
        else {
            g.drawString(String.valueOf(score), 10, 35);
        }
    }

    public void move() {
        // Update dinosaur's position
        velocityY += gravity;
        dinosaur.y += velocityY;
        
        if (dinosaur.y > dinosaurY) { // Prevents the dinosaur from falling below the ground
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        // Update cactuses' positions
        for (int i = 0; i < cactusArray.size(); i++) {
            Block cactus = cactusArray.get(i);
            cactus.x += velocityX; 

            // Remove cactus if it moves off-screen to prevent infinite growth
            if (cactus.x + cactus.width < 0) {
                cactusArray.remove(i);
                i--; // Adjust index after removal
            }

            if(collision(dinosaur, cactus)) {
                gameOver = true;
                dinosaur.img = dinosaurDeadImg;
            }
        }

        //scoring
        score++;
    }    

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeCactusTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (dinosaur.y == dinosaurY) { // Only jump if dinosaur is on the ground
                velocityY = -20;
                dinosaur.img = dinosaurJumpImg; // Change image to jump image
            }
            if (gameOver) {
                dinosaur.y = dinosaurY;
                dinosaur.img = dinosaurImg;
                velocityY = 0;
                cactusArray.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeCactusTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        // Optionally, revert to running image when key is released
        if (dinosaur.y == dinosaurY) {
            dinosaur.img = dinosaurImg;
        }
    }
}
