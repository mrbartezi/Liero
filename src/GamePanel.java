import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class GamePanel extends JPanel implements Runnable {

    private MovingObject rObj;
    private Thread thread;
    private boolean running;
    private int time = 0, timeInSeconds = 0, count = 0;
    private int fps = 60;
    private int lastMove = 0;


    public GamePanel(int frameWidth, int frameHeight) {

        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        thread = new Thread(this);

        rObj = new MovingObject(0,50,50);
        rObj.setxCord(375);
        rObj.setyCord(275);

        start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.WHITE);
        Rectangle2D rect = new Rectangle2D.Double(rObj.getxCord(), Math.floor(rObj.getyCord()), rObj.getWidth(), rObj.getHeight());
        g2.fill(rect);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000/fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
            if(count%fps == 0) {
                timeInSeconds++;
                System.out.print( "\r" + timeInSeconds);
            }

            time++;
            rObj.setTime(time);
            rObj.calculatePosition();
            if(lastMove != 0) {
                if(lastMove == 1) {
                    rObj.setxCord(rObj.getxCord()+10);
                }
                else if(lastMove == 2) {
                    rObj.setxCord(rObj.getxCord()-10);
                }
            }
            repaint();

        }
    }

    public void start() {
        running = true;
        thread.start();
    }

    public  void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            rObj.setySpeed(-500);
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            lastMove = 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            lastMove = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            lastMove = 0;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            lastMove = 0;
        }
    }
}
