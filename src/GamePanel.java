import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    private MovingObject rObj;
    private Thread thread;
    private boolean running, onGround, staticObjectsCollision = false,
            editMode = true, controlPressed = false, mousePressed = false;
    private int time = 0, timeInSeconds = 0;
    private static int fps = 120;
    private int lastMove = 0;
    private ArrayList<StaticObject> staticObjectsList;
    private int frameWidth, frameHeight;
    private int[][] pixels;
    private BufferedImage image;


    public GamePanel(int frameWidth, int frameHeight) {
        pixels = new int[frameWidth][frameHeight];
        for(int i = 0; i < frameWidth; i++) {
            for(int j = 0; j < frameHeight; j++) {
                pixels[i][j] = 0;
            }
        }

        MovingObject.setFps(fps);
        StaticObject.setPixels(pixels);
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        image = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_RGB);
        StaticObject.setImage(image);

        MovingObject.setStaticObjectsList(staticObjectsList);

        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);


        thread = new Thread(this);

        rObj = new MovingObject(0,10,30);
        rObj.setxCord(frameWidth/2);
        rObj.setyCord(frameHeight/2);

        start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(image, 0, 0, null);

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
                g2.fill(ghostRect);
                g2.draw(ghostRect);
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000/fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(lastMove == 1 && lastMove != 11) {
                rObj.setxSpeed(1000);
                rObj.checkIfInFrame();
            }
            else if(lastMove == 2 && lastMove != 12) {
                rObj.setxSpeed(-1000);
                rObj.checkIfInFrame();
            }

            rObj.calculatePosition();

            if (mousePressed && !staticObjectsCollision && editMode) {
                int x1 = getMousePosition().x - 50;
                int y1 = getMousePosition().y - 5;
                int x2 = getMousePosition().x + 50;
                int y2 = getMousePosition().y + 5;

                for (int i = x1; i < x2; i++) {
                    for (int j = y1; j < y2; j++) {
                        pixels[i][j] = 1;
                    }
                }
                int alpha = 255;
                int red = 255;
                int green = 255;
                int blue = 255;
                int p = (alpha<<24) | (red<<16) | (green<<8) | blue;

                for(int i = 0; i < frameWidth; i++) {
                    for(int j = 0; j < frameHeight; j++) {
                        if(pixels[i][j] == 1) {
                            image.setRGB(i, j, p);
                        }
                    }
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
            onGround = true;

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
            System.out.println(rObj.getxCord() + " : " + rObj.getyCord() + " : " + rObj.getySpeed());
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
    }

    public void mouseReleased(MouseEvent e) {
        mousePressed = false;

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}