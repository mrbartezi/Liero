import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class MainFrame extends JFrame implements KeyListener {

    private GamePanel gamePanel;
    private int frameWidth = 800, frameHeight = 600;

    public MainFrame() {

        Toolkit tk = Toolkit.getDefaultToolkit();

        setLocation((tk.getScreenSize().width - frameWidth)/2, (tk.getScreenSize().height - frameHeight)/2);
        setTitle("Liero");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        addKeyListener(this);
        setFocusable(true);

        MovingObject.setFrameWidth(frameWidth);
        MovingObject.setFrameHeight(frameHeight);

        gamePanel = new GamePanel(frameWidth, frameHeight);

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
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            remove(gamePanel);
            gamePanel = new GamePanel(frameWidth, frameHeight);
            add(gamePanel, BorderLayout.CENTER);
            pack();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gamePanel.keyReleased(e);
    }
}
