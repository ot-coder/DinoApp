package DinoApp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChromeDino extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 1000;
    int boardHeight = 500;

    // Images
    Image dinosaurImg;
    Image dinasaurDeadImg;
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

    //cactus
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;

    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray;

    Block cactus;


    //game physics
    int velocityY = 0;
    int gravity = 1;
    
    Timer gameLoop;
    Timer cactusLoop;

    public ChromeDino() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        dinosaurImg = new ImageIcon(getClass().getResource("./img/dino-run1.gif")).getImage();
        dinasaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();

        //dinosaur
        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);

        //cactus
        cactusArray = new ArrayList<Block>();

        //game timer
        gameLoop = new Timer(1000 / 60, this); // 60 fps 
        gameLoop.start();

        //cactus timer
        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();

            }
        });
        placeCactusTimer.start();
    }

   

    protected void paintComponent(Graphics g) { // Fixed method signature
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null); // Fixed parameter order
    }

    public void move() {
        velocityY += gravity;
        dinosaur.y += velocityY;
        
          if   (dinosaur.y > dinosaurY){ //this section prevents the dinosaur from falling off the screen 
                dinosaur.y = dinosaurY;
                velocityY = 0;
         
        }
    }    

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (dinosaur.y == dinosaurY) {
                velocityY = -20;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
