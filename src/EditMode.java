import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

public class EditMode {
    private static int width = 100, height = 100;
    private static int[][] pixels;

    public static void keyTyped(KeyEvent e) {
    }

    public static void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
            width += 10 ;
        }
        if(e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
            if(width >= 20) {
                width -= 10;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
            height += 10;
        }
        if(e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            if(height >= 20) {
                height -= 10;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
            int temp = height;
            height = width;
            width = temp;
        }
        if(e.getKeyCode() == KeyEvent.VK_S) {
            saveMap();
        }
    }

    public static void keyReleased(KeyEvent e) {

    }

    public static void mouseClicked(MouseEvent e) {

    }

    public static void mousePressed(MouseEvent e) {

    }

    public static void mouseReleased(MouseEvent e) {

    }

    public static void mouseEntered(MouseEvent e) {

    }

    public static void mouseExited(MouseEvent e) {

    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        EditMode.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        EditMode.height = height;
    }

    public static void setPixels(int[][] pixels1) {
        pixels = pixels1;
    }

    private static void saveMap() {
        File file = new File("map.txt");
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(pixels.length + "");
            bw.newLine();
            bw.write(pixels[0].length + "");

            for(int i = 0; i < pixels.length; i++) {
                for(int j = 0; j < pixels[0].length; j++) {
                    bw.newLine();
                    bw.write(pixels[i][j] + "");
                }
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Unable to write to file" + file.toString());
        }

    }
}