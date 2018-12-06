import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class EditMode {
    private static int width = 100, height = 100;

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

    private void saveMap() {

    }
}