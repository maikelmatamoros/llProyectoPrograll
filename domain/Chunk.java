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
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javax.imageio.ImageIO;

/**
 *
 * @author maikel
 */
public class Chunk implements Serializable {

    private int x, y;
    private int size;
    private byte[] imageBytes;

    public Chunk(byte[]image,int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.imageBytes=image;
        
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
    
    private BufferedImage bytesToImage() throws IOException{
        ByteArrayInputStream in = new ByteArrayInputStream(this.imageBytes);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        return bImageFromConvert;
    }



    public void draw(GraphicsContext gc, int aux) throws IOException {
        if (aux == 0) {
            gc.drawImage(SwingFXUtils.toFXImage(bytesToImage(), null), (x * size) + (1 + x) * 10, (y * size) + (1 + y) * 10,size,size);
        } else {
            gc.drawImage(SwingFXUtils.toFXImage(bytesToImage(), null), x * size, y * size, size, size);
        }
    }

    public boolean chunckMosaicClicked(int xMouse, int yMouse) {
        if ((xMouse >= this.x * size && xMouse <= this.x * size + size)
                && (yMouse >= this.y * size && yMouse <= this.y * size + size)) {
            return true;

        }
        return false;
    } // mousePresionado
   
        public boolean chunckImageClicked(int xMouse, int yMouse) {
        if ((xMouse >= (x * size) + (1 + x) * 10 && xMouse <= (x * size) + (1 + x) * 10 + this.size)
                && (yMouse >= (y * size) + (1 + y) * 10 && yMouse <= (y * size) + (1 + y) * 10 + this.size)) {
            
            return true;
        }
        return false;
    } // mousePresionado

    @Override
    public String toString() {
        return "MosaicChunk{" + "x=" + x + ", y=" + y + ", size=" + size + '}';
    }

}
