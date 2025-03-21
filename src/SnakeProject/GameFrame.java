package SnakeProject;


import javax.swing.JFrame;
import javax.swing.ImageIcon;
public class GameFrame extends JFrame{
    ImageIcon icon = new ImageIcon("Snakelc.png");
    GameFrame(){
        this.setIconImage(icon.getImage());
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}