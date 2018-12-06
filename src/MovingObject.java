import java.awt.image.BufferedImage;

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
    private static int fps = 120;
    private static int[][] pixels;
    private static BufferedImage image;
    private int weaponPower = 0;

    public MovingObject(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void calculatePosition() {
        if(id == 0) {
            // Air resistance
            xAcc = -30 * xSpeed;
            //yAcc = -1/2 * ySpeed;


            xSpeed += xAcc / fps;

            //Don't add gravity acceleration to ySpeed when on ground.
            if((int)yCord + height + 1 < frameHeight) {
                for (int i = (int) xCord; i < (int) xCord + width; i++) {
                    if (pixels[i][(int) yCord + height + 1] == 1) {
                        break;
                    }
                    if (i == (int) xCord + width - 1) {
                        ySpeed += gravAcc / fps;
                    }
                }
            }
            ySpeed += yAcc / fps;

            if (xSpeed < -1) {
                loop1:
                for (int i = 0; i > xSpeed / fps * 10; i--) {
                    if (xCord > 0) {
                        xCord -= 0.1;
                    }
                    else {
                        xSpeed = 0;
                    }
                    if (i % 10 == 0) {
                        for (int j = (int) yCord; j < (int) yCord + height; j++) {
                            if (pixels != null) {
                                if (pixels[(int) xCord][j] == 1) {
                                    xCord += 1;
                                    xSpeed = 0;
                                    break loop1;
                                }
                            }
                        }
                    }
                }
            } else if (xSpeed >= 1) {
                loop2:
                for (int i = 0; i < xSpeed / fps * 10; i++) {
                    if (xCord + width < frameWidth - 1) {
                        xCord += 0.1;
                    }
                    else {
                        xSpeed = 0;
                    }
                    if (i % 10 == 0) {
                        for (int j = (int) yCord; j < (int) yCord + height; j++) {
                            if (pixels != null) {
                                if (pixels[(int) xCord + width][j] == 1) {
                                    xCord -= 1;
                                    xSpeed = 0;
                                    break loop2;
                                }
                            }
                        }
                    }
                }
            } else {
                xSpeed = 0;
            }
            if (ySpeed < -1) {
                loop3:
                for (int i = 0; i < (-1) * ySpeed / fps * 10; i++) {
                    if (yCord > 0) {
                        yCord -= 0.1;
                    }
                    else {
                        ySpeed = 0;
                    }
                    if (i % 10 == 0) {
                        for (int j = (int) xCord; j < xCord + width; j++) {
                            if (pixels != null) {
                                if (pixels[j][(int) yCord] == 1) {
                                    yCord += 1;
                                    ySpeed = 0;
                                    break loop3;
                                }
                            }
                        }
                    }
                }
            } else if (ySpeed >= 1.0) {
                loop4:
                for (int i = 0; i < ySpeed / fps * 10; i++) {
                    if (yCord + height < frameHeight - 1) {
                        yCord += 0.1;
                    }
                    else {
                        ySpeed = 0;
                    }
                    if (i % 10 == 0) {
                        for (int j = (int) xCord; j < xCord + width; j++) {
                            if (pixels != null) {
                                if (pixels[j][(int) yCord + height] == 1) {
                                    yCord -= 1;
                                    ySpeed = 0;
                                    break loop4;
                                }
                            }
                        }
                    }
                }
            } else {
                ySpeed = 0;
            }
        }
        //////
        if(id >= 1 && id < 100) {
            if (xSpeed < -1) {
                loop1:
                for (int i = 0; i > xSpeed / fps * 10; i--) {
                    if (xCord > 0) {
                        xCord -= 0.1;
                    }
                    else {
                        id = -1;
                    }
                    if (i % 10 == 0) {
                        for (int j = (int) yCord; j < (int) yCord + height; j++) {
                            if (pixels != null) {
                                if (pixels[(int) xCord][j] == 1) {
                                    for(int k = (int)yCord - weaponPower; k < yCord + height + weaponPower; k++) {
                                        for(int l = (int)xCord - weaponPower; l < xCord + width + weaponPower; l++) {
                                            pixels[l][k] = 0;
                                            image.setRGB(l, k, (255<<24) | (60<<16) | (50<<8) | 40);
                                        }
                                    }
                                    id = -1;
                                    break loop1;
                                }
                            }
                        }
                    }
                }
            } else if (xSpeed >= 1) {
                loop2:
                for (int i = 0; i < xSpeed / fps * 10; i++) {
                    if (xCord + width < frameWidth - 1) {
                        xCord += 0.1;
                    }
                    else {
                        id = -1;
                    }
                    if (i % 10 == 0) {
                        for (int j = (int) yCord; j < (int) yCord + height; j++) {
                            if (pixels != null) {
                                if (pixels[(int) xCord + width][j] == 1) {
                                    for(int k = (int)yCord - weaponPower; k < yCord + height + weaponPower; k++) {
                                        for(int l = (int) xCord - weaponPower; l < xCord + width + weaponPower; l++) {
                                            pixels[l][k] = 0;
                                            image.setRGB(l, k,(255<<24) | (60<<16) | (50<<8) | 40);
                                        }
                                    }
                                    id = -1;
                                    break loop2;
                                }
                            }
                        }
                    }
                }
            } else {
                xSpeed = 0;
            }
            if (ySpeed < -1) {
                loop3:
                for (int i = 0; i < (-1) * ySpeed / fps * 10; i++) {
                    if (yCord > 0) {
                        yCord -= 0.1;
                    }
                    else {
                        id = -1;
                    }
                    if (i % 10 == 0) {
                        for (int j = (int) xCord; j < xCord + width; j++) {
                            if (pixels != null) {
                                if (pixels[j][(int) yCord] == 1) {
                                    for(int k = (int) xCord - weaponPower; k < xCord + width + weaponPower; k++) {
                                        for(int l = (int)yCord - weaponPower; l < yCord + height + weaponPower; l++) {
                                            pixels[k][l] = 0;
                                            image.setRGB(k, l,(255<<24) | (60<<16) | (50<<8) | 40);
                                        }
                                    }

                                    id = -1;
                                    break loop3;
                                }
                            }
                        }
                    }
                }
            } else if (ySpeed >= 1.0) {
                loop4:
                for (int i = 0; i < ySpeed / fps * 10; i++) {
                    if (yCord + height < frameHeight - 1) {
                        yCord += 0.1;
                    }
                    else {
                        id = -1;
                    }
                    if (i % 10 == 0) {
                        for (int j = (int) xCord; j < xCord + width; j++) {
                            if (pixels != null) {
                                if (pixels[j][(int) yCord + height] == 1) {
                                    for(int k = (int) xCord - weaponPower; k < xCord + width + weaponPower; k++) {
                                        for(int l = (int) yCord - weaponPower; l < yCord + height + weaponPower; l++) {
                                            pixels[k][l] = 0;
                                            image.setRGB(k, l,(255<<24) | (60<<16) | (50<<8) | 40);
                                        }
                                    }
                                    id = -1;
                                    break loop4;
                                }
                            }
                        }
                    }
                }
            } else {
                ySpeed = 0;
            }
        }
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

    public static int[][] getPixels() {
        return pixels;
    }

    public static void setPixels(int[][] pixels) {
        MovingObject.pixels = pixels;
    }

    public static BufferedImage getImage() {
        return image;
    }

    public static void setImage(BufferedImage image) {
        MovingObject.image = image;
    }

    public void setWeaponPower(int weaponPower) {
        this.weaponPower = weaponPower;
    }
}