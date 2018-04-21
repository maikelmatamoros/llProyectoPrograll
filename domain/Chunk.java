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
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;

/**
 *
 * @author maikel
 */
public class Chunk implements Serializable {

    private int x, y, rotation;
    private int size;
    private byte[] imageBytes;

    public Chunk(byte[] image, int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.imageBytes = image;
        this.rotation = 0;
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

    private BufferedImage bytesToImage() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(this.imageBytes);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        return bImageFromConvert;
    }

    public void draw(GraphicsContext gc, int aux) throws IOException {
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(bytesToImage(), null));
        imageView.setRotate(imageView.getRotate() + rotation);
        SnapshotParameters snapshot = new SnapshotParameters();
        //System.out.println(imageView.getRotate());
        if (aux == 0) {
            gc.drawImage(imageView.snapshot(snapshot, null), (x * size) + (1 + x) * 10, (y * size) + (1 + y) * 10, size, size);
        } else {
            gc.drawImage(imageView.snapshot(snapshot, null), x * size, y * size, size, size);
        }
    }

    public void rotate(int click) {
        if (click == 0) {
            if (rotation < 360) {
                rotation += 90;
            } else {
                rotation = rotation * 0 + 90;
            }
        }else{
            if (rotation > 0) {
                rotation -= 90;
            } else {
                rotation = 360 - 90;
            }
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
