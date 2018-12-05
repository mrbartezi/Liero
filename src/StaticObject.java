import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

public class StaticObject {
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private static int frameWidth, frameHeight;
    private static int[][] pixels;
    private static BufferedImage image;

    public StaticObject(int x1, int y1, int x2, int y2) {
        if (x1 < 0) {
            x1 = 0;
        }
        if (x2 > frameWidth) {
            x2 = frameWidth;
        }
        if (y1 < 0) {
            y1 = 0;
        }
        if (y2 > frameHeight) {
            y2 = frameHeight;
        }

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

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

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public static void setPixels(int[][] pixels) {
        StaticObject.pixels = pixels;
    }

    public static int[][] getPixels() {
        return pixels;
    }

    public static void setFrameWidth(int frameWidth) {
        StaticObject.frameWidth = frameWidth;
    }

    public static void setFrameHeight(int frameHeight) {
        StaticObject.frameHeight = frameHeight;
    }

    public static BufferedImage getImage() {
        return image;
    }

    public static void setImage(BufferedImage img) {
       image = img;
    }
}