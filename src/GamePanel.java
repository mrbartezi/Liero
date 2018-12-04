import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    private MovingObject rObj;
    private Thread thread;
    private boolean running, onGround, staticObjectsCollision = false,
            editMode = true, controlPressed = false, mousePressed = false;
    private int time = 0, timeInSeconds = 0;
    private static int fps = 60;
    private int lastMove = 0;
    private ArrayList<StaticObject> staticObjectsList;



    public GamePanel(int frameWidth, int frameHeight) {
        MovingObject.setFps(fps);

        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        staticObjectsList = new ArrayList<>();
        staticObjectsList.add(new StaticObject(0, frameHeight, frameWidth, frameHeight));

        MovingObject.setStaticObjectsList(staticObjectsList);


        thread = new Thread(this);

        rObj = new MovingObject(0,10,30);
        rObj.setxCord(frameWidth/2);
        rObj.setyCord(frameHeight/2 - 400);

        start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(Color.WHITE);
        for(StaticObject s : staticObjectsList) {
            g2.fill(new Rectangle2D.Double(s.getX1(), s.getY1(), s.getX2() - s.getX1(), s.getY2() - s.getY1()));
            g2.draw(new Rectangle2D.Double(s.getX1(), s.getY1(), s.getX2() - s.getX1(), s.getY2() - s.getY1()));
        }

        g2.setPaint(Color.RED);
        Rectangle2D rect = new Rectangle2D.Double(rObj.getxCord(), rObj.getyCord(),
                rObj.getWidth(), rObj.getHeight());
        g2.fill(rect);
        g2.draw(rect);


        if(editMode) {
            g2.setPaint(Color.GREEN);
            staticObjectsCollision = false;
            if (getMousePosition() != null) {
                Rectangle2D ghostRect = new Rectangle2D.Double(getMousePosition().x - 50, getMousePosition().y - 5, 100, 10);

                loop1:
                for (StaticObject a : staticObjectsList) {
                    if (getMousePosition().x + 50 >= a.getX1() && getMousePosition().x - 50 <= a.getX2() &&
                            getMousePosition().y + 5 >= a.getY1() && getMousePosition().y - 5 <= a.getY2()) {
                        staticObjectsCollision = true;
                        g2.setPaint(Color.RED);
                    }
                }
                g2.fill(ghostRect);
                g2.draw(ghostRect);
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            rObj.checkIfInFrame();
            try {
                Thread.sleep(1000/fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(lastMove == 1 && lastMove != 11) {
                rObj.setxCord(rObj.getxCord() + 600/fps);
                rObj.checkIfInFrame();
            }
            else if(lastMove == 2 && lastMove != 12) {
                rObj.setxCord(rObj.getxCord() - 600/fps);
                rObj.checkIfInFrame();
            }

            rObj.calculatePosition();

            rObj.checkIfInFrame();
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
        if(e.getKeyCode() == KeyEvent.VK_F1) {
            if(editMode) {
                editMode = false;
            }
            else {
                editMode = true;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
            controlPressed = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_Z) {
            if(controlPressed && editMode && staticObjectsList.size() > 1) {
                staticObjectsList.remove(staticObjectsList.size()-1);
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_UP) {
            for(StaticObject s : staticObjectsList) {
                if(rObj.getxCord() >= s.getX1() - rObj.getWidth() && rObj.getxCord() <= s.getX2()){
                    if(rObj.getyCord() + rObj.getHeight() >= s.getY1() - 1 &&
                            rObj.getyCord() + rObj.getHeight() < s.getY1() + rObj.getySpeed()/fps + 1) {
                        onGround = true;
                    }
                }
            }
            if(onGround) {
                rObj.setySpeed(-1000);
                onGround = false;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            lastMove = 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            lastMove = 2;
        }

        if(e.getKeyCode() == KeyEvent.VK_E) {
            System.out.println(staticObjectsList.size());
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(lastMove != 2) {
                lastMove = 11;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(lastMove != 1) {
                lastMove = 12;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
            controlPressed = false;
        }
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        if (!staticObjectsCollision && editMode) {
            staticObjectsList.add(new StaticObject(getMousePosition().x - 50, getMousePosition().y - 5,
                    getMousePosition().x + 50, getMousePosition().y + 5));
        }
    }

    public void mouseReleased(MouseEvent e) {
        mousePressed = false;

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}