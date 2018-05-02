package domain;

import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public class ChunkMosaic extends Chunk {

    private int rotation;
    private int horizontal, vertical, negativoHorizontal, negativoVertical;

    public ChunkMosaic(byte[] image, int x, int y, int size) {
        super(image, x, y, size);
        this.rotation = 0;
        this.horizontal = 0;
        this.vertical = 0;
        this.negativoHorizontal = 1;
        this.negativoVertical = 1;
    } // constructor

    public void rotate(int click) {
        if (click == 0) {
            if (rotation < 360) {
                rotation += 90;
            } else {
                rotation = rotation * 0 + 90;
            }
        } else {
            if (rotation > 0) {
                rotation -= 90;
            } else {
                rotation = 360 - 90;
            }
        }
    } // rotate

    public void setRotation() {
        this.rotation = 0;
        flipHorizontal(0);
        flipVertical(0);
    } // setRotation

    public void flipHorizontal(int click) {
        if (click == 1) { // derecha
            if (this.horizontal == this.size) {
                this.horizontal = 0;
                this.negativoHorizontal = 1;
            } else {
                this.horizontal = this.size;
                this.negativoHorizontal = -1;
            }
        } else { // izquierda
            this.horizontal = 0;
            this.negativoHorizontal = 1;
        }  
    } // flip

    public void flipVertical(int click) {
        if (click == 1) { // abajo
            if(this.vertical == this.size){
                this.vertical = 0;
                this.negativoVertical = 1;
            }else{
                this.vertical = this.size;
                this.negativoVertical = -1;
            }
        } else { // arriba
            this.vertical = 0;
            this.negativoVertical = 1;
        }
    } // flip

    @Override
    public void draw(GraphicsContext gc) throws IOException {
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(bytesToImage(), null));
        imageView.setRotate(imageView.getRotate() + this.rotation);
        SnapshotParameters snapshot = new SnapshotParameters();
        gc.drawImage(imageView.snapshot(snapshot, null), this.x * this.size + this.horizontal, this.y * 
                this.size + this.vertical, this.size * this.negativoHorizontal, this.size * this.negativoVertical);
    } // draw

    @Override
    public boolean chunkClicked(int xMouse, int yMouse) {
        if ((xMouse >= this.x * this.size && xMouse <= this.x * this.size + this.size)
                && (yMouse >= this.y * this.size && yMouse <= this.y * this.size + this.size)) {
            return true;
        }
        return false;
    } // chunkClicked

} // fin de la clase
