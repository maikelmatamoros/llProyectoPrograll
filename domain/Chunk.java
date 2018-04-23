package domain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javax.imageio.ImageIO;

public abstract class Chunk implements Serializable {

    protected int x, y;
    protected int size;
    protected byte[] imageBytes;

    public Chunk(byte[] image, int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.imageBytes = image;
    } // constructor

    public int getX() {
        return x;
    } // getX

    public void setX(int x) {
        this.x = x;
    } // setX

    public int getY() {
        return y;
    } // getY

    public void setY(int y) {
        this.y = y;
    } // setY

    public int getSize() {
        return size;
    } // getSize

    public void setSize(int size) {
        this.size = size;
    } // setSize

    public byte[] getImageBytes() {
        return imageBytes;
    } // getImageBytes

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    } // setImageBytes

    public BufferedImage bytesToImage() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(this.imageBytes);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        return bImageFromConvert;
    } // bytesToImage

    public abstract void draw(GraphicsContext gc) throws IOException;

    public abstract boolean chunkClicked(int xMouse, int yMouse);

} // fin de la clase
