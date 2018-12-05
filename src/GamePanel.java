import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    private MovingObject player;
    private Thread thread;
    private boolean running, onGround, mousePressed = false, controlPressed = false;
    private static int fps = 120;
    private int lastMove = 0;
    private int frameWidth, frameHeight;
    private int[][] pixels;
    private static boolean editMode = false;
    private BufferedImage image;
    private int blockWidth, blockHeight, x1, x2, y1, y2;


    public GamePanel(int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setLayout(new GridBagLayout());
        setBackground(Color.BLUE);

        pixels = new int[frameWidth][frameHeight];
        for(int i = 0; i < frameWidth; i++) {
            for(int j = 0; j < frameHeight; j++) {
                pixels[i][j] = 0;
            }
        }
        MovingObject.setPixels(pixels);
        MovingObject.setFps(fps);

        image = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_RGB);

        thread = new Thread(this);

        player = new MovingObject(0,10,30);
        player.setxCord(frameWidth/2);
        player.setyCord(frameHeight/2);

        blockWidth = EditMode.getWidth();
        blockHeight = EditMode.getHeight();

        start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(image, 0, 0, null);

        g2.setPaint(Color.RED);
        Rectangle2D rect = new Rectangle2D.Double(player.getxCord(), player.getyCord(),
                player.getWidth(), player.getHeight());
        g2.fill(rect);
        g2.draw(rect);


        if(editMode) {
            g2.setPaint(Color.GREEN);
            if (getMousePosition() != null) {
                Rectangle2D ghostRect = new Rectangle2D.Double(getMousePosition().x - blockWidth/2,
                        getMousePosition().y - blockHeight/2, blockWidth, blockHeight);
                //g2.fill(ghostRect);
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
                player.setxSpeed(1000);
            }
            else if(lastMove == 2 && lastMove != 12) {
                player.setxSpeed(-1000);
            }

            player.calculatePosition();

            if (mousePressed && editMode) {
                makeBlock();
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
        if(editMode) {
            EditMode.keyPressed(e);
            blockWidth = EditMode.getWidth();
            blockHeight = EditMode.getHeight();
        }

        ///////////////////////////////////////////////

        if(e.getKeyCode() == KeyEvent.VK_UP) {
            onGround = true;

            if(onGround) {
                player.setySpeed(-1000);
                onGround = false;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            lastMove = 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            lastMove = 2;
        }

        //////////////////////////////////////////

        if(e.getKeyCode() == KeyEvent.VK_F1) {
            if(editMode) {
                editMode = false;
            }
            else {
                editMode = true;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_E) {
            System.out.println(player.getxCord() + " : " + player.getyCord() + " : " + player.getySpeed());
        }

        if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
            controlPressed = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_Z) {
            if(controlPressed) {
                deleteLastBlock();
                System.out.println("d");
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if(editMode) {
            EditMode.keyReleased(e);
        }
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
        ///////////////////////////////////////////

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


    public void makeBlock() {
        blockWidth = EditMode.getWidth();
        blockHeight = EditMode.getHeight();

        x1 = getMousePosition().x - blockWidth/2;
        y1 = getMousePosition().y - blockHeight/2;
        x2 = getMousePosition().x + blockWidth/2;
        y2 = getMousePosition().y + blockHeight/2;

        EditMode.blockList.add(new Rectangle2D.Double(x1 + blockWidth/2, y1 + blockHeight/2, blockWidth, blockHeight));

        if(x1 < 0) {
            x1 = 0;
        }
        if(x2 > frameWidth) {
            x2 = frameWidth;
        }
        if(y1 < 0) {
            y1 = 0;
        }
        if(y2 > frameHeight) {
            y2 = frameHeight;
        }

        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                pixels[i][j] = 1;
            }
        }

        MovingObject.setPixels(pixels);

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

    public void deleteLastBlock() {
        double deleteX = EditMode.blockList.get(EditMode.blockList.size()-1).getX();
        double deleteY = EditMode.blockList.get(EditMode.blockList.size()-1).getY();
        double deleteWidth = EditMode.blockList.get(EditMode.blockList.size()-1).getWidth();
        double deleteHeight = EditMode.blockList.get(EditMode.blockList.size()-1).getHeight();

        for(int i = (int)deleteX; i < deleteX + deleteWidth; i++) {
            for(int j = (int)deleteY; j < deleteY + deleteHeight; j++) {
                pixels[i][j] = 0;
            }
        }
        MovingObject.setPixels(pixels);

        EditMode.blockList.remove(EditMode.blockList.size()-1);
    }
}