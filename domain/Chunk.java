/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javax.imageio.ImageIO;

/**
 *
 * @author maikel
 */
public abstract class Chunk implements Serializable{
    protected int x, y;
    protected int size;
    protected byte[] imageBytes;

    public Chunk(byte[] image, int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.imageBytes = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public BufferedImage bytesToImage() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(this.imageBytes);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        return bImageFromConvert;
    }
    
    public abstract void draw(GraphicsContext gc)throws IOException;
    public abstract boolean chunkClicked(int xMouse, int yMouse);
}
