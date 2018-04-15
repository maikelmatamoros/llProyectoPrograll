/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sun.java2d.pipe.DrawImage;

/**
 *
 * @author maikel
 */
public class MosaicChunk {

    private int x, y;
    private int size;
    private Image image;

    public MosaicChunk(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x * size, y * size, size, size);
    }

    public boolean mousePresionado(int xMouse, int yMouse) {
        if ((xMouse >= this.x * size && xMouse <= this.x * size + size)
                && (yMouse >= this.y * size && yMouse <= this.y * size + size)) {
                return true;

        }
        return false;
    } // mousePresionado
}
