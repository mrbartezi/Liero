import java.util.ArrayList;
import java.util.HashMap;

public class MovingObject {
    private static int frameWidth;
    private static int frameHeight;
    private int width;
    private int height;
    private double xCord = 0.0;
    private double yCord = 0.0;
    private double xSpeed = 0.0;
    private double ySpeed = 0.0;
    private int id;
    private double gravAcc = 2500.0;
    private double xAcc = 0.0;
    private double yAcc = 0.0;
    private int time = 0;
    private static int fps = 120;
    private static ArrayList<StaticObject> staticObjectsList;
    private static int[][] pixels;
    private boolean changePosVer;

    public MovingObject(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void calculatePosition() {

        // Air resistance

        xAcc =  -30*xSpeed;
        //yAcc = -1/2 * ySpeed;


        xSpeed += xAcc / fps;
        ySpeed += gravAcc / fps + yAcc / fps;

        xCord += xSpeed/fps;
        yCord += ySpeed/fps;

        checkIfInFrame();
    }

    public void checkIfInFrame() {
        pixels = StaticObject.getPixels();

        if(yCord < 0) {
            yCord = 0;
            ySpeed = 0;
        }

        if (yCord > frameHeight - height) {
            yCord = frameHeight - height;
        }

        if(xCord <= 0) {
            xCord = 0;
        }

        if(xCord >= frameWidth - width) {
            xCord = frameWidth - width;
        }
        changePosVer = false;

        //Checking if static object is under moving object.
        int yBounds = (int)(yCord + height + ySpeed / fps + 1);
        if((int)(yCord + height + ySpeed / fps + 1) > frameHeight) {
            yBounds = frameHeight;
        }

        for(int i = (int)xCord; i < xCord + width; i++) {
            for(int j = (int)yCord + height; j < yBounds; j++) {
                if(pixels[i][j] == 1) {
                    yCord = j - height - 1;
                    ySpeed = 0;
                    changePosVer = true;
                }
            }
        }

        //Checking if static object is over moving object.
        yBounds = (int)(yCord + ySpeed/fps - 1);
        if((int)(yCord + ySpeed/fps - 1) < 0) {
            yBounds = 0;
        }

        for(int i = (int)xCord; i < xCord + width; i++) {
            for(int j =(int)yCord; j > yBounds; j--) {
                if(pixels[i][j] == 1) {
                    yCord = j + 1;
                    ySpeed = 0;
                    changePosVer = true;
                }
            }
        }

        //Checking if static object is on the right of the moving object.
        if(!changePosVer) {
            int xBounds = (int) (xCord + width + xSpeed / fps + 1);
            if ((int) (xCord + width + xSpeed / fps + 1) > frameWidth) {
                xBounds = frameWidth;
            }

            for (int j = (int) yCord; j < yCord + height; j++) {
                for (int i = xBounds; i > (int) xCord + width; i--) {
                    if (pixels[i][j] == 1) {
                        xCord = i - width - 2;
                        xSpeed = 0;
                    }
                }
            }

            //Checking if static object is on the left of the moving object.

            xBounds = (int) (xCord + xSpeed / fps - 1);
            if ((int) (xCord + xSpeed / fps - 1) < 0) {
                xBounds = 0;
            }

            for (int j = (int) yCord; j < yCord + height; j++) {
                for (int i = xBounds; i < xCord; i++) {
                    if (pixels[i][j] == 1) {
                        xCord = i + 2;
                        xSpeed = 0;
                    }
                }
            }
        }



        /*
        for(StaticObject s : staticObjectsList) {
            if (xCord > s.getX1() - width && xCord < s.getX2()) {
                if (yCord + height >= s.getY1() && yCord + height < s.getY1() + ySpeed / fps + 1) {
                    yCord = s.getY1() - height - 1;
                    ySpeed = 0;
                }

                if (yCord <= s.getY2() && yCord >= s.getY2() + ySpeed / fps - 1) {
                    yCord = s.getY2();
                    ySpeed = 0;
                }
            }

            if (yCord + height >= s.getY1() + ySpeed / fps + 1 && yCord <= s.getY2()) {
                if (xCord + width >= s.getX1() && xCord + width <= s.getX1() + 5) {
                    xCord = s.getX1() - width - 1;
                }
                if (xCord <= s.getX2() && xCord >= s.getX2() - 5) {
                    xCord = s.getX2();
                }
            }
        }
        */
    }

    public ArrayList<StaticObject> getStaticObjectsList() {
        return staticObjectsList;
    }

    public static void setStaticObjectsList(ArrayList<StaticObject> List) {
        staticObjectsList = List;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setxAcc(double xAcc) {
        this.xAcc = xAcc;
    }

    public void setyAcc(double yAcc) {
        this.yAcc = yAcc;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static void setFps(int fps1) {
        fps = fps1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getxCord() {
        return xCord;
    }

    public void setxCord(double xCord) {
        this.xCord = xCord;
    }

    public double getyCord() {
        return yCord;
    }

    public void setyCord(double yCord) {
        this.yCord = yCord;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public static void setFrameWidth(int Width) {
        frameWidth = Width;
    }

    public static void setFrameHeight(int Height) {
        frameHeight = Height;
    }

    public void setGravAcc(double gravAcc) {
        this.gravAcc = gravAcc;
    }
}