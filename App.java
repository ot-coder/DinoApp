import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 1000;
        int boardHeight = 500;

        JFrame frame = new JFrame("ChromeDino");
        
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Dino Game");
        frame.setVisible(true);

        ChromeDino chromeDino = new ChromeDino();
        frame.add(chromeDino);
        frame.pack();
        chromeDino.requestFocus();
        frame.setVisible(true);
    }
} 
