/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author maikel
 */
public class Chunk {
    private Image image;
    private int x,y;
    private int chunkWidth,chunkHeight;

    public Chunk(Image image, int x, int y, int chunkWidth, int chunkHeight) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    public int getChunkWidth() {
        return chunkWidth;
    }

    public void setChunkWidth(int chunkWidth) {
        this.chunkWidth = chunkWidth;
    }

    public int getChunkHeight() {
        return chunkHeight;
    }

    public void setChunkHeight(int chunkHeight) {
        this.chunkHeight = chunkHeight;
    }
    
    public void draw(GraphicsContext gc){
        gc.drawImage(image, (x*chunkWidth)+(1+x)*10, (y*chunkHeight)+(1+y)*10);
    }
        public boolean mousePresionado(int xMouse, int yMouse) {
        if ((xMouse >= this.x*chunkWidth && xMouse <= this.x*chunkWidth + this.chunkWidth)
                && (yMouse >= this.y*chunkHeight && yMouse <= this.y*chunkHeight + this.chunkHeight)) {
            
            return true;
        }
        return false;
    } // mousePresionado
    

    
    
}
