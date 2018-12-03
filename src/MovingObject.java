import java.util.ArrayList;

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
    private int fps = 60;
    private static ArrayList<StaticObject> staticObjectsList;

    public MovingObject(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void calculatePosition() {

        // Air resistance
        /*
        xAcc = -1/2 * xSpeed;
        yAcc = -1/2 * ySpeed;
        */

        xSpeed += xAcc / fps;
        ySpeed += gravAcc / fps + yAcc / fps;

        xCord += xSpeed/fps;
        yCord += ySpeed/fps;

        checkIfInFrame();
    }

    public void checkIfInFrame() {

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

        for(StaticObject s : staticObjectsList) {
            if(xCord >= s.getX1() - width && xCord <= s.getX2()){
                if(yCord + height >= s.getY1() && yCord + height < s.getY1() + ySpeed/fps + 1) {
                    yCord = s.getY1() - height;
                    ySpeed = 0;
                }
            }
        }
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

    public void setFps(int fps) {
        this.fps = fps;
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