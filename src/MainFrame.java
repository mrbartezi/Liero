import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainFrame extends JFrame implements KeyListener, MouseListener {

    private GamePanel gamePanel;
    private int frameWidth = 1600, frameHeight = 900;
    private int mapWidth = 3000, mapHeight = 3000;

    public MainFrame() {

        Toolkit tk = Toolkit.getDefaultToolkit();

        setLocation((tk.getScreenSize().width - frameWidth)/2, (tk.getScreenSize().height - frameHeight)/2);
        setTitle("Liero");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //FULLSCREEN
        //setUndecorated(true);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);

        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());

        addKeyListener(this);
        setFocusable(true);

        addMouseListener(this);

        gamePanel = new GamePanel(frameWidth, frameHeight, mapWidth, mapHeight);

        add(gamePanel, BorderLayout.CENTER);

        pack();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        gamePanel.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.keyPressed(e);
        if(e.getKeyCode() == KeyEvent.VK_R) {
            remove(gamePanel);
            gamePanel = new GamePanel(frameWidth, frameHeight, mapWidth, mapHeight);
            add(gamePanel, BorderLayout.CENTER);
            pack();
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gamePanel.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gamePanel.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gamePanel.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        gamePanel.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        gamePanel.mouseExited(e);
    }
}