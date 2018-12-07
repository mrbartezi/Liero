import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.*;

public class GamePanel extends JPanel implements Runnable {

    private MovingObject player;
    private Thread thread;
    private boolean running, onGround;
    private static int fps = 120;
    private int lastMove = 0;
    private int frameWidth, frameHeight;
    private int mapWidth, mapHeight;
    private int[][] pixels;
    private int framesCounter = 0, tempFramesCounter = 0, FPS = 0;
    private static boolean editMode = false;
    private BufferedImage image;
    private int blockWidth, blockHeight, x1, x2, y1, y2;
    private ArrayList<MovingObject> movingObjects = new ArrayList<>();
    private int crosshairAngle = 180;
    private Date date = new Date();
    private int xOffScreen, yOffScreen;

    //PRESSED KEYS BOOLEANS
    private boolean mouse1Pressed = false, mouse2Pressed = false, controlPressed = false, shiftPressed = false,
            upArrowPressed = false, downArrowPressed = false;


    public GamePanel(int frameWidth, int frameHeight, int mapWidth1, int mapHeight1) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        mapWidth = mapWidth1;
        mapHeight = mapHeight1;
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        /*
        pixels = new int[mapWidth][mapHeight];
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                pixels[i][j] = 0;
            }
        }
        */
        File file = new File("map.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int b1 = Integer.valueOf(br.readLine());
            int b2 = Integer.valueOf(br.readLine());
            pixels = new int[b1][b2];
            mapWidth = b1;
            mapHeight = b2;

            for(int i = 0; i < b1; i++) {
                for(int j = 0; j < b2; j++) {
                    pixels[i][j] = Integer.valueOf(br.readLine());
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        MovingObject.setPixels(pixels);
        MovingObject.setFps(fps);
        MovingObject.setMapWidth(mapWidth);
        MovingObject.setMapHeight(mapHeight);

        image = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
        MovingObject.setImage(image);

        thread = new Thread(this);

        player = new MovingObject(0,10,30);
        player.setxCord(mapWidth/2);
        player.setyCord(mapHeight/2);
        movingObjects.add(player);

        blockWidth = EditMode.getWidth();
        blockHeight = EditMode.getHeight();

        refreshImage();
        start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        pixels = MovingObject.getPixels();
        image = MovingObject.getImage();

        g2.drawImage(image, -xOffScreen, -yOffScreen, null);

        //MOVING OBJECTS
        try {
            for (MovingObject m : movingObjects) {
                if (m.getId() == -1) {
                    movingObjects.remove(m);
                } else {
                    Rectangle2D rect = new Rectangle2D.Double(m.getxCord() - xOffScreen, m.getyCord() - yOffScreen,
                            m.getWidth(), m.getHeight());
                    switch (m.getId()) {
                        case 0:
                            g2.setPaint(Color.DARK_GRAY);
                            break;
                        case 1:
                            g2.setPaint(Color.WHITE);
                            break;
                    }
                    g2.fill(rect);
                    g2.draw(rect);
                }
            }
        }catch (Exception e) {

        }

        //CROSSHAIR
        g2.setPaint(Color.LIGHT_GRAY);
        if(lastMove == 1 || lastMove == 11 || lastMove == 0) {
            g2.drawLine((int)(player.getxCord() + player.getWidth() + Math.cos(Math.toRadians(crosshairAngle)) * 7 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 + 1 + Math.sin(Math.toRadians(crosshairAngle)) * 7 - yOffScreen),
                    (int)(player.getxCord() + player.getWidth() - Math.cos(Math.toRadians(crosshairAngle)) * 15 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 + 1 - Math.sin(Math.toRadians(crosshairAngle)) * 15 - yOffScreen));
            g2.drawLine((int)(player.getxCord() + player.getWidth() + Math.cos(Math.toRadians(crosshairAngle)) * 7 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 + 2 + Math.sin(Math.toRadians(crosshairAngle)) * 7 - yOffScreen),
                    (int)(player.getxCord() + player.getWidth() - Math.cos(Math.toRadians(crosshairAngle)) * 15 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 + 2 - Math.sin(Math.toRadians(crosshairAngle)) * 15 - yOffScreen));
            g2.drawLine((int)(player.getxCord() + player.getWidth() + Math.cos(Math.toRadians(crosshairAngle)) * 7 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 + Math.sin(Math.toRadians(crosshairAngle)) * 7 - yOffScreen),
                    (int)(player.getxCord() + player.getWidth() - Math.cos(Math.toRadians(crosshairAngle)) * 15 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 - Math.sin(Math.toRadians(crosshairAngle)) * 15 - yOffScreen));
        }
        else {
            g2.drawLine((int)(player.getxCord() - Math.cos(Math.toRadians(-crosshairAngle)) * 7 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 + 1 - Math.sin(Math.toRadians(-crosshairAngle)) * 7 - yOffScreen),
                    (int)(player.getxCord() + Math.cos(Math.toRadians(-crosshairAngle)) * 15 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 + 1 + Math.sin(Math.toRadians(-crosshairAngle)) * 15 - yOffScreen));
            g2.drawLine((int)(player.getxCord() - Math.cos(Math.toRadians(-crosshairAngle)) * 7 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 + 2 - Math.sin(Math.toRadians(-crosshairAngle)) * 7 - yOffScreen),
                    (int)(player.getxCord() + Math.cos(Math.toRadians(-crosshairAngle)) * 15 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 + 2 + Math.sin(Math.toRadians(-crosshairAngle)) * 15 - yOffScreen));
            g2.drawLine((int)(player.getxCord() - Math.cos(Math.toRadians(-crosshairAngle)) * 7 - xOffScreen),
                    (int)(player.getyCord() + player.getHeight()/2 - Math.sin(Math.toRadians(-crosshairAngle)) * 7 - yOffScreen),
                    (int)(player.getxCord() + Math.cos(Math.toRadians(-crosshairAngle)) * 15) - xOffScreen,
                    (int)(player.getyCord() + player.getHeight()/2 + Math.sin(Math.toRadians(-crosshairAngle)) * 15 - yOffScreen));
        }



        if(editMode) {
            g2.setPaint(Color.GREEN);
            if(mouse2Pressed) {
                g2.setPaint(Color.RED);
            }
            if (getMousePosition() != null) {
                Rectangle2D ghostRect = new Rectangle2D.Double(getMousePosition().x - blockWidth/2,
                        getMousePosition().y - blockHeight/2, blockWidth, blockHeight);
                g2.draw(ghostRect);
            }
        }

        //FPS COUNTER
        g2.setPaint(Color.YELLOW);
        g2.drawString(FPS + " " , 0,10);
    }

    @Override
    public void run() {
        while (running) {
            framesCounter++;


            if((int)player.getxCord() + player.getWidth() + frameWidth/2 < mapWidth &&
                    (int) player.getxCord() + player.getWidth() - frameWidth / 2 >= 0) {
                xOffScreen = (int) player.getxCord() + player.getWidth() + 2 - frameWidth/2;
            }

            if((int)player.getyCord() + player.getHeight() + frameHeight/2 < mapHeight &&
                    (int) player.getyCord() + player.getHeight() - frameHeight/2 >= 0) {
                yOffScreen = (int) player.getyCord() + player.getHeight() + 9 - frameHeight/2;
            }


            //Setting FPS counter
            if(new Date().getTime() - date.getTime() >= 250) {
                date = new Date();
                FPS = (framesCounter - tempFramesCounter)*4;
                tempFramesCounter = framesCounter;
            }
            try {
                Thread.sleep(1000/fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(lastMove == 1 && lastMove != 11) {
                player.setxSpeed(500);
            }
            else if(lastMove == 2 && lastMove != 12) {
                player.setxSpeed(-500);
            }

            try {
                for (MovingObject mO : movingObjects) {
                    mO.calculatePosition();
                }
            }catch (Exception e) {

            }
            pixels = MovingObject.getPixels();
            /////////////////
            if (editMode && controlPressed ) {
                if(mouse2Pressed) {
                    deleteBlock();
                }
                else if(mouse1Pressed) {
                    createBlock();
                }
            }
            /////////////////
            if(upArrowPressed) {
                if(crosshairAngle > 90) {
                    crosshairAngle -= 1;
                }
            }
            if(downArrowPressed) {
                if(crosshairAngle < 270) {
                    crosshairAngle += 1;
                }
            }
            /////////////////
            if(shiftPressed && framesCounter%10 == 0) {
                shoot();
            }
            /////////////////

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
            EditMode.setPixels(pixels);
            blockWidth = EditMode.getWidth();
            blockHeight = EditMode.getHeight();
        }

        ///////////////////////////////////////////////

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
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
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            upArrowPressed = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            downArrowPressed = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
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
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            upArrowPressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            downArrowPressed = false;
        }
        ///////////////////////////////////////////

        if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
            controlPressed = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if(editMode) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                createBlock();
                mouse1Pressed = true;
            }
            if(e.getButton() == MouseEvent.BUTTON3) {
                deleteBlock();
                mouse2Pressed = true;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            mouse1Pressed = false;
        }
        if(e.getButton() == MouseEvent.BUTTON3) {
            mouse2Pressed = false;
        }

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }


    public void createBlock() {
        blockWidth = EditMode.getWidth();
        blockHeight = EditMode.getHeight();

        try {
            x1 = getMousePosition().x - blockWidth / 2 + xOffScreen;
            y1 = getMousePosition().y - blockHeight / 2 + yOffScreen;
            x2 = getMousePosition().x + blockWidth / 2 + xOffScreen;
            y2 = getMousePosition().y + blockHeight / 2 + yOffScreen;
        }catch (Exception e){

        }


        if(x1 < 0) {
            x1 = 0;
        }
        if(x2 > mapWidth) {
            x2 = mapWidth;
        }
        if(y1 < 0) {
            y1 = 0;
        }
        if(y2 > mapHeight) {
            y2 = mapHeight;
        }

        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                pixels[i][j] = 1;
            }
        }

        MovingObject.setPixels(pixels);

        refreshImage();
    }

    public void deleteBlock() {
        blockWidth = EditMode.getWidth();
        blockHeight = EditMode.getHeight();

        try {
            x1 = getMousePosition().x - blockWidth / 2 + xOffScreen;
            y1 = getMousePosition().y - blockHeight / 2 + yOffScreen;
            x2 = getMousePosition().x + blockWidth / 2 + xOffScreen;
            y2 = getMousePosition().y + blockHeight / 2 + yOffScreen;
        }catch (Exception e){

        }


        if(x1 < 0) {
            x1 = 0;
        }
        if(x2 > mapWidth) {
            x2 = mapWidth;
        }
        if(y1 < 0) {
            y1 = 0;
        }
        if(y2 > mapHeight) {
            y2 = mapHeight;
        }

        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                pixels[i][j] = 0;
            }
        }

        MovingObject.setPixels(pixels);

        refreshImage();

    }

    private void shoot() {
        if(lastMove == 1 || lastMove == 11 || lastMove == 0) {
        }
        else {
        }

        MovingObject bullet = new MovingObject(1, 3,3);
        bullet.setWeaponPower(20);
        if(lastMove == 1 || lastMove == 11 || lastMove == 0) {
            bullet.setxCord(player.getxCord() + player.getWidth());
            bullet.setyCord(player.getyCord() + player.getHeight()/2);
            bullet.setxSpeed(Math.cos(Math.toRadians(crosshairAngle)) * (-1000));
            bullet.setySpeed(Math.sin(Math.toRadians(crosshairAngle)) * (-1000));
        }
        else {
            bullet.setxCord(player.getxCord());
            bullet.setyCord(player.getyCord() + player.getHeight()/2);
            bullet.setxSpeed(Math.cos(Math.toRadians(-crosshairAngle)) * 1000);
            bullet.setySpeed(Math.sin(Math.toRadians(-crosshairAngle)) * 1000);
        }
        movingObjects.add(bullet);
    }

    private void refreshImage() {
        image = MovingObject.getImage();

        int alpha = 255;
        int red = 255;
        int green = 255;
        int blue = 255;
        int p = (alpha<<24) | (red<<16) | (green<<8) | blue;

        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                switch (pixels[i][j]){
                    case 0:
                        image.setRGB(i, j,(255<<24) | (60<<16) | (50<<8) | 40);
                        break;
                    case 1:
                        image.setRGB(i, j, (255<<24) | (130<<16) | (100<<8) | 80);
                        break;
                }
            }
        }

        MovingObject.setImage(image);
    }
}